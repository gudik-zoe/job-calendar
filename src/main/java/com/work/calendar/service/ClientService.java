package com.work.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.entity.Client;
import com.work.calendar.repository.ClientRepository;
@Service
public class ClientService extends CrudService<Client> {
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public JpaRepository<Client, Long> getRepository() {
		return clientRepository;
	}

	@Override
	public boolean validateEntity(Client entity) {
		return !entity.getFullName().isBlank() ? true : false;

	}

}
