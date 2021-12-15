package com.work.calendar.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.JobType;
import com.work.calendar.repository.JobTypeRepository;
@Service
public class JobTypeService extends CrudService<JobType> {

	private JobTypeRepository jobTypeRepository;

	@Override
	public JpaRepository<JobType, Long> getRepository() {
		return jobTypeRepository;
	}

	@Override
	public boolean validateEntity(JobType entity) {
		return !entity.getDescription().trim().isEmpty() ? true : false;

	}

}
