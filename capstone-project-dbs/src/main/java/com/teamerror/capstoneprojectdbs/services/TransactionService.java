package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.models.CustomResponse;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    ClientRepository clientRepository;
    CustodianRepository custodianRepository;
    InstrumentRepository instrumentRepository;
    StocksRepository stocksRepository;
    OrderBookRepository orderBookRepository;

    private ResponseEntity<Object> buyOrder(OrderBookRequest orderBookRequest) {
        return null;
    }

    public ResponseEntity<Object> processTransaction(OrderBookRequest orderBookRequest) {
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.BUY)) {
            return this.buyOrder(orderBookRequest);
        }
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.SELL)) {
            return this.sellOrder(orderBookRequest);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("Invalid Request", "Invalid Order Direction"));
    }


    private ResponseEntity<Object> sellOrder(OrderBookRequest orderBookRequest) {
        return null;
    }

}
