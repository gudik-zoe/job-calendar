package com.work.calendar.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ClientJobType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "job_type_id")
	private JobType jobtype;

	private Date date;

	private int hoursNumber;

	private String note;

	private String position;

	public ClientJobType() {

	}

	public ClientJobType(Client client, JobType jobtype, Date date, int hoursNumber, String position) {
		this.client = client;
		this.jobtype = jobtype;
		this.date = date;
		this.hoursNumber = hoursNumber;
		this.position = position;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public JobType getJobtype() {
		return jobtype;
	}

	public void setJobtype(JobType jobtype) {
		this.jobtype = jobtype;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getHoursNumber() {
		return hoursNumber;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setHoursNumber(int hoursNumber) {
		this.hoursNumber = hoursNumber;
	}

}
