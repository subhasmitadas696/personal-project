package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import com.csmtech.sjta.entity.Village_master;

public interface VillageMasterRepo extends JpaRepository<Village_master, String> {

	@Query("From Village_master where (:selTahasilCode='0' or selTahasilCode like CONCAT('%',:selTahasilCode,'%'))  and (:txtVillageCode='0' or txtVillageCode like CONCAT('%',:txtVillageCode,'%'))  and (:txtVillageName='0' or txtVillageName like CONCAT('%',:txtVillageName,'%')) ORDER BY  selTahasilCode, txtVillageCode, txtVillageName ")
	List<Village_master> findAll(String selTahasilCode, String txtVillageCode, String txtVillageName);

	@Query(value = "select count(*) from land_bank.village_master", nativeQuery = true)
	Integer countAll();

	Village_master findByTxtVillageCode(String txtVillageCode);

	@Query("From Village_master where (:selTahasilCode='0' or selTahasilCode like CONCAT('%',:selTahasilCode,'%'))  ORDER BY  selTahasilCode ")
	List<Village_master> findByselTahasilCode(String selTahasilCode);

	@Query("From Village_master where (:txtVillageName='0' or txtVillageName like CONCAT('%',:txtVillageName,'%')) and selTahasilCode =:selTahasilCode ORDER BY selTahasilCode ")
	List<Village_master> findByTxtTahasilName(String txtVillageName, String selTahasilCode);
}