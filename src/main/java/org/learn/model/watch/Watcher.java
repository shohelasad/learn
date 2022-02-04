package org.learn.model.watch;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.learn.model.Channel;
import org.learn.model.User;
import org.learn.providers.SessionFactoryCreator;

@Entity
public class Watcher {
	@GeneratedValue @Id
	private Long id;
	
	private boolean active = true;

	@OneToOne
	private final User watcher;
	
	@ManyToMany(mappedBy="watchers")
	private List<Channel> channels;
	
	@ManyToMany(mappedBy="watchers")
	private List<User> users;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Watcher() {
		this(null);
	}
	
	public Watcher(User watcher){
		this.watcher = watcher;
		this.createdAt = new DateTime();
	}

	public void inactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public User getWatcher() {
		return watcher;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Watcher other = (Watcher) obj;
		if(id == null || other.id == null) return false;
		if (id.equals(other.id))
			return true;
		return false;
	}

	public Long getId() {
		return this.id;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
