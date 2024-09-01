package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.csmtech.sjta.entity.LandGIS;

public interface LandGISRepo extends JpaRepository<LandGIS, Integer> {

	@Modifying
	@Transactional
	@Query(value = "insert into land_bank.land_gis(plot_code) values(:khatianPlotCode) ", nativeQuery = true)
	public int savePlotCode(String khatianPlotCode);

	@Query(value = "select * from land_bank.land_gis where plot_code =:khatianPlotCode ", nativeQuery = true)
	public LandGIS findByPlotCode(String khatianPlotCode);

}
