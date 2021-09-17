package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.repositories.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstrumentService {

    @Autowired
    InstrumentRepository instrumentRepository;

    public Instrument findbyInstrumentId(String instrumentId)
    {
        Optional<Instrument> instrumentobj = instrumentRepository.findById(instrumentId);

        if(instrumentobj.isPresent()) {
            return instrumentobj.get();
        }

        else {
            return null;
        }
    }
}
