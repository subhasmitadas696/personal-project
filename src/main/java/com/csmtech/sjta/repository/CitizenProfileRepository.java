package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.CitizenProfileEntity;

public interface CitizenProfileRepository extends JpaRepository<CitizenProfileEntity, Serializable> {

	@Query(value = "select * from citizen_profile_details where citizen_profile_details_id=:userId and status='0' ", nativeQuery = true)
	CitizenProfileEntity findByCitizenId(BigInteger userId);

}
