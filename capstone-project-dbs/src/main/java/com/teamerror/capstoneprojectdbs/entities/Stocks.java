package com.teamerror.capstoneprojectdbs.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
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
