package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Client {
    @Id
    String clientId;
    String clientName;
    @ManyToOne
    @JoinColumn(name="custodian_id")
    Custodian custodianId;
    Double transactionLimit;

}
