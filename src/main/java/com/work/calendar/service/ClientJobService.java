package com.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.ClientJob;
import com.work.calendar.repository.ClientJobTypeRepository;
@Service
public class ClientJobService extends CrudService<ClientJob> {
	
	
	@Autowired
	private ClientJobTypeRepository ClientJobTypeRepository;

	@Override
	public JpaRepository<ClientJob, Long> getRepository() {
		return ClientJobTypeRepository;
	}

	@Override
	public boolean validateEntity(ClientJob entity) {
		return true;
	}

}
