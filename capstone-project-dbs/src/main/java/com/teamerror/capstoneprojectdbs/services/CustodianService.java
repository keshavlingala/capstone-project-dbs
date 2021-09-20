package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Custodian;
import com.teamerror.capstoneprojectdbs.repositories.CustodianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustodianService {

    CustodianRepository custodianRepository;

    public List<Custodian> findAll(){
        return custodianRepository.findAll();
    }
}
