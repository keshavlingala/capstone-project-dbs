package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Stocks {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
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
