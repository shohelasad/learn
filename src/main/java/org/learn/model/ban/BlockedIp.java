package org.learn.model.ban;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.learn.model.User;
import org.learn.providers.SessionFactoryCreator;

import javax.persistence.*;

@Cacheable
@Entity
public class BlockedIp {

	@Id
	@GeneratedValue
	private Long id;

	private String ip;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User author;

	/**
	 * @deprecated hibernate only
	 */
	BlockedIp() {
	}

	public BlockedIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public Long getId() {
		return id;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}
}
