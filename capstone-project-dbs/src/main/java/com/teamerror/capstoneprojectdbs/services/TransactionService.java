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

import javax.transaction.Transactional;
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
    ClientService clientService;

    public OrderBook processTransaction(OrderBookRequest orderBookRequest) {
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.BUY)) {
            return this.buyOrder(orderBookRequest);
        }
        if (orderBookRequest.getOrderDirection().equals(OrderDirection.SELL)) {
            return this.sellOrder(orderBookRequest);
        }
        throw new ValidationException("Invalid request");
    }

    private OrderBook buyOrder(OrderBookRequest orderReqOfBuyer) {

        Instrument buyerInstrument = instrumentRepository.findById(orderReqOfBuyer.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument Not Found"));
        Client buyer = clientRepository.findById(orderReqOfBuyer.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));

        //first create the OrderBook instance of the seller request(to be saved to the database later)
        OrderBook buyerOrderBookInstance = new OrderBook(
                UUID.randomUUID().toString(),
                buyer,
                buyerInstrument,
                orderReqOfBuyer.getPrice(),
                orderReqOfBuyer.getQuantity(),
                OrderStatus.PROCESSING,
                OrderDirection.BUY,
                false,
                new Date()
        );

        System.out.println("uuid:"+buyerOrderBookInstance.getOrderId());

        List<OrderBook> sellOrdersWithSameInstrument = orderBookRepository
                .findAllByOrderDirectionAndOrderStatus(OrderDirection.SELL, OrderStatus.PROCESSING).stream()
                .filter(record -> {
                    return record.getInstrument().getInstrumentId().equals(buyerInstrument.getInstrumentId());
                })
                .collect(Collectors.toList());

        //if there are no buyers with same instrument save it in order book table.
        if (sellOrdersWithSameInstrument.isEmpty()) {
            return orderBookRepository.save(buyerOrderBookInstance);
        }

        double buyerStocksWorth = orderReqOfBuyer.getQuantity() * orderReqOfBuyer.getPrice();

        //perfectSellers = sellers with same instrument and same price expectations and has enough transaction limit to make the transaction
        List<OrderBook> perfectSellers = sellOrdersWithSameInstrument
                .stream()
                .filter(orders -> orders.getClient().getTransactionLimit() >= buyerStocksWorth)
                .filter(item -> item.getPrice().equals(orderReqOfBuyer.getPrice())).collect(Collectors.toList());

        if (perfectSellers.isEmpty()) {
            return orderBookRepository.save(buyerOrderBookInstance);
        }

        // One seller Case (when there is a sell order 's' where s.quantity >= buyerOrder.quantity)
        Optional<OrderBook> minSellerOrderReqOpt = perfectSellers.stream()
                .filter(order -> order.getQuantity() >= orderReqOfBuyer.getQuantity())
                .min(Comparator.comparing(OrderBook::getTimeStamp));

        if (minSellerOrderReqOpt.isPresent()) {
            OrderBook minSellerOrderBookInstance = minSellerOrderReqOpt.get();
            // Perfect Buy Sell Trade
            Client seller = minSellerOrderBookInstance.getClient();

            exchangeStocks(buyer, seller, buyerOrderBookInstance,minSellerOrderBookInstance,true);
        } else {
            // Match multiple sellers to single buyer

            //sort perfectSellers based on timestamp and select the sellers which have less quantity than the current buyer
            List<OrderBook> sellers = perfectSellers.stream()
                    .sorted(Comparator.comparing(OrderBook::getTimeStamp))
                    .filter(order -> order.getQuantity() < orderReqOfBuyer.getQuantity())
                    .collect(Collectors.toList());

            //loop through sellers and deduct the instrument quantity from both sellers and buyer
            int buyerQuantity = orderReqOfBuyer.getQuantity();

            for (OrderBook sellerOrderBookInstance : sellers) {
                Client seller = sellerOrderBookInstance.getClient();

                if (buyerQuantity <= sellerOrderBookInstance.getQuantity()) {
                    exchangeStocks(buyer, seller, buyerOrderBookInstance,sellerOrderBookInstance,true);
                    break;
                } else {
                    buyerQuantity = buyerQuantity - sellerOrderBookInstance.getQuantity();
                    exchangeStocks(buyer, seller, buyerOrderBookInstance,sellerOrderBookInstance,false);
                }
            }
        }

        return orderBookRepository.save(buyerOrderBookInstance);

    }

    private OrderBook sellOrder(OrderBookRequest orderReqOfSeller) {

        Instrument sellerInstrument = instrumentRepository.findById(orderReqOfSeller.getInstrumentId())
                .orElseThrow(() -> new ResourceNotFoundException("Instrument Not Found"));
        Client seller = clientRepository.findById(orderReqOfSeller.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));


        //check whether the seller has the specified stock and if he/she has the atleast the specified number of stocks
        Stocks stockAboutToSell = stocksRepository.findByClientAndInstrument(seller,sellerInstrument).orElseThrow(() ->
                new ValidationException("seems like the seller doesn't have the specified stocks"));
        if(stockAboutToSell.getQuantity() < orderReqOfSeller.getQuantity()){
            throw new ValidationException("the seller doesn't have the specified number of stocks");
        }

        //first create the OrderBook instance of the seller request(to be saved to the database later)
        OrderBook sellerOrderBookInstance = new OrderBook(UUID.randomUUID().toString(),
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
            OrderBook minBuyerOrderBookInstance = minBuyerOrderReqOpt.get();
            // Perfect Buy Sell Trade
            Client buyer = minBuyerOrderBookInstance.getClient();

            exchangeStocks(buyer, seller, minBuyerOrderBookInstance,sellerOrderBookInstance,false);
        } else {
            // Match multiple Buyers to single Seller

            //sort perfectBuyers based on timestamp and select the buyers which have less quantity than the current seller
            List<OrderBook> buyers = perfectBuyers.stream()
                    .sorted(Comparator.comparing(OrderBook::getTimeStamp))
                    .filter(order -> order.getQuantity() < orderReqOfSeller.getQuantity())
                    .collect(Collectors.toList());

            //loop through buyers and deduct the instrument quantity from both buyers and seller
            int sellerQuantity = orderReqOfSeller.getQuantity();

            for (OrderBook buyerOrderBookInstance : buyers) {
                Client buyer = buyerOrderBookInstance.getClient();

                if (sellerQuantity <= buyerOrderBookInstance.getQuantity()) {
                    exchangeStocks(buyer, seller, buyerOrderBookInstance,sellerOrderBookInstance,false);
                    break;
                } else {
                    sellerQuantity = sellerQuantity - buyerOrderBookInstance.getQuantity();
                    exchangeStocks(buyer, seller, buyerOrderBookInstance,sellerOrderBookInstance,true);
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
    @Transactional
    private void exchangeStocks(Client buyer, Client seller, OrderBook buyerOrderBookInstance,OrderBook sellerOrderBookInstance,boolean considerBuyersOrderReq) {
        OrderBook orderReq;
        if(considerBuyersOrderReq){ //consider the quantity and price present in buyerOrderBookInstance while exchanging the stock
            orderReq = buyerOrderBookInstance;
        }
        else{
            orderReq = sellerOrderBookInstance;
        }

        stockService.saveStock(buyer, orderReq.getInstrument(), orderReq.getQuantity());
        stockService.saveStock(seller, orderReq.getInstrument(), -1 * orderReq.getQuantity());

        //adjust the transaction limit
        buyer.setTransactionLimit(buyer.getTransactionLimit() - orderReq.getQuantity() * orderReq.getPrice());
        seller.setTransactionLimit(seller.getTransactionLimit() - orderReq.getQuantity() * orderReq.getPrice());
        clientRepository.save(buyer);
        clientRepository.save(seller);

        // we need to adjust the stock quantity in the order request of seller and buyer
        buyerOrderBookInstance.setQuantity(buyerOrderBookInstance.getQuantity() - orderReq.getQuantity());
        sellerOrderBookInstance.setQuantity(sellerOrderBookInstance.getQuantity() - orderReq.getQuantity());
        if(buyerOrderBookInstance.getQuantity().equals(0)){
            buyerOrderBookInstance.setOrderStatus(OrderStatus.COMPLETED);
        }
        if(sellerOrderBookInstance.getQuantity().equals(0)){
            sellerOrderBookInstance.setOrderStatus(OrderStatus.COMPLETED);
        }
        orderBookRepository.save(buyerOrderBookInstance);
        orderBookRepository.save(sellerOrderBookInstance);

    }


}
