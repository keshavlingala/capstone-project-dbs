package com.teamerror.capstoneprojectdbs.entities;

import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class OrderBook {
    @Id
    UUID orderId;
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
