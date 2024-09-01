package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Khatian_information;

public interface KhatianInformationRepo extends JpaRepository<Khatian_information, String> {

	@Query("From Khatian_information where (:selVillageCode='0' or selVillageCode like CONCAT('%',:selVillageCode,'%'))  and (:txtKhatianCode='0' or txtKhatianCode like CONCAT('%',:txtKhatianCode,'%'))  and (:txtKhataNo='0' or txtKhataNo like CONCAT('%',:txtKhataNo,'%')) ORDER BY  selVillageCode, txtKhataNo")
	List<Khatian_information> findAll(String selVillageCode, String txtKhatianCode, String txtKhataNo);

	@Query(value = "select count(*) from land_bank.plot_information", nativeQuery = true)
	Integer countByAll();

	Khatian_information findByTxtKhatianCode(String txtKhatianCode);

	@Query(value = "SELECT d.digital_file FROM land_bank.Khatian_information k "
			+ "JOIN land_bank.khatian_digital_information d USING (khatian_code) "
			+ "WHERE k.khatian_code = :txtKhatianCode", nativeQuery = true)
	List<Object> getFile(String txtKhatianCode);

	@Query("From Khatian_information where (:selVillageCode='0' or selVillageCode like CONCAT('%',:selVillageCode,'%'))   ORDER BY  selVillageCode, txtKhataNo")
	List<Khatian_information> findBySelVillageCode(String selVillageCode);

	@Query("From Khatian_information where (:txtKhataNo='0' or txtKhataNo like CONCAT('%',:txtKhataNo,'%')) and selVillageCode =:selVillageCode ORDER BY  selVillageCode, txtKhataNo")
	List<Khatian_information> findByTxtKhataNo(String txtKhataNo, String selVillageCode);

	@Query(value = "select count(*) from land_bank.khatian_information ki JOIN land_bank.village_master vm USING(village_code) "
			+ "where ki.khata_no =:txtKhataNo AND vm.tahasil_code =:selTahasilCode ", nativeQuery = true)
	Integer findByKhataNoOfSameTahasil(String txtKhataNo, String selTahasilCode);
}