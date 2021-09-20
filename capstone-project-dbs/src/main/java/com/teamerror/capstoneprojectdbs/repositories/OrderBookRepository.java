package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.OrderBook;
import com.teamerror.capstoneprojectdbs.models.ClientWiseStats;
import com.teamerror.capstoneprojectdbs.models.CustodianWiseStats;
import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> {

    List<OrderBook> findAllByOrderDirectionAndOrderStatus(OrderDirection orderDirection, OrderStatus orderStatus);
    List<OrderBook> findAllByOrderDirection(OrderDirection orderDirection);

    @Query(value="select custodian_id as custodianId," +
            "sum(IF(ob.order_direction='BUY' and ob.order_status='COMPLETED',ob.price * initial_quantity,0)) as totalBuy," +
            "sum(IF(ob.order_direction='SELL' and ob.order_status='COMPLETED',ob.price * initial_quantity,0)) as totalSell " +
            "from client c left join order_book ob on c.client_id = ob.client_id " +
            "group by custodian_id",nativeQuery = true)
    List<CustodianWiseStats> getCustodianWiseStats();

    @Query(value="select c.client_id as clientId," +
            "sum(if(ob.order_direction = 'SELL' and ob.order_status='COMPLETED',ob.price * initial_quantity,0)) as totalSell," +
            "sum(if(ob.order_direction = 'BUY' and ob.order_status='COMPLETED',ob.price * initial_quantity,0)) as totalBuy " +
            "from client c " +
            "left join order_book ob on c.client_id = ob.client_id " +
            "group by c.client_id",nativeQuery = true)
    List<ClientWiseStats> getClientWiseStats();

}

