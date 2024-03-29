package org.learn.validators;

import static org.learn.model.Comment.COMMENT_MIN_LENGTH;
import static org.learn.model.Comment.ERROR_LENGTH;
import static org.learn.model.Comment.ERROR_NOT_EMPTY;

import javax.inject.Inject;

import org.learn.controllers.BrutalValidator;
import org.learn.factory.MessageFactory;
import org.learn.model.Comment;

import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.validator.Validator;

public class CommentValidator {

	@Inject private Validator validator;
	@Inject private MessageFactory factory;
	@Inject private BrutalValidator brutalValidator;

	public boolean validate(Comment comment){
		brutalValidator.validate(comment);
		return !validator.hasErrors();
	}
	
	public boolean validate(String comment){
		if(comment == null || comment.isEmpty()){
			validator.add(factory.build("error", ERROR_NOT_EMPTY));
			return false;
		}
		if(comment.length() < COMMENT_MIN_LENGTH){
			validator.add(factory.build("error", ERROR_LENGTH));
		}
		return !validator.hasErrors();
	}

	public <T> T onErrorRedirectTo(T controller){
		return validator.onErrorRedirectTo(controller);
	}
	
	public <T> T onErrorRedirectTo(Class<? extends T> controller){
		return validator.onErrorRedirectTo(controller);
	}

	public <T extends View> T onErrorUse(Class<T> view) {
		return validator.onErrorUse(view);
	}

}
