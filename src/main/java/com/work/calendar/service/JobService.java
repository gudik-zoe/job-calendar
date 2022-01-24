package com.work.calendar.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.Client;
import com.work.calendar.entity.Job;
import com.work.calendar.repository.JobRepository;

@Service
public class JobService {

	private Logger log = LoggerFactory.getLogger(JobService.class);
	@Autowired
	private JobRepository jobTypeRepository;

	public boolean validateEntity(Job entity) {
		return !entity.getDescription().trim().isEmpty() ? true : false;

	}

	public void deleteJob(Long id) {
		Optional<Job> jt = jobTypeRepository.findById(id);
		if (jt.isPresent()) {
			jobTypeRepository.delete(jt.get());
		} else {
			log.error("job type doesn't exist");
		}

	}

	public Job addJobType(Job jobType) {
		if (validateEntity(jobType)) {
			return jobTypeRepository.save(jobType);
		} else {
			log.info("job type is not valid");
			return null;
		}
	}

	public List<Job> getJobTypes() {
		return jobTypeRepository.findAll();
	}

}
