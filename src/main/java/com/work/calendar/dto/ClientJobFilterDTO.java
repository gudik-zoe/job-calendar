package com.work.calendar.dto;

import java.util.Date;

public class ClientJobFilterDTO {

	private Long clientId;

	private Date startDate;

	private Date endDate;

	public ClientJobFilterDTO(Long clientId, Date startDate, Date endDate) {
		this.clientId = clientId;
		this.startDate = startDate;
		this.endDate = endDate;
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
