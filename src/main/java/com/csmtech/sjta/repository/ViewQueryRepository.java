package com.csmtech.sjta.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.QueryEntity;

/**
 * @author prasanta.sethi
 */

public interface ViewQueryRepository extends JpaRepository<QueryEntity, BigInteger> {

}
