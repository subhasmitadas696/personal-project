package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.LandAllotementForAuctionEntity;

public interface LandAllotementForAuctionRepository extends JpaRepository<LandAllotementForAuctionEntity, Serializable> {
	
	@Query(value = " select * from application.land_allotement_for_auction where deleted_flag='0' limit ? offset ? " , nativeQuery = true)
	public List<LandAllotementForAuctionEntity> getMultiRecord(Integer limit,Integer offset);

}
