package com.teamerror.capstoneprojectdbs.entities;

import com.teamerror.capstoneprojectdbs.models.OrderDirection;
import com.teamerror.capstoneprojectdbs.models.OrderStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderBook {
    @Id
    @Column(columnDefinition = "VARCHAR(255)")
    String orderId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "instrument_id")
    Instrument instrument;

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

    @Temporal(TemporalType.TIMESTAMP)
    Date timeStamp;
}
