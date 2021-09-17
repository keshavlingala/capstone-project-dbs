package com.teamerror.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientIdservice {
	
	@Autowired
	ClientRepository clientRepository;
	
	public Client findByClientid()
	{
		Optional<Client> clientobj = clientRepository.findById(client_id);
		
		if(clientobj.isPresent()) {
			return clientobj.get();
		}
		
		else {
			return null;
		}
	}
}
