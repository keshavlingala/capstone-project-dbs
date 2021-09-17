package com.teamerror.capstoneprojectdbs.services;

import java.util.Optional;
import java.util.UUID;

import com.teamerror.capstoneprojectdbs.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamerror.capstoneprojectdbs.entities.Client;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository clientRepository;
	
	public Client findByClientid(String clientId)
	{
		Optional<Client> clientobj = clientRepository.findById(clientId);

		if(clientobj.isPresent()) {
			return clientobj.get();
		}

		else {
			return null;
		}
	}
}
