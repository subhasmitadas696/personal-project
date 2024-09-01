package com.csmtech.sjta.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.KhatianDigitalInformation;

public interface KhatianDigitalInfoRepo extends JpaRepository<KhatianDigitalInformation, BigInteger> {

	@Query(value = "select max((digital_information_id) + 1 ) as digital_information_id from land_bank.khatian_digital_information", nativeQuery = true)
	BigInteger getLastDigitInfoId();

}
