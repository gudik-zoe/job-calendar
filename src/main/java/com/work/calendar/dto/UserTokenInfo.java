package com.work.calendar.dto;

public class UserTokenInfo {

	private Long id;
	private String fullName;

	public UserTokenInfo(Long id, String fullName) {
		this.id = id;
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
