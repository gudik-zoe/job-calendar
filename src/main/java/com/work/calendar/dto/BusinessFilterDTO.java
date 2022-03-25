package com.work.calendar.dto;

import java.util.Calendar;
import java.util.Date;

public class BusinessFilterDTO {

	private Long clientId;

	private Long jobId;

	private Date startDate;

	private Date endDate;

	private Calendar calendar;

	public BusinessFilterDTO(Long clientId, Long jobId, Date startDate, Date endDate, Calendar calendar) {
		this.clientId = clientId;
		this.jobId = jobId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.calendar = calendar;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

}
