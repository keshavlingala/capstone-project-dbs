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
    StockService stockService;

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


    private ResponseEntity<Object> sellOrder(OrderBookRequest orderReqOfSeller) {

        Instrument instrument = instrumentRepository.findById(orderReqOfSeller.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument Not Found"));
        Client seller = clientRepository.findById(orderReqOfSeller.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));


        List<OrderBook> buyOrdersWithSameInstrument = orderBookRepository.findAllByOrderDirection(OrderDirection.BUY).stream().filter(record -> {
            return record.getInstrument().getInstrumentId().equals(instrument.getInstrumentId());
        }).collect(Collectors.toList());

        //if there are no buyers with same instrument save it in order book table.
        if (buyOrdersWithSameInstrument.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(saveOrderInOrderBook(orderReqOfSeller, instrument, seller));
        }

        //perfectBuyers = buyers with same instrument and same price expectations
        List<OrderBook> perfectBuyers = buyOrdersWithSameInstrument
                .stream()
                .filter(item -> item.getPrice().equals(orderReqOfSeller.getPrice())).collect(Collectors.toList());

        if (!perfectBuyers.isEmpty()) {
            // One Buyer Case
            OrderBook minBuyerOrderReq = perfectBuyers.stream().min(Comparator.comparing(OrderBook::getTimeStamp)).get();
            if (minBuyerOrderReq.getQuantity().equals(orderReqOfSeller.getQuantity())) {

                // Perfect Buy Sell Trade
                Client buyer = minBuyerOrderReq.getClient();

                // Buyer and Seller Transaction limit update
                buyer.setTransactionLimit(buyer.getTransactionLimit() - minBuyerOrderReq.getQuantity() * minBuyerOrderReq.getPrice());
                seller.setTransactionLimit(seller.getTransactionLimit() - minBuyerOrderReq.getQuantity() * minBuyerOrderReq.getPrice());

                // TODO Unfinished

                //update the status of the buyer order request to "completed"
                minBuyerOrderReq.setOrderStatus(OrderStatus.COMPLETED);
                orderBookRepository.save(minBuyerOrderReq);

                // create/add the stocks to the buyer and remove the same stocks from the seller
                stockService.saveStock(buyer, minBuyerOrderReq.getInstrument(), minBuyerOrderReq.getQuantity());
                stockService.saveStock(seller, minBuyerOrderReq.getInstrument(), -1 * minBuyerOrderReq.getQuantity());

            } else {
                // Match multiple Buyers to single Seller

                //sort perfectBuyers based on timestamp and select the buyers which have less quantity than the current seller
                List<OrderBook> buyers = perfectBuyers.stream().sorted(Comparator.comparing(OrderBook::getTimeStamp))
                        .filter(order -> order.getQuantity() < orderReqOfSeller.getQuantity()).collect(Collectors.toList());

                //loop through buyers and deduct the instrument quantity from both buyers and sellers
                int sellerQuantity = orderReqOfSeller.getQuantity();
                for (OrderBook orderReqOfBuyer : buyers) {
                    if (sellerQuantity < orderReqOfBuyer.getQuantity()) {
                        continue;
                    }

                    Client buyer = orderReqOfBuyer.getClient();
                    buyer.setTransactionLimit(buyer.getTransactionLimit() - orderReqOfBuyer.getQuantity() * orderReqOfBuyer.getPrice());
                    seller.setTransactionLimit(seller.getTransactionLimit() - orderReqOfBuyer.getQuantity() * orderReqOfBuyer.getPrice());

                    orderBookRepository.delete(orderReqOfBuyer);
                    stockService.saveStock(buyer, orderReqOfBuyer.getInstrument(), orderReqOfBuyer.getQuantity());
                    stockService.saveStock(seller, orderReqOfBuyer.getInstrument(), -1 * orderReqOfBuyer.getQuantity());

                    sellerQuantity = sellerQuantity - orderReqOfBuyer.getQuantity();
                }

            }
        }

        buyOrdersWithSameInstrument.sort(Comparator.comparing(OrderBook::getTimeStamp));
        return null;
    }

    private void makeTransaction(Client buyer, Client seller, OrderBook orderReq) {

    }

    private OrderBook saveOrderInOrderBook(OrderBookRequest orderBookRequest, Instrument instrument, Client client) {
        OrderBook orderBook = new OrderBook();
        orderBook.setOrderId(UUID.randomUUID());
        orderBook.setOrderStatus(OrderStatus.PROCESSING);
        orderBook.setOrderDirection(orderBookRequest.getOrderDirection());
        orderBook.setQuantity(orderBookRequest.getQuantity());
        orderBook.setLimitOrder(orderBookRequest.getLimitOrder());
        orderBook.setInstrument(instrument);
        orderBook.setClient(client);
        orderBook.setTimeStamp(new Date());
        return orderBookRepository.save(orderBook);
    }


}
