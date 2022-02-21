package com.work.calendar.dto;

import java.util.Date;

public class JobsDetail {
	private double jobDuration;
	private Date date;

	public JobsDetail(double jobDuration, Date date) {
		this.jobDuration = jobDuration;
		this.date = date;
	}

	public double getJobDuration() {
		return jobDuration;
	}

	public void setJobDuration(double jobDuration) {
		this.jobDuration = jobDuration;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
