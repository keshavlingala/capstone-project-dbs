package com.teamerror.capstoneprojectdbs.models;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import lombok.Data;

@Data
public class OrderBookRequest {
    String clientId;
    String instrumentId;
    Double price;
    Integer quantity;
    OrderDirection orderDirection;
    Boolean limitOrder;
}
