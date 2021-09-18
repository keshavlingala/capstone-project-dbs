package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.exceptions.ResourceNotFoundException;
import com.teamerror.capstoneprojectdbs.exceptions.ValidationException;
import com.teamerror.capstoneprojectdbs.models.CustomResponse;
import com.teamerror.capstoneprojectdbs.models.OrderBookRequest;
import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import com.teamerror.capstoneprojectdbs.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public OrderBook processTransaction(OrderBookRequest orderBookRequest) {
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.BUY)) {
            return this.buyOrder(orderBookRequest);
        }
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.SELL)) {
            return this.sellOrder(orderBookRequest);
        }
        throw new ValidationException("Invalid request");
    }

    private OrderBook buyOrder(OrderBookRequest orderBookRequest) {
        return null;
    }

    private OrderBook sellOrder(OrderBookRequest orderReqOfSeller) {

        Instrument sellerInstrument = instrumentRepository.findById(orderReqOfSeller.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument Not Found"));
        Client seller = clientRepository.findById(orderReqOfSeller.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));

        //first create the OrderBook instance of the seller request(to be saved to the database later)
        OrderBook sellerOrderBookInstance = new OrderBook(UUID.randomUUID(),
                seller,
                sellerInstrument,
                orderReqOfSeller.getPrice(),
                orderReqOfSeller.getQuantity(),
                OrderStatus.PROCESSING,
                OrderDirection.SELL,
                false,
                new Date()
        );


        List<OrderBook> buyOrdersWithSameInstrument = orderBookRepository
                .findAllByOrderDirectionAndOrderStatus(OrderDirection.BUY, OrderStatus.PROCESSING).stream()
                .filter(record -> {
                    return record.getInstrument().getInstrumentId().equals(sellerInstrument.getInstrumentId());
                })
                .collect(Collectors.toList());

        //if there are no buyers with same instrument save it in order book table.
        if (buyOrdersWithSameInstrument.isEmpty()) {
            return orderBookRepository.save(sellerOrderBookInstance);
        }

        double sellerStocksWorth = orderReqOfSeller.getQuantity() * orderReqOfSeller.getPrice();

        //perfectBuyers = buyers with same instrument and same price expectations and has enough transaction limit to make the transaction
        List<OrderBook> perfectBuyers = buyOrdersWithSameInstrument
                .stream()
                .filter(orders -> orders.getClient().getTransactionLimit() >= sellerStocksWorth)
                .filter(item -> item.getPrice().equals(orderReqOfSeller.getPrice())).collect(Collectors.toList());

        if (perfectBuyers.isEmpty()) {
            return orderBookRepository.save(sellerOrderBookInstance);
        }

        // One Buyer Case (when there is a buy order 'b' where b.quantity >= sellerOrder.quantity)
        Optional<OrderBook> minBuyerOrderReqOpt = perfectBuyers.stream()
                .filter(order -> order.getQuantity() >= orderReqOfSeller.getQuantity())
                .min(Comparator.comparing(OrderBook::getTimeStamp));

        if (minBuyerOrderReqOpt.isPresent()) {
            OrderBook minBuyerOrderReq = minBuyerOrderReqOpt.get();
            // Perfect Buy Sell Trade
            Client buyer = minBuyerOrderReq.getClient();

            exchangeStocks(buyer, seller, sellerOrderBookInstance);

            //update the status of the buyer order request to "completed" if buyerQuantity==sellerQuantity
            if (minBuyerOrderReq.getQuantity().equals(orderReqOfSeller.getQuantity())) {
                minBuyerOrderReq.setOrderStatus(OrderStatus.COMPLETED);
            }
            sellerOrderBookInstance.setOrderStatus(OrderStatus.COMPLETED);

            orderBookRepository.save(minBuyerOrderReq);
            orderBookRepository.save(sellerOrderBookInstance);

        } else {
            // Match multiple Buyers to single Seller

            //sort perfectBuyers based on timestamp and select the buyers which have less quantity than the current seller
            List<OrderBook> buyers = perfectBuyers.stream()
                    .sorted(Comparator.comparing(OrderBook::getTimeStamp))
                    .filter(order -> order.getQuantity() < orderReqOfSeller.getQuantity())
                    .collect(Collectors.toList());

            //loop through buyers and deduct the instrument quantity from both buyers and seller
            int sellerQuantity = orderReqOfSeller.getQuantity();

            for (OrderBook orderReqOfBuyer : buyers) {
                Client buyer = orderReqOfBuyer.getClient();

                if (sellerQuantity <= orderReqOfBuyer.getQuantity()) {

                    exchangeStocks(buyer, seller, sellerOrderBookInstance);

                    sellerOrderBookInstance.setOrderStatus(OrderStatus.COMPLETED);
                    orderBookRepository.save(sellerOrderBookInstance);

                    if (sellerQuantity == orderReqOfBuyer.getQuantity()) {
                        orderReqOfBuyer.setOrderStatus(OrderStatus.COMPLETED);
                        orderBookRepository.save(orderReqOfBuyer);
                    }

                    break;
                } else {
                    exchangeStocks(buyer, seller, orderReqOfBuyer);

                    orderReqOfBuyer.setOrderStatus(OrderStatus.COMPLETED);
                    orderBookRepository.save(orderReqOfBuyer);

                    sellerQuantity = sellerQuantity - orderReqOfBuyer.getQuantity();
                }
            }
        }

        //just calling the save method to persist any unnoticed changes
        return orderBookRepository.save(sellerOrderBookInstance);
    }

    /*
        Exchanges stock among the buyer and the seller based on the orderReq.
        Transfers the stocks present in orderReq from the seller to buyer.
    */
    private void exchangeStocks(Client buyer, Client seller, OrderBook orderReq) {
        buyer.setTransactionLimit(buyer.getTransactionLimit() - orderReq.getQuantity() * orderReq.getPrice());
        seller.setTransactionLimit(seller.getTransactionLimit() - orderReq.getQuantity() * orderReq.getPrice());

        clientRepository.save(buyer);
        clientRepository.save(seller);

        stockService.saveStock(buyer, orderReq.getInstrument(), orderReq.getQuantity());
        stockService.saveStock(seller, orderReq.getInstrument(), -1 * orderReq.getQuantity());
    }


}
