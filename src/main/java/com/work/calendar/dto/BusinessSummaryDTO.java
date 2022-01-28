package com.work.calendar.dto;

import java.util.List;

public class BusinessSummaryDTO {

	private double totalHours;

	private List<ClientBusinessSummaryDTO> clientBusinessSummaryDTO;

	public BusinessSummaryDTO() {

	}

	public BusinessSummaryDTO(double totalHours, List<ClientBusinessSummaryDTO> clientBusinessSummaryDTO) {
		this.totalHours = totalHours;
		this.clientBusinessSummaryDTO = clientBusinessSummaryDTO;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public List<ClientBusinessSummaryDTO> getClientBusinessSummaryDTO() {
		return clientBusinessSummaryDTO;
	}

	public void setClientBusinessSummaryDTO(List<ClientBusinessSummaryDTO> clientBusinessSummaryDTO) {
		this.clientBusinessSummaryDTO = clientBusinessSummaryDTO;
	}

}
