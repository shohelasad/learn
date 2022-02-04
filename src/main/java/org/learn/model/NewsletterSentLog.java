package org.learn.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.learn.providers.SessionFactoryCreator;

@Entity
public class NewsletterSentLog {
	@Id
	@GeneratedValue
	private Long id;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();
}
