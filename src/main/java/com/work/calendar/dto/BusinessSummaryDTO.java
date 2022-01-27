package com.work.calendar.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessSummaryDTO {

	private String clientName;

	List<BusinessDetailsDTO> businessDetails = new ArrayList<>();

	private int totalHrs;

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

	public int getTotalHrs() {
		return totalHrs;
	}

	public void setTotalHrs(int totalHrs) {
		this.totalHrs = totalHrs;
	}

}
