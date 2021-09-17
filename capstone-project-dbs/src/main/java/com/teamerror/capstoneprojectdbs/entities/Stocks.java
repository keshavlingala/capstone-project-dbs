package com.teamerror.capstoneprojectdbs.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Stocks {
    @Id
    UUID stockId;

    @ManyToOne
    @NotNull
    Client client;

    @ManyToOne
    @NotNull
    Instrument instrument;

    @NotNull
    Integer quantity;
}
