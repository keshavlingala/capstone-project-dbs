package com.teamerror.capstoneprojectdbs.models;

import com.teamerror.capstoneprojectdbs.entities.Client;
import com.teamerror.capstoneprojectdbs.entities.Instrument;
import lombok.Data;

@Data
public class OrderBookRequest {

    Client clientId;
    Instrument instrumentId;
     Double price;
    Integer quantity;
    OrderDirection orderDirection;
    Boolean limitOrder;
}
