package com.work.calendar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.controller.ClientController;
import com.work.calendar.entity.Client;
import com.work.calendar.repository.ClientRepository;

@Service
public class ClientService extends CrudService<Client> {

	private Logger log = LoggerFactory.getLogger(ClientService.class);
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public JpaRepository<Client, Long> getRepository() {
		log.info("passing from client service");
		return clientRepository;
	}

	@Override
	public boolean validateEntity(Client entity) {
		return !entity.getFullName().isBlank() ? true : false;

	}

}
