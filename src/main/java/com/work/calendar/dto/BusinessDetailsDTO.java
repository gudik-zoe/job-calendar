package com.work.calendar.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessDetailsDTO {

	private String jobDescription;

	private List<JobsDetail> jobDetails = new ArrayList<>();

	public BusinessDetailsDTO(String jobDescription, List<JobsDetail> jobDetails) {
		this.jobDescription = jobDescription;
		this.jobDetails = jobDetails;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public List<JobsDetail> getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(List<JobsDetail> jobDetails) {
		this.jobDetails = jobDetails;
	}

}
