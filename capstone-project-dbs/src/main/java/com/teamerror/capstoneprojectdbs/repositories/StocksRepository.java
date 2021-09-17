package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StocksRepository extends JpaRepository<Stocks, String> {
    List<Stocks> findByClientId(String id);
}
