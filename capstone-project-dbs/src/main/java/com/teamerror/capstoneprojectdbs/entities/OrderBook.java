package com.teamerror.capstoneprojectdbs.entities;

import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String orderId;
    @ManyToOne
    @NotNull
    Client clientId;
    @ManyToOne
    @NotNull
    Instrument instrumentId;
    @NotNull
    Double price;
    @NotNull
    Integer quantity;
    @Enumerated(EnumType.STRING)
    @NotNull
    OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @NotNull
    OrderDirection orderDirection;
    @NotNull
    Boolean limitOrder;

}
