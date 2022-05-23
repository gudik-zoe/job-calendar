package com.work.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.work.calendar.entity.Client;
import com.work.calendar.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>  {

	
	
	@Query("SELECT j FROM Job j where j.userId=:userId")
	public List<Job> getUserJobs(@Param(value = "userId") Long userId);

}
