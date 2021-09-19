package com.teamerror.capstoneprojectdbs.controllers;

import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.models.ClientWiseStats;
import com.teamerror.capstoneprojectdbs.models.CustodianWiseStats;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.services.DashboardService;
import com.teamerror.capstoneprojectdbs.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    DashboardService dashboardService;

    @PostMapping("transaction")
    public ResponseEntity<OrderBook> transaction(@RequestBody OrderBookRequest orderBookRequest){
        OrderBook orderBook = transactionService.processTransaction(orderBookRequest);
        return new ResponseEntity<>(orderBook, HttpStatus.OK);
    }

    @GetMapping("custodianWiseStats")
    public ResponseEntity<List<CustodianWiseStats>> getCustodianWiseStats(){
        List<CustodianWiseStats> stats =  dashboardService.getCustodianWiseStats();
        return new ResponseEntity<>(stats,HttpStatus.OK);
    }

    @GetMapping("clientWiseStats")
    public ResponseEntity<List<ClientWiseStats>> getClientWiseStats(){
        List<ClientWiseStats> stats = dashboardService.getClientWiseStats();
        return new ResponseEntity<>(stats,HttpStatus.OK);
    }

}
