package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.exceptions.ResourceNotFoundException;
import com.teamerror.capstoneprojectdbs.models.CustomResponse;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import com.teamerror.capstoneprojectdbs.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Instrument instrument = instrumentRepository.findById(orderBookRequest.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument Not Found"));
        Client client = clientRepository.findById(orderBookRequest.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));

        List<OrderBook> buyOrdersWithSameInstrument = orderBookRepository.findAllByOrderDirection(OrderDirection.BUY).stream().filter(record -> {
            return record.getInstrumentId().getInstrumentId().equals(instrument.getInstrumentId());
        }).collect(Collectors.toList());
        if (buyOrdersWithSameInstrument.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(saveOrderInOrderBook(orderBookRequest, instrument, client));
        }
        List<OrderBook> perfectBuyers = buyOrdersWithSameInstrument
                .stream()
                .filter(item -> item.getPrice().equals(orderBookRequest.getPrice())).collect(Collectors.toList());
        if (!perfectBuyers.isEmpty()) {
            // One Buyer Case
            OrderBook minBuyer = perfectBuyers.stream().min(Comparator.comparing(OrderBook::getTimeStamp)).get();
            if (minBuyer.getQuantity().equals(orderBookRequest.getQuantity())) {
                // Perfect Buy Sell Trade
                Client buyer = minBuyer.getClientId();
                Stocks buyerStock = new Stocks();
                buyerStock.setStockId(UUID.randomUUID());
                buyerStock.setClientId(buyer);
                buyerStock.setInstrumentId(minBuyer.getInstrumentId());
                buyerStock.setQuantity(minBuyer.getQuantity());
                // Buyer and Seller Transaction limit update
                buyer.setTransactionLimit(buyer.getTransactionLimit() - minBuyer.getQuantity() * minBuyer.getPrice());
                client.setTransactionLimit(client.getTransactionLimit() - minBuyer.getQuantity() * minBuyer.getPrice());

                // TODO Unfinished

                orderBookRepository.delete(minBuyer);
                stocksRepository.save(buyerStock);


            } else {
                // TODO Match multiple Buyers to single Seller
                List<OrderBook> buyers = perfectBuyers.stream()
                        .filter(order -> order.getQuantity() < orderBookRequest.getQuantity()).collect(Collectors.toList());

            }
        }

        buyOrdersWithSameInstrument.sort(Comparator.comparing(OrderBook::getTimeStamp));
        return null;
    }

    private OrderBook saveOrderInOrderBook(OrderBookRequest orderBookRequest, Instrument instrument, Client client) {
        OrderBook orderBook = new OrderBook();
        orderBook.setOrderId(UUID.randomUUID());
        orderBook.setOrderStatus(OrderStatus.PROCESSING);
        orderBook.setOrderDirection(OrderDirection.SELL);
        orderBook.setQuantity(orderBookRequest.getQuantity());
        orderBook.setLimitOrder(orderBookRequest.getLimitOrder());
        orderBook.setInstrumentId(instrument);
        orderBook.setClientId(client);
        orderBook.setTimeStamp(new Date());
        return orderBookRepository.save(orderBook);
    }

}
