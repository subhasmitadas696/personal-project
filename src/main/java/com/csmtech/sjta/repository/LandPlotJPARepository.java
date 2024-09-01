package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Land_plot;

public interface LandPlotJPARepository extends JpaRepository<Land_plot, Integer> {
	Land_plot findByIntIdAndBitDeletedFlag(Integer intId, Boolean bitDeletedFlag);

	@Query("From Land_plot where deleted_flag='0' ")
	List<Land_plot> findAllByBitDeletedFlag();

	Land_plot findByIntLandApplicantIdAndBitDeletedFlag(Integer intLandApplicantId, Boolean bitDeletedFlag);

	Land_plot deleteByIntLandApplicantIdAndBitDeletedFlag(Integer intLandApplicantId, Boolean bitDeletedFlag);
}