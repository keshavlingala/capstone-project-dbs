package com.teamerror.capstoneprojectdbs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Stocks {
    @Id
    String stockId;
    @ManyToOne
    Client clientId;
    @ManyToOne
    Instrument instrumentId;
    Integer quantity;
}
