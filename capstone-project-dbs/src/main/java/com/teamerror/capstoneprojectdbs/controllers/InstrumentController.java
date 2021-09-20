package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.repositories.InstrumentRepository;
import com.teamerror.capstoneprojectdbs.services.InstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class InstrumentController {
    InstrumentService instrumentService;

    @GetMapping(value="instrument/{id}")
    public ResponseEntity<Instrument> getClient(@PathVariable("id") String id) {
        Instrument instrument = instrumentService.findByInstrumentId(id);
        return new ResponseEntity<>(instrument, HttpStatus.OK);
    }

    @GetMapping(value="instruments")
    public ResponseEntity<List<Instrument>> getAllInstruments(){
        return new ResponseEntity<>(instrumentService.findAll(),HttpStatus.OK);
    }

}
