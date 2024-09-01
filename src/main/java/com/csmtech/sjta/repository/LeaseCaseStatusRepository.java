package com.csmtech.sjta.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.csmtech.sjta.entity.LeaseCaseStatusEntity;

public interface LeaseCaseStatusRepository extends JpaRepository<LeaseCaseStatusEntity, Serializable> {

}
