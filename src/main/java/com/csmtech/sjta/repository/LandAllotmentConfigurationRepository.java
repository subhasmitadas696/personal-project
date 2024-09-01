package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.LandAllotmentConfigurationEntity;

public interface LandAllotmentConfigurationRepository
		extends JpaRepository<LandAllotmentConfigurationEntity, Serializable> {
	@Query(value = " select count(*) From application.land_allotment_configuration where deleted_flag='0' ", nativeQuery = true)
	Integer countByBitDeletedFlag();

	@Query(value = "update application.land_allotment_configuration set deleted_flag=B'1' where land_allotment_configuration_id=? ", nativeQuery = true)
	Integer updateDataById(BigInteger landId);

}
