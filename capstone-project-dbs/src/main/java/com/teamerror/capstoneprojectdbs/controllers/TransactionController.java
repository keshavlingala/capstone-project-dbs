package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransactionController {
    TransactionService transactionService;
    @PostMapping("transaction")
    public ResponseEntity<Object> transaction(OrderBookRequest orderBookRequest){
        return transactionService.processTransaction(orderBookRequest);
    }
}
