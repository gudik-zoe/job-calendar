package com.work.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.work.calendar.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long>  {

}
