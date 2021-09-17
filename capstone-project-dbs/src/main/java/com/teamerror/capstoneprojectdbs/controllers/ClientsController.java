package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.exceptions.ResourceNotFoundException;
import com.teamerror.capstoneprojectdbs.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

@RestController
@CrossOrigin(origins = "*")
public class ClientsController {
    ClientService clientService;

    @GetMapping(value="client/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") String id) {
        Client client = clientService.findByClientId(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping(value="/test")
    public ResponseEntity<Object> testMethod(){
        throw new ResourceNotFoundException("something went wrong");
    }
}
