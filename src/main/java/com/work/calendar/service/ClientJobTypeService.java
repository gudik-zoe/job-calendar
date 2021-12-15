package com.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.ClientJobType;
import com.work.calendar.repository.ClientJobTypeRepository;
@Service
public class ClientJobTypeService extends CrudService<ClientJobType> {
	
	
	@Autowired
	private ClientJobTypeRepository ClientJobTypeRepository;

	@Override
	public JpaRepository<ClientJobType, Long> getRepository() {
		return ClientJobTypeRepository;
	}

	@Override
	public boolean validateEntity(ClientJobType entity) {
		return true;
	}

}
