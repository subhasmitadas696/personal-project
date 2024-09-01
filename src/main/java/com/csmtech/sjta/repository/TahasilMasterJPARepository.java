package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Tahasil_master;

public interface TahasilMasterJPARepository extends JpaRepository<Tahasil_master, String> {

	@Query("From Tahasil_master where (:selDistrictCode='0' or selDistrictCode like CONCAT('%',:selDistrictCode,'%'))  and (:txtTahasilName='0' or txtTahasilName like CONCAT('%',:txtTahasilName,'%')) ORDER BY selDistrictCode , txtTahasilName ")
	List<Tahasil_master> findAll(String selDistrictCode, String txtTahasilName);

	@Query("FROM Tahasil_master WHERE (:selDistrictCode='0' OR LOWER(selDistrictCode) = LOWER(:selDistrictCode)) AND (:txtTahasilName='0' OR LOWER(txtTahasilName) = LOWER(:txtTahasilName)) ORDER BY txtTahasilCode")
	List<Tahasil_master> findBySelDistrictCodeAndTxtTahasilName(String selDistrictCode, String txtTahasilName);

	@Query("From Tahasil_master where (:selDistrictCode='0' or selDistrictCode like CONCAT('%',:selDistrictCode,'%')) ORDER BY txtTahasilCode ")
	List<Tahasil_master> findBySelDistrictCode(String selDistrictCode);

	@Query("FROM Tahasil_master WHERE (:txtTahasilName='0' OR LOWER(txtTahasilName) LIKE CONCAT('%', LOWER(:txtTahasilName), '%')) AND selDistrictCode = :selDistrictCode ORDER BY txtTahasilCode")
	List<Tahasil_master> findByTxtTahasilName(String txtTahasilName, String selDistrictCode);

	@Query(value = "select count(*) from land_bank.tahasil_master", nativeQuery = true)
	Integer countAll();

	Tahasil_master findByTxtTahasilCode(String txtTahasilCode);

	@Query(value = "select count(*) from land_bank.tahasil_master where district_code =:selDistrictCode", nativeQuery = true)
	Integer countOfDistrict(String selDistrictCode);

	@Query(value = "select count(*) from land_bank.tahasil_master where tahasil_code =:txtTahasilCode", nativeQuery = true)
	Integer countOfTahasilCode(String txtTahasilCode);

	@Query(value = "select count(*) from land_bank.tahasil_master where LOWER(tahasil_name) =LOWER(:txtTahasilName)", nativeQuery = true)
	Integer countOfTahasilName(String txtTahasilName);

	@Query(value = "select MAX(tahasil_code) as tahasil_code from land_bank.tahasil_master where district_code =:selDistrictCode", nativeQuery = true)
	List<Object> getLastTahasilCode(String selDistrictCode);

}