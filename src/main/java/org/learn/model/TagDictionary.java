package org.learn.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.learn.providers.SessionFactoryCreator;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="cache")
@Entity
public class TagDictionary {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false)
	@NotEmpty
	private String bangla;
	
	private String english;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();
	
	/**
     * @deprecated hibernate eyes only
     */
    public TagDictionary() {
    }

	
	public TagDictionary(String bangla, String english) {
		this.bangla = bangla;
		this.english = english.toLowerCase();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBangla() {
		return bangla;
	}


	public void setBangla(String bangla) {
		this.bangla = bangla;
	}


	public String getEnglish() {
		return english;
	}


	public void setEnglish(String english) {
		this.english = english;
	}


	public DateTime getCreatedAt() {
		return createdAt;
	}	
}
