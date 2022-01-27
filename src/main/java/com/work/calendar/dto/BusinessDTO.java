package com.work.calendar.dto;

import java.util.Date;

public class BusinessDTO {

	private String note;

	private Long clientId;
	private Long jobId;
	private String position;
	private int timeSpentInHrs;
	private Date date;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getTimeSpentInHrs() {
		return timeSpentInHrs;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTimeSpentInHrs(int timeSpentInHrs) {
		this.timeSpentInHrs = timeSpentInHrs;
	}

}
