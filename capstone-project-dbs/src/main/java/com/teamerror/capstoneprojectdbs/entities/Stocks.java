package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Stocks {
    @Id
    @NotNull
    String stockId;
    @ManyToOne
    @NotNull
    Client clientId;
    @ManyToOne
    @NotNull
    Instrument instrumentId;
    @NotNull
    Integer quantity;
}
