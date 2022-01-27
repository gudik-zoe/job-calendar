package com.work.calendar.service;

import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.Client;
import com.work.calendar.entity.Job;
import com.work.calendar.repository.JobRepository;

@Service
public class JobService  extends CrudService<Job>{

	private Logger log = LoggerFactory.getLogger(JobService.class);
	@Autowired
	private JobRepository jobRepository;

	public boolean validateEntity(Job entity) {
		return !entity.getDescription().trim().isEmpty() ? true : false;

	}

	public void deleteJob(Long id) {
		Optional<Job> jt = jobRepository.findById(id);
		if (jt.isPresent()) {
			jobRepository.delete(jt.get());
		} else {
			log.error("job type doesn't exist");
		}

	}

	public Job addJobType(Job jobType) {
		if (validateEntity(jobType)) {
			return jobRepository.save(jobType);
		} else {
			log.info("job type is not valid");
			return null;
		}
	}

	public List<Job> getJobTypes() {
		return jobRepository.findAll();
	}

	@Override
	public JpaRepository<Job, Long> getRepository() {
		return jobRepository;
	}
	
	public Job getJobById(Long id) throws AccountNotFoundException {
		if(jobRepository.findById(id).get() != null) {
			return jobRepository.findById(id).get();
		}else {
			throw new AccountNotFoundException("no client account with the id " + id);
		}
	}


}
