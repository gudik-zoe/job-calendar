package com.work.calendar.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.work.calendar.entity.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> , JpaSpecificationExecutor<Business>{

	@Query("SELECT cb FROM ClientJob as cb WHERE cb.client.id =:id")
	public List<Business> getClientJobSummaryForClientId(@Param(value = "id") Long id);

	@Query("SELECT cb FROM ClientJob as cb WHERE cb.client.id =:id and cb.date between :startDate and :endDate")
	public List<Business> getClientSummaryOnSpecificPeriod(@Param(value = "id") Long id,
			@Param(value = "startDate") Date startDate, @Param(value = "endDate") Date endDate);

}
