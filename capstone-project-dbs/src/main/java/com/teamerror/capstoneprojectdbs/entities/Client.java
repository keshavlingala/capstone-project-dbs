package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
    Custodian custodianId;
    @NotNull
    Double transactionLimit;
}
