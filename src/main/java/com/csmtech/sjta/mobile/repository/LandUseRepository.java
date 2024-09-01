package com.csmtech.sjta.mobile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.mobile.dto.LandTypeDto;
import com.csmtech.sjta.mobile.dto.LandUseDto;
import com.csmtech.sjta.mobile.entity.LandUseEntity;

public interface LandUseRepository extends JpaRepository<LandUseEntity, Integer>{

	@Query("select new com.csmtech.sjta.mobile.dto.LandUseDto(l.landUseId,l.landUse) from LandUseEntity l where l.status = '0'")
	List<LandUseDto> findDetails();

}
