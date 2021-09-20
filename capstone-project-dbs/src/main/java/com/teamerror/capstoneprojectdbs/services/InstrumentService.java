package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.repositories.InstrumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InstrumentService {

    InstrumentRepository instrumentRepository;

    public Instrument findByInstrumentId(String instrumentId) {
        return instrumentRepository.findById(instrumentId).get();
    }

    public List<Instrument> findAll(){
        return instrumentRepository.findAll();
    }
}
