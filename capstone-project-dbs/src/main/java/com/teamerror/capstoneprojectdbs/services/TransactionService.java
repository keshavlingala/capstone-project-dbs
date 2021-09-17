package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    OrderBookRepository orderBookRepository;

    ClientService clientService;

    public OrderBook buyFunction(String instrumentId, Integer quantity, Double price, String clientId) {
        List<OrderBook> matches = orderBookRepository.findById(String instrumentId);
        OrderBook o1;

        for (OrderBook name : matches) {
            if (name.getOrderDirection().equals("sell")) {
                if (name.getPrice().equals(price)) {
                    Integer buyQuantity;
                    Integer sellQuantity = name.getQuantity();
                    Client clientobj = clientService.findByClientid(clientId);
                    Client sellerId = name.getClientId();
                    Client clientobj1 = clientService.findByClientid(sellerId.getClientId());

                    if (quantity > sellQuantity) {
                        buyQuantity = quantity - sellQuantity;
                        name.setOrderDirection("Done");
                        name.setOrderStatus("executed");
                        name.setQuantity(0);
                        clientobj.setTransactionLimit(clientobj.getTransactionLimit() - name.getPrice());
                        clientobj1.setTransactionLimit(clientobj1.getTransactionLimit() + name.getPrice());
                        name.setPrice(0);


                    } else {
                        sellQuantity = sellQuantity - quantity;
                        name.setQuantity(sellQuantity);
                        Double updateprice = name.getPrice() - price;
                        name.setOrderStatus("partially executed");
                        name.setQuantity(sellQuantity);
                        clientobj.setTransactionLimit(clientobj.getTransactionLimit() - price);
                        clientobj1.setTransactionLimit(clientobj1.getTransactionLimit() + price);
                        name.setPrice(updateprice);

                    }
                }
            }
        }
        return o1;

    }

    public ResponseEntity<Object> processTransaction(OrderBookRequest orderBookRequest) {
        return null;
    }
}
