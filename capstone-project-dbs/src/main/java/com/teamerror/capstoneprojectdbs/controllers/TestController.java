package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.repositories.StocksRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TestController {
        StocksRepository stocksRepository;

        @GetMapping(value="stocks/{id}")
        public ResponseEntity<List<Stocks>> getStocksByClientId(@PathVariable("id") Client id) {
                List<Stocks> stocks = stocksRepository.findAllByClient(id);
                return new ResponseEntity<List<Stocks>>(stocks, HttpStatus.OK);
        }

        @GetMapping(value="stocks/{clientId,instrumentId}")
        public ResponseEntity<Optional<Stocks>> getStocksByClientAndInstrument(@PathVariable("clientId") Client clientId,
                                                        @PathVariable("instrumentId") Instrument instrumentId) {
                Optional<Stocks> stocks = stocksRepository.findByClientAndInstrument(clientId, instrumentId);
                return new ResponseEntity<Optional<Stocks>>(stocks, HttpStatus.OK);
        }
}
