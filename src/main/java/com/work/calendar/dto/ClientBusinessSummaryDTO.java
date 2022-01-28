package com.work.calendar.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientBusinessSummaryDTO {
	private Long clientId;

	private String clientName;

	List<BusinessDetailsDTO> businessDetails = new ArrayList<>();

	private double totalHoursForClient;

	public ClientBusinessSummaryDTO() {

	}

	public ClientBusinessSummaryDTO(Long clientId, String clientName, List<BusinessDetailsDTO> businessDetails,
			double totalHoursForClient) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.businessDetails = businessDetails;
		this.totalHoursForClient = totalHoursForClient;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<BusinessDetailsDTO> getBusinessDetails() {
		return businessDetails;
	}

	public void setBusinessDetails(List<BusinessDetailsDTO> businessDetails) {
		this.businessDetails = businessDetails;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public double getTotalHoursForClient() {
		return totalHoursForClient;
	}

	public void setTotalHoursForClient(double totalHoursForClient) {
		this.totalHoursForClient = totalHoursForClient;
	}

}
