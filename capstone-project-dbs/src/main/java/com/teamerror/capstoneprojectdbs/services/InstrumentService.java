package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.repositories.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentService {

    @Autowired
    InstrumentRepository instrumentRepository;

    public Instrument findByInstrumentId(String instrumentId) {
        return instrumentRepository.findById(instrumentId).get();
    }
}
