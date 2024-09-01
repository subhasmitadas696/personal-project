package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.PlotInformation;

public interface PlotInformationRepository extends JpaRepository<PlotInformation, String> {

	List<PlotInformation> findAllByKhatianCode(String khatianCode);

}
