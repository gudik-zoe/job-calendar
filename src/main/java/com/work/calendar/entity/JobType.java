package com.work.calendar.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "job_type")
public class JobType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_type_id")
	private Long id;

	private String description;

	private Date timeStamp;

	@OneToMany(mappedBy = "jobtype")
	List<ClientJobType> clientJobTypes;

	public JobType(String description, Date timeStamp, List<ClientJobType> clientJobTypes) {
		this.description = description;
		this.timeStamp = timeStamp;
		this.clientJobTypes = clientJobTypes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}


}
