package com.work.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.work.calendar.entity.JobType;

public interface JobTypeRepository extends JpaRepository<JobType, Long>  {

}
