package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransactionController {
    TransactionService transactionService;
    @PostMapping("transaction")
    public ResponseEntity<OrderBook> transaction(OrderBookRequest orderBookRequest){
        OrderBook orderBook = transactionService.processTransaction(orderBookRequest);
        return new ResponseEntity<>(orderBook, HttpStatus.OK);
    }
}
