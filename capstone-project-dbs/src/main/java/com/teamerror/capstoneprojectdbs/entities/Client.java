package com.teamerror.capstoneprojectdbs.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Client {
    @Id
    @NotNull
    String clientId;

    @NotNull
    String clientName;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "custodian_id")
    Custodian custodian;

    @NotNull
    Double transactionLimit;
}
