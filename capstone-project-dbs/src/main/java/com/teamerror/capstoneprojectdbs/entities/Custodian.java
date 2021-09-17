package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Custodian {
    @Id
    @NotNull
    String custodianId;
    @NotNull
    String custodianName;

}
