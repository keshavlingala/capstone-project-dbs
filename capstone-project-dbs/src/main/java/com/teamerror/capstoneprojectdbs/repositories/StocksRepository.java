package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StocksRepository extends JpaRepository<Stocks, String> {
}
