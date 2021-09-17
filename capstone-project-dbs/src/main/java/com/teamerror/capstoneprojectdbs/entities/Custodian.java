package com.teamerror.capstoneprojectdbs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Custodian {
    @Id
    String custodianId;
    String custodianName;

}
