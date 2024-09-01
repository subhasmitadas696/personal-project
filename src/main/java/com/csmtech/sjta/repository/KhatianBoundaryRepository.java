package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Khatian_information;

public interface KhatianBoundaryRepository extends JpaRepository<Khatian_information, Serializable> {

	@Query(value = "WITH PlotInformation AS (SELECT\r\n"
			+ "ki.khata_no,\r\n"
			+ "STRING_AGG(pi.plot_code, ',') AS plot_code,\r\n"
			+ "ki.khatian_code  \r\n"
			+ "FROM land_bank.village_master vm\r\n"
			+ "JOIN land_bank.khatian_information ki ON ki.village_code = vm.village_code\r\n"
			+ "JOIN land_bank.plot_information pi ON pi.khatian_code = ki.khatian_code\r\n"
			+ "WHERE\r\n"
			+ "vm.village_code =:selVillageCode\r\n"
			+ "GROUP BY\r\n"
			+ "ki.khata_no, ki.khatian_code, pi.plot_code) \r\n"
			+ "SELECT\r\n"
			+ "(select khata_no from land_bank.khatian_information where khatian_code=p.khatian_code limit 1) as  khata_no, \r\n"
			+ "CAST(ST_extent(ST_Transform(spb.geom, 3857)) as varchar) AS extent FROM\r\n"
			+ "land_bank.sjta_plot_boundary spb\r\n"
			+ "JOIN PlotInformation p ON spb.plot_code = p.plot_code WHERE\r\n"
			+ "spb.plot_code IN (SELECT unnest(string_to_array(plot_code, ',')) FROM PlotInformation) GROUP BY\r\n"
			+ "p.khatian_code  \r\n"
			+ " ", nativeQuery = true)
	List<Object[]> viewAll(String selVillageCode);

}