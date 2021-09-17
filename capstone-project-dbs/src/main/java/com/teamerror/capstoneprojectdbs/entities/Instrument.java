package com.teamerror.capstoneprojectdbs.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Instrument {
    @Id
    String instrumentId;

    @NotNull
    String instrumentName;

    Double faceValue;

    @Temporal(TemporalType.TIMESTAMP)
    Date expiryDate;

    @NotNull
    Integer minQuantity;
}
