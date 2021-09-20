package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Custodian;
import com.teamerror.capstoneprojectdbs.repositories.ClientRepository;
import com.teamerror.capstoneprojectdbs.repositories.CustodianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    ClientRepository clientRepository;
    CustodianRepository custodianRepository;

    public Client findByClientId(String clientId) {
        return clientRepository.findById(clientId).get();
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public List<Client> findAllByCustodian(String custodianId){
        Custodian custodian = custodianRepository.findById(custodianId).get();
        return clientRepository.findAllByCustodian(custodian);
    }
}
