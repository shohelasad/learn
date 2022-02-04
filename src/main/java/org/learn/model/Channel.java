package org.learn.model;


import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;
import static org.hibernate.annotations.CascadeType.ALL;
import static org.learn.sanitizer.QuotesSanitizer.sanitize;

import java.util.*;


import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.*;
import org.joda.time.DateTime;
import org.learn.model.interfaces.Moderatable;
import org.learn.model.interfaces.RssContent;
import org.learn.model.interfaces.Taggable;
import org.learn.model.interfaces.ViewCountable;
import org.learn.model.interfaces.Votable;
import org.learn.model.interfaces.Watchable;
import org.learn.model.watch.Watcher;
import org.learn.providers.SessionFactoryCreator;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="cache")
@SQLDelete(sql = "update Channel set deleted = true where id = ?")
@Where(clause = "deleted = 0")
@Entity
public class Channel extends Moderatable implements Post, Taggable, ViewCountable, Watchable, RssContent, ReputationEventContext {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	//@Cascade(ALL)
	@JoinColumn(name="parent_id")
	private Channel parent;
	
	@OneToOne(optional = false, fetch = EAGER)
	@Cascade(SAVE_UPDATE)
	@NotNull
	private ChannelInformation information = null;
	
	@OneToMany(mappedBy="channel")
	@Cascade(SAVE_UPDATE)
	private List<ChannelInformation> history = new ArrayList<>();
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private DateTime lastUpdatedAt = new DateTime();

	@ManyToOne
	private User lastTouchedBy = null;

	@ManyToOne(fetch = EAGER)
	private User author;

	private long views = 0l;
	
	@JoinTable(name = "Channel_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();

	private long voteCount = 0l;

	@Embedded
	private final ChannelCommentList comments = new ChannelCommentList();
	
	@JoinTable(name = "Channel_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	@Embedded
	private final ModerationOptions moderationOptions = new ModerationOptions();

	@ManyToMany
	  @JoinTable(
	      name="Channel_Watchers",
	      joinColumns=@JoinColumn(name="channel_id", referencedColumnName="id"),
	      inverseJoinColumns=@JoinColumn(name="watchers_id", referencedColumnName="id"))
	private final List<Watcher> watchers = new ArrayList<>();

	@OneToMany
	private Set<Attachment> attachments = new HashSet<>();

    private boolean deleted = false;
    
	/**
	 * @deprecated hibernate eyes only
	 */
	public Channel() {
		this.information = null;
	}

	public Channel(ChannelInformation channelInformation, User author) {
		setAuthor(author);
		enqueueChange(channelInformation, UpdateStatus.NO_NEED_TO_APPROVE);
	}

	@Override
	public String toString() {
		return "Channel [title=" + information.getTitle() + ", createdAt=" + createdAt + "]";
	}

	public void setAuthor(User author) {
		if (this.author != null)
			return;
		this.author = author;
		touchedBy(author);
	}

	public void touchedBy(User author) {
		this.lastTouchedBy = author;
		this.lastUpdatedAt = new DateTime();
	}

	void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public DateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public User getLastTouchedBy() {
		return lastTouchedBy;
	}


	@Override
	public Channel getMainThread() {
		return this; //sorry, I need to get a Question from a Comment.
	}
	
	public void ping() {
		this.views++;
	}

	@Override
	public void substitute(Vote previous, Vote vote) {
		this.voteCount += vote.substitute(previous, votes);
	}
	
	public void remove(Vote previous) {
		votes.remove(previous);
		this.voteCount -= previous.getCountValue();
//		addUserInteraction(vote.getAuthor());
	}

	@Override
	public long getVoteCount() {
		return voteCount;
	}

	public User getAuthor() {
		return author;
	}

	@Override
	public Comment add(Comment comment) {
		this.comments.add(comment);
		return comment;
	}
	
	@Override
	public List<Comment> getVisibleCommentsFor(User user) {
		return comments.getVisibleCommentsFor(user);
	}

	public String getTitle() {
		return information.getTitle();
	}

	public String getDescription() {
		return information.getDescription();
	}

	public String getSluggedTitle() {
		return information.getSluggedTitle();
	}

	public String getMarkedDescription() {
		return information.getMarkedDescription();
	}

	public String getSanitizedDescription() {
		return sanitize(information.getDescription());
	}

	public String getTagsAsString(String separator) {
		return information.getTagsAsString(separator);
	}
	
	@Override
	public ChannelInformation getInformation() {
		return information;
	}
	
	public List<Tag> getTags() {
		return information.getTags();
	}
	
	public List<TagUsage> getTagsUsage() {
		ArrayList<TagUsage> tagsUsage = new ArrayList<>();
		for (Tag tag : this.getTags()) {
			tagsUsage.add(new TagUsage(tag, tag.getUsageCount()));
		}
		return tagsUsage;
	}
	
	public Tag getMostImportantTag(){
		List<Tag> tags = information.getTags();
		if(tags.isEmpty()){
			throw new IllegalStateException("a channel must have at least one tag");
		}
		return tags.get(0);
	}

	public UpdateStatus updateWith(ChannelInformation information, Updater updater) {
	    return updater.update(this, information);
	}
	
	protected void addHistory(Information newInformation) {
		this.history.add((ChannelInformation) newInformation);
	}

	public List<ChannelInformation> getHistory() {
		return history;
	}
	
	private void setInformation(ChannelInformation newInformation) {
		newInformation.setModeratable(this);
		if (this.information != null) {
			for (Tag tag : this.information.getTags()) {
				tag.decrementUsage();
			}
		}
		for (Tag tag : newInformation.getTags()) {
			tag.incrementUsage();
		}
        this.information = newInformation;
    }
	
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	protected void updateApproved(Information approved) {
		ChannelInformation approvedChannel = (ChannelInformation) approved;
		this.touchedBy(approvedChannel.getAuthor());
		setInformation(approvedChannel);
	}

	@Override
	public boolean isEdited() {
		return lastUpdatedAt.isAfter(createdAt);
	}
	
	public String getTypeName() {
        return getType().getSimpleName();
    }

    @Override
    public Class<? extends Votable> getType() {
        return Channel.class;
    }
    
    public boolean wasMadeBy(User author) {
        return this.author.getId().equals(author.getId());
    }

	@Override
	public void add(Watcher watcher) {
		watchers.add(watcher);
	}

	@Override
	public void remove(Watcher watcher) {
		watchers.remove(watcher);
	}

	@Override
	public List<Watcher> getWatchers() {
		return watchers;
	}

	@Override
	public void add(Flag flag) {
		flags.add(flag);
	}
	
	@Override
	public boolean alreadyFlaggedBy(User user) {
		for (Flag flag : flags) {
			if (flag.createdBy(user))
				return true;
		}
		return false;
	}

	@Override
	public void remove() {
		this.moderationOptions.remove();
	}
	
	@Override
	public boolean isVisible() {
		return this.moderationOptions.isVisible();
	}
	
	@Override
	public boolean isVisibleForModeratorAndNotAuthor(User user) {
		return !this.isVisible() && user != null && !user.isAuthorOf(this);
	}
	
	public boolean isVisibleFor(User user) {
		return this.isVisible() || (user != null && (user.isModerator() || user.isAuthorOf(this)));
	}

	@Override
	public boolean hasPendingEdits() {
		for (ChannelInformation information : history) {
			if(information.isPending()) return true;
		}
		return false;
	}
	
	@Override
	public String getTypeNameKey() {
		return "channel.type_name";
	}

	@Override
	public void deleteComment(Comment comment) {
		this.comments.delete(comment);
	}

	public String getTrimmedContent() {
        String markedDescription = getMarkedDescription();
        if (markedDescription.length() < 200)
            return markedDescription;
        return markedDescription.substring(0, 200) + "...";
    }
    
    public boolean hasAuthor(User user) {
		return user.getId().equals(author.getId());
    }

	@Override
	public Question getQuestion() {
		return null; //no single question
	}

	public String getMetaDescription() {
		String fullMeta = getTitle() + " " + getMarkedDescription();
		int index = Math.min(fullMeta.length(), 200);
		return fullMeta.substring(0, index);
	}

	public String getLinkPath() {
		return id + "-" + getSluggedTitle();
	}

	public boolean isInactiveForOneMonth() {
		return lastUpdatedAt.isBefore(new DateTime().minusMonths(1));
	}
	
	@Override
	public List<Vote> getVotes() {
		return votes;
	}
	
	public List<Flag> getFlags() {
		return flags;
	}

	public boolean hasTags() {
		return !this.getTags().isEmpty();
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void add(List<Attachment> attachments) {
		this.attachments.addAll(attachments);
	}

	public void remove(Attachment attachment) {
		this.attachments.remove(attachment);
	}

	public void removeAttachments() {
		this.attachments.clear();
	}	

	public boolean isDeletable() {
		return flags.isEmpty() && comments.isEmpty();
	}

	public List<Comment> getAllComments() {
		return comments.getAll();
	}

	public Channel getParent() {
		return parent;
	}

	public void setParent(Channel parent) {
		this.parent = parent;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
