package org.learn.dao;

import static org.learn.model.MarkedText.notMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.learn.builder.QuestionBuilder;
import org.learn.model.Answer;
import org.learn.model.AnswerInformation;
import org.learn.model.Comment;
import org.learn.model.Flag;
import org.learn.model.FlagType;
import org.learn.model.LoggedUser;
import org.learn.model.LoginMethod;
import org.learn.model.News;
import org.learn.model.NewsInformation;
import org.learn.model.Question;
import org.learn.model.SanitizedText;
import org.learn.model.Tag;
import org.learn.model.User;
import org.learn.model.Vote;
import org.learn.model.VoteType;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

/**
 * Constructor from this class should not be used anywhere beside tests.
 */
public abstract class TestCase {
	
	@Before
	public void fixTime() {
		DateTimeUtils.setCurrentMillisSystem();
	}

	private QuestionBuilder questionBuilder = new QuestionBuilder();
	
	protected Answer answer(String description, Question question, User author) {
		Answer q = new Answer(new AnswerInformation(notMarked(description), new LoggedUser(author, null), "default commentdefault commentdefault commentdefault comment")
							, question, author);
		return q;
	}
	
	protected Question question(User author, Tag... tags){
		ArrayList<Tag> tagsList = new ArrayList<>(Arrays.asList(tags));
		if (tagsList.isEmpty())
			tagsList.add(tag("teste"));
		Question question = questionBuilder.withAuthor(author).withTags(tagsList).build();
		return question;
	}
	protected Question question(User author, List<Tag> tags){
		return question(author, tags.toArray(new Tag[tags.size()]));
	}
	
	protected User user(String name, String email) {
	    User user = new User(SanitizedText.fromTrustedText(name), email);
	    user.confirmEmail();
	    return user;
	}
	
	protected LoggedUser loggedUser(String name, String email, Long id) {
		return new LoggedUser(user(name, email, id), null);
	}
	
	protected User userWithPassword(String name, String email) {
		User user = user(name, email);
		LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, "123456");
		user.add(brutalLogin);
		return user;
	}
	
	protected Vote vote(User author, VoteType type, Long id) {
	    Vote v = new Vote(author, type);
	    setId(v, id);
	    return v;
	}
	
	protected User user(String name, String email, Long id) {
	    User user = user(name, email);
	    setId(user, id);
	    return user;
	}
	
	protected Flag flag(FlagType flagType, User author) {
		return new Flag(flagType, author);
	}
	
    protected AnswerInformation answerInformation(String description, User otherUser, Answer answer) {
        return new AnswerInformation(notMarked(description), new LoggedUser(otherUser, null), answer, "comment");
    }
    
    protected Comment comment(User author, String comment) {
    	return comment(author, comment, DateTime.now());
    }
    
    protected Comment comment(final User author, final String comment, DateTime when) {
    	return TimeMachine.goTo(when).andExecute(new Block<Comment>() {
			@Override
			public Comment run() {
				return new Comment(author, notMarked(comment));
			}
		});
    }
    
    protected void setId(Object o, Long id) {
        new Mirror().on(o).set().field("id").withValue(id);
    }

    protected Tag tag(String name){
    	return new Tag(name, "", null);
    }
    
    protected User moderator() {
    	return user("moderator", "moderator@brutal.com").asModerator();
    }
    
    protected News news(String title, String description, User author) {
    	NewsInformation newsInformation = new NewsInformation(title, notMarked(description), new LoggedUser(author, null), "comment comment comment");
    	return new News(newsInformation, author);
	}
    
}

