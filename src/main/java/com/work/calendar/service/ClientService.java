package com.work.calendar.service;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.work.calendar.aspect.UserHelper;
import com.work.calendar.dto.ClientDTO;
import com.work.calendar.entity.Client;
import com.work.calendar.exceptions.BadRequestException;
import com.work.calendar.exceptions.MyPlatformException;
import com.work.calendar.repository.ClientRepository;

@Service
public class ClientService extends CrudService<Client> {

	private Logger log = LoggerFactory.getLogger(ClientService.class);
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private UserService userService;
	
	public Client addClient(UserHelper userHelper , ClientDTO clientDTO) throws NotFound {
		Client client = new Client(clientDTO.getFullName(), new Date(), clientDTO.getColor());
		client.setUserId(userHelper.getId());
		clientRepository.save(client);
		return client;

	}

	public void deleteClient(UserHelper userHelper , Long id) {
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

	public Client getClientById(UserHelper userHelper , Long id) throws AccountNotFoundException {
		if (clientRepository.findById(id).isPresent()) {
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

	public Client updateClient(UserHelper userHelper ,Client client) {
		if(validateEntity(client)) {
			return clientRepository.save(client);
		}
		log.info("error validating client");
		return null;
		
	}

	public List<Client> getClientsByUser(UserHelper userHelper) {
		return clientRepository.getUserClients(userHelper.getId());
	}

	public Client getClientByIdAndCheckAvailability(UserHelper userHelper, Long clientId) throws AccountNotFoundException {
		Client client = getClientById(userHelper ,clientId);
		if(client.getUserId() !=userHelper.getId() ) {
			throw new MyPlatformException("UNAUTHORIZED , client does not belong to user", HttpStatus.UNAUTHORIZED.value());
		}
		return client;
	}
}
