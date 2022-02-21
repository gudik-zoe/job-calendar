package com.work.calendar.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientBusinessSummaryDTO {
	private Long clientId;

	private String clientName;

	Map<String, List<JobsDetail>> jobs = new HashMap<>();

	private double totalHoursForClient;

	public ClientBusinessSummaryDTO() {

	}

	public ClientBusinessSummaryDTO(Long clientId, String clientName, Map<String, List<JobsDetail>> jobs,
			double totalHoursForClient) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.jobs = jobs;
		this.totalHoursForClient = totalHoursForClient;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Map<String, List<JobsDetail>> getJobs() {
		return jobs;
	}

	public void setJobs(Map<String, List<JobsDetail>> jobs) {
		this.jobs = jobs;
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
