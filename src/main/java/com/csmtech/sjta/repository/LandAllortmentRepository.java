package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.LandAllotementEntity;

public interface LandAllortmentRepository extends  JpaRepository<LandAllotementEntity, Serializable> {

	@Query(value ="",nativeQuery = true)
	LandAllotementEntity findBylandAllotmentId(BigInteger landAllotedId);

}
