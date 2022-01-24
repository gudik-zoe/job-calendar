package com.work.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.work.calendar.entity.ClientJob;

public interface ClientJobTypeRepository extends JpaRepository<ClientJob, Long> {

}
