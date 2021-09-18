package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import com.teamerror.capstoneprojectdbs.entities.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StocksRepository extends JpaRepository<Stocks, String> {
    List<Stocks> findAllByClient(Client id);
    Optional<Stocks> findByClientAndInstrument(Client client, Instrument instrument);
}
