package org.learn.reputation.rules;

import static org.junit.Assert.assertEquals;
import static org.learn.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_DOWN;
import static org.learn.reputation.rules.KarmaCalculator.MY_ANSWER_VOTED_UP;
import static org.learn.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_DOWN;
import static org.learn.reputation.rules.KarmaCalculator.MY_QUESTION_VOTED_UP;

import org.junit.Test;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.TestCase;
import org.learn.model.Answer;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.VoteType;
import org.learn.reputation.rules.KarmaCalculator;
import org.learn.reputation.rules.ReceivedVoteEvent;

public class KarmaCalculatorTest extends TestCase {
    private KarmaCalculator karmaCalculator = new KarmaCalculator();
    private User questionAuthor = user("chico", "chico@brutal", 1l);
    private User answerAuthor = user("answerauthor", "answer@brutal", 2l);
    private Question question = new QuestionBuilder().withAuthor(questionAuthor).build();
    private Answer answer = answer("answer description", question, answerAuthor);

    @Test
    public void should_calculate_karma_for_votes() {
        assertEquals(MY_QUESTION_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, question, null, true).reputationEvent()));
        assertEquals(MY_QUESTION_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, answer, null, true).reputationEvent()));

        assertEquals(MY_ANSWER_VOTED_UP, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.UP, answer, null, true).reputationEvent()));
        assertEquals(MY_ANSWER_VOTED_DOWN, karmaCalculator.karmaFor(new ReceivedVoteEvent(VoteType.DOWN, question, null, true).reputationEvent()));
    }

}
