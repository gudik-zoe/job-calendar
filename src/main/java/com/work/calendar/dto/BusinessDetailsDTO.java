package com.work.calendar.dto;

import java.util.Date;

public class BusinessDetailsDTO {

	private String jobDescription;
	private int jobDuration;
	private Date date;

	public BusinessDetailsDTO(String jobDescription, int jobDuration, Date date) {
		this.jobDescription = jobDescription;
		this.jobDuration = jobDuration;
		this.date = date;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public int getJobDuration() {
		return jobDuration;
	}

	public void setJobDuration(int jobDuration) {
		this.jobDuration = jobDuration;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
