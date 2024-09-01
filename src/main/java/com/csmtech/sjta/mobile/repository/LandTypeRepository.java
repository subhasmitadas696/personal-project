package com.csmtech.sjta.mobile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.mobile.dto.LandTypeDto;
import com.csmtech.sjta.mobile.entity.LandTypeEntity;

public interface LandTypeRepository extends JpaRepository<LandTypeEntity, Integer>{
	
	@Query("select new com.csmtech.sjta.mobile.dto.LandTypeDto(l.landTypeStatusId,l.landTypeStatus) from LandTypeEntity l where l.status = '0'")
	List<LandTypeDto> findDetails();

}
