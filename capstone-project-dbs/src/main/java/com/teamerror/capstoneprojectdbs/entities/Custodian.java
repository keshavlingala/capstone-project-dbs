package com.teamerror.capstoneprojectdbs.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Custodian {
    @Id
    @NotNull
    String custodianId;
    @NotNull
    String custodianName;

}
