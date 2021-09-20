package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    ClientRepository clientRepository;

    public Client findByClientId(String clientId) {
        return clientRepository.findById(clientId).get();
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }
}
