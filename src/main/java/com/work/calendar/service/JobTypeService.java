package com.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.JobType;
import com.work.calendar.repository.JobTypeRepository;

@Service
public class JobTypeService extends CrudService<JobType> {

	private Logger log = LoggerFactory.getLogger(JobTypeService.class);
	@Autowired
	private JobTypeRepository jobTypeRepository;

	@Override
	public JpaRepository<JobType, Long> getRepository() {
		log.info("passing from jobType service");
		return jobTypeRepository;
	}

	@Override
	public boolean validateEntity(JobType entity) {
		return !entity.getDescription().trim().isEmpty() ? true : false;

	}

}
