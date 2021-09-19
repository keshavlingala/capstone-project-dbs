package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.repositories.StocksRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
        StocksRepository stocksRepository;

        @GetMapping(value="stocks/{id}")
        public ResponseEntity<List<Stocks>> getCustomer(@PathVariable("id") Client id) {
                List<Stocks> stocks = stocksRepository.findAllByClient(id);
                return new ResponseEntity<List<Stocks>>(stocks, HttpStatus.OK);
        }
}
