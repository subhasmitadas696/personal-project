package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.KhatianInformation;

public interface KhatianInformationRepository extends JpaRepository<KhatianInformation, String> {

	List<KhatianInformation> findAllByVillageCode(String villageCode);

}
