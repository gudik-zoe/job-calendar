package com.work.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.work.calendar.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long>  {
	
	@Query("SELECT c FROM Client c where c.userId=:userId")
	public List<Client> getUserClients(@Param(value = "userId") Long userId);
	

}
