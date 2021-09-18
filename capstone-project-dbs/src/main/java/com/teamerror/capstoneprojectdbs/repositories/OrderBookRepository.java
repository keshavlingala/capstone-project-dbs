package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> {

    List<OrderBook> findAllByOrderDirectionAndOrderStatus(OrderDirection orderDirection, OrderStatus orderStatus);
    List<OrderBook> findAllByOrderDirection(OrderDirection orderDirection);

}
