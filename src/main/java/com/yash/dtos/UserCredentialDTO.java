package com.yash.dtos;

import java.util.Date;

public class UserCredentialDTO {
	private Long id;
	private String username;
	private String email;
	private String password;
	private Date registeredOn;
	private boolean isActive;

	public UserCredentialDTO() {
		super();
	}

	public UserCredentialDTO(Long id, String username, String email, String password, Date registeredOn,
			boolean isActive) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.registeredOn = registeredOn;
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"registeredOn\": \"%tc\", \"isActive:\": %b}", id, username, email, password, registeredOn, isActive);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public boolean isIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
