package com.work.calendar.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "user_id")
	private Long id;

	private String fullName;
	private String email;
	private String passsword;

//	@JoinColumn(name = "id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Client> clients;

//	@JoinColumn(name = "id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "user")
	private List<Job> jobs;

//	@JoinColumn(name = "id", referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
	@OneToMany(fetch = FetchType.LAZY , mappedBy = "user")
	private List<Business> businessList;

	public User() {

	}

	public User(String fullName, String email, String passsword) {
		this.fullName = fullName;
		this.email = email;
		this.passsword = passsword;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Business> getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List<Business> businessList) {
		this.businessList = businessList;
	}

}
