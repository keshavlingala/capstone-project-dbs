package com.teamerror.capstoneprojectdbs.repositories;

import com.teamerror.capstoneprojectdbs.entities.Custodian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustodianRepository extends JpaRepository<Custodian, String> {
    List<Custodian> findAll();
}
