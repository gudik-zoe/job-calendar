package com.work.calendar.dto;

import java.util.Date;

public class ClientJobFilterDTO {

	private Long clientId;

	private Date startDate;

	private Date endDate;

	private Date date;

	public ClientJobFilterDTO(Long clientId, Date startDate, Date endDate, Date date) {
		this.clientId = clientId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}
