package com.work.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.work.calendar.entity.Business;

public interface BusinessRepository extends JpaRepository<Business, Long>, JpaSpecificationExecutor<Business> {

	@Query("SELECT b FROM Business b where CAST(b.date AS date)= CAST(:date AS date)")
	List<Business> getBusinessOnDate(@Param(value = "date") Date date);

}
