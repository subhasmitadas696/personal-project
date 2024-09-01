package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.LandPlot;

public interface LandPlotRepository extends JpaRepository<LandPlot, BigInteger> {

	List<LandPlot> findByLandApplicantId(BigInteger landApplicantId);

}
