package com.appsdeveloperblog.app.ws.shared.dto;

import java.io.Serializable;
import java.util.List;

import com.appsdeveloperblog.app.ws.views.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

public class UserDto implements Serializable {
	
	private static final long serialVersionUID = 6835192601898364280L;
	
	@JsonView(View.DetailView.class)
	private long id;
	
	@JsonView({View.OveralView.class}) // @JsonView({View.OveralView.class, View.ProductView.class})
	private String userId;
	
	@JsonView(View.DetailView.class)
	private String firstName;
	
	@JsonView(View.DetailView.class)
	private String lastName;
	
	@JsonView(View.DetailView.class)
	private String email;
	
//	@JsonIgnore
	@JsonView(View.DetailView.class)
	private String password;
	
	@JsonView(View.DetailView.class)
//	@JsonIgnore
	private String encryptedPassword;
	
	
	@JsonView(View.DetailView.class)
	@JsonIgnore // 
	private String emailVerificationToken;
	
	@JsonIgnore // no use, without using view, by default it gets ignored
	private Boolean emailVerificationStatus = false;
	
	@JsonView(View.DetailView.class)
	private List<AddressDTO> addresses;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}
	
}
