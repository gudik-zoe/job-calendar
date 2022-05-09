package com.work.calendar.service;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.work.calendar.dto.ClientDTO;
import com.work.calendar.entity.Client;
import com.work.calendar.repository.ClientRepository;

@Service
public class ClientService extends CrudService<Client> {

	private Logger log = LoggerFactory.getLogger(ClientService.class);
	@Autowired
	private ClientRepository clientRepository;

	public Client addClient(ClientDTO clientDTO) {
		Client client = new Client(clientDTO.getFullName(), new Date(), clientDTO.getColor());
		clientRepository.save(client);
		return client;

	}

	public void deleteClient(Long id) {
		try {
			clientRepository.deleteById(id);
		} catch (Exception e) {
			log.error("error deleting a client " + e.getMessage());
			throw new Error("couldn't delet client");
		}
	}

	public List<Client> getClients() {
		return clientRepository.findAll();
	}

	public Client getClientById(Long id) throws AccountNotFoundException {
		if (clientRepository.findById(id).get() != null) {
			return clientRepository.findById(id).get();
		} else {
			throw new AccountNotFoundException("no client account with the id " + id);
		}
	}

	@Override
	public JpaRepository<Client, Long> getRepository() {
		return clientRepository;
	}

	@Override
	public boolean validateEntity(Client entity) {
		return !entity.getFullName().trim().isEmpty() ? true : false;
	}

	public Client updateClient(Client client) {
		if(validateEntity(client)) {
			return clientRepository.save(client);
		}
		log.info("error validating client");
		return null;
		
	}
}
