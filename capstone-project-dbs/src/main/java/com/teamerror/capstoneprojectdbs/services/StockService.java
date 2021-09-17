package com.teamerror.capstoneprojectdbs.services;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import com.teamerror.capstoneprojectdbs.repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    StocksRepository stocksRepository;

    public Stocks saveStock(Client client, Instrument instrument,int quantity){
        //Stocks stock = stocksRepository.findByClientIdAndInstrument(client,instrument);

        return null;
    }

}

