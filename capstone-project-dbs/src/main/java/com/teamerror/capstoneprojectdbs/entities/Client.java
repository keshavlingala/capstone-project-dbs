package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
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
