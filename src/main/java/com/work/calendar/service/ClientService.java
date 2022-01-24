package com.work.calendar.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.controller.ClientController;
import com.work.calendar.entity.Client;
import com.work.calendar.repository.ClientRepository;

@Service
public class ClientService {

	private Logger log = LoggerFactory.getLogger(ClientService.class);
	@Autowired
	private ClientRepository clientRepository;

	public boolean validateEntity(Client entity) {
		return (entity.getFullName().length() > 3) ? true : false;

	}

	public Client addClient(Client client) {
		if (validateEntity(client)) {
			return clientRepository.save(client);
		}
		log.error("client not valid");
		return null;
	}

	public void deleteClient(Long id) {
		clientRepository.deleteById(id);

	}

	public List<Client> getClients() {
		return clientRepository.findAll();
	}

}
