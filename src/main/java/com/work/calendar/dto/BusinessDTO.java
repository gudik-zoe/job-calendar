package com.work.calendar.dto;

import java.util.Date;

public class BusinessDTO {

	private String note;
	private Long businessId;
	private Long clientId;
	private Long jobId;
	private String clientFullName;
	private String jobDescription;
	private String position;
	private Date startTime;
	private Date endTime;
	private Date date;
	private Long userId;

	public BusinessDTO() {

	}

	public BusinessDTO(Long businessId, String note, Long clientId, Long jobId, String clientFullName,
			String jobDescription, String position, Date startTime, Date endTime, Date date) {
		this.businessId = businessId;
		this.note = note;
		this.clientId = clientId;
		this.jobId = jobId;
		this.clientFullName = clientFullName;
		this.jobDescription = jobDescription;
		this.position = position;
		this.startTime = startTime;
		this.endTime = endTime;
		this.date = date;
	}

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getClientFullName() {
		return clientFullName;
	}

	public void setClientFullName(String clientFullName) {
		this.clientFullName = clientFullName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "BusinessDTO [note=" + note + ", businessId=" + businessId + ", clientId=" + clientId + ", jobId="
				+ jobId + ", clientFullName=" + clientFullName + ", jobDescription=" + jobDescription + ", position="
				+ position + ", startTime=" + startTime + ", endTime=" + endTime + ", date=" + date + "]";
	}

}
