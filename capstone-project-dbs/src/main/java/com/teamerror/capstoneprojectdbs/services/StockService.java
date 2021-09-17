package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.repositories.StocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StockService {

    StocksRepository stocksRepository;

    /**
     * save stock
     *
     * @param client     client
     * @param instrument instrument
     * @param quantity   quantity (Can be Positive or Negative)
     * @return {@link Stocks} saves a particular stocks for user, if already exists adds quantity ( Quantity can be Negative i.e. reduce Stocks for that client )
     * @see Stocks
     */
    public Stocks saveStock(Client client, Instrument instrument, Integer quantity) {
        Stocks getOrCreate = stocksRepository.findByClientIdAndInstrument(client, instrument).orElseGet(() -> {
            Stocks newStock = new Stocks();
            newStock.setStockId(UUID.randomUUID());
            newStock.setClientId(client);
            newStock.setInstrumentId(instrument);
            newStock.setQuantity(0);
            return newStock;
        });
        getOrCreate.setQuantity(getOrCreate.getQuantity() + quantity);
        return getOrCreate;
    }


    /**
     * get stock quantity
     *
     * @param client     client
     * @param instrument instrument
     * @return {@link Stocks} Returns a Stock Object if found else Create an empty stock with 0 quantity and returns
     * @see Stocks
     */
    Stocks getStockQuantity(Client client, Instrument instrument) {
        return stocksRepository.findByClientIdAndInstrument(client, instrument).orElseGet(() -> {
            Stocks newStock = new Stocks();
            newStock.setStockId(UUID.randomUUID());
            newStock.setClientId(client);
            newStock.setInstrumentId(instrument);
            newStock.setQuantity(0);
            return newStock;
        });
    }

}

