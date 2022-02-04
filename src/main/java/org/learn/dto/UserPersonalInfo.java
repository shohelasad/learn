package org.learn.dto;

import static org.learn.validators.UserPersonalInfoValidator.ABOUT_LENGTH_MESSAGE;
import static org.learn.validators.UserPersonalInfoValidator.ABOUT_MAX_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.ABOUT_MIN_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.EMAIL_LENGTH_MESSAGE;
import static org.learn.validators.UserPersonalInfoValidator.EMAIL_MAX_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.EMAIL_MIN_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.EMAIL_NOT_VALID;
import static org.learn.validators.UserPersonalInfoValidator.LOCATION_LENGTH_MESSAGE;
import static org.learn.validators.UserPersonalInfoValidator.LOCATION_MAX_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.MARKED_ABOUT_MAX_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.NAME_LENGTH_MESSAGE;
import static org.learn.validators.UserPersonalInfoValidator.NAME_MAX_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.NAME_MIN_LENGTH;
import static org.learn.validators.UserPersonalInfoValidator.NAME_REQUIRED;
import static org.learn.validators.UserPersonalInfoValidator.WEBSITE_LENGTH_MESSAGE;
import static org.learn.validators.UserPersonalInfoValidator.WEBSITE_MAX_LENGHT;
import static org.learn.validators.UserPersonalInfoValidator.WEBSITE_MIN_LENGTH;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.learn.model.MarkedText;
import org.learn.model.SanitizedText;
import org.learn.model.User;

public class UserPersonalInfo {
	private final User user;
	
	@Length(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
	@NotEmpty(message = NAME_REQUIRED)
	private String name;
	
	@Length(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
	@Email(message = EMAIL_NOT_VALID)
	private String email;
	
	@Length(min = WEBSITE_MIN_LENGTH, max = WEBSITE_MAX_LENGHT, message = WEBSITE_LENGTH_MESSAGE)
	private String website;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String location;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String occupation;
	
	@Length(max = LOCATION_MAX_LENGTH, message = LOCATION_LENGTH_MESSAGE)
	private String organization;
	
	private DateTime birthDate;
	
	private String about;

	private String markedAbout;

	private boolean isSubscribed;
	
	private boolean receiveAllUpdates;


	public UserPersonalInfo(User user) {
		this.user = user;
	}

	public UserPersonalInfo withBirthDate(DateTime birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public UserPersonalInfo withLocation(SanitizedText location) {
		this.location = location.getText();
		return this;
	}
	
	public UserPersonalInfo withOccupation(SanitizedText occupation) {
		this.occupation = occupation.getText();
		return this;
	}
	
	public UserPersonalInfo withOrganization(SanitizedText organization) {
		this.organization = organization.getText();
		return this;
	}

	public UserPersonalInfo withWebsite(SanitizedText website) {
		this.website = website.getText();
		return this;
	}

	public UserPersonalInfo withEmail(String email) {
		this.email = email;
		return this;
	}

	public UserPersonalInfo withAbout(MarkedText about) {
		this.about = about.getPure();
		this.markedAbout = about.getMarked();
		return this;
	}

	public UserPersonalInfo withName(SanitizedText name){
		this.name = name.getText();
		return this;
	}
	
	public User getUser() {
		return this.user;
	}

	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getWebsite() {
		return this.website;
	}

	public String getLocation() {
		return this.location;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public String getOrganization() {
		return this.organization;
	}

	public DateTime getBirthDate() {
		return this.birthDate;
	}

	public String getAbout() {
		return this.about;
	}
	
	public String getMarkedAbout() {
		return this.markedAbout;
	}

	public UserPersonalInfo withIsSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
		return this;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public UserPersonalInfo withReceiveAllUpdates(boolean receiveAllUpdates) {
		this.receiveAllUpdates = receiveAllUpdates;
		return this;
	}

	public boolean getReceiveAllUpdates() {
		return receiveAllUpdates;
	}
}