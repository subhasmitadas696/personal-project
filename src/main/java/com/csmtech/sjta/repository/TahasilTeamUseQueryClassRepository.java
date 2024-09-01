package com.csmtech.sjta.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.entity.PlotVerification;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TahasilTeamUseQueryClassRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	public List<Object[]> getTahasilsWithStatistics(String tahasilCode) {
		String nativeQuery = "SELECT * FROM (SELECT A.tahasil_code, A.tahasil_name, \r\n"
				+ "(SELECT COUNT(DISTINCT vm.village_code) FROM land_bank.village_master as vm \r\n"
				+ "INNER JOIN land_bank.khatian_information as ki ON vm.village_code = ki.village_code \r\n"
				+ "WHERE tahasil_code = A.tahasil_code) AS total_mouza, \r\n"
				+ "(SELECT COUNT(DISTINCT Y.khata_no) FROM land_bank.khatian_information Y \r\n"
				+ "inner join land_bank.plot_information B on Y.khatian_code=B.khatian_code \r\n"
				+ "WHERE Y.village_code IN ( SELECT village_code FROM land_bank.village_master \r\n"
				+ "WHERE tahasil_code = A.tahasil_code ) ) AS total_katha, ( SELECT COUNT(plot_code) FROM land_bank.plot_information WHERE khatian_code IN ( SELECT khatian_code FROM land_bank.khatian_information WHERE village_code IN ( SELECT village_code FROM land_bank.village_master \r\n"
				+ "WHERE tahasil_code = A.tahasil_code ) ) ) AS total_plot, ( SELECT SUM(area_acre) FROM land_bank.plot_information WHERE khatian_code IN ( SELECT khatian_code FROM land_bank.khatian_information WHERE village_code IN ( SELECT village_code FROM land_bank.village_master \r\n"
				+ "WHERE tahasil_code = A.tahasil_code ) ) ) AS total_area, CAST(st_extent(ST_Transform(tb.geom,3857)) as VARCHAR ) as extent \r\n"
				+ "FROM land_bank.tahasil_master A \r\n"
				+ "JOIN land_bank.tahasil_boundary tb ON(A.tahasil_code=tb.tahasil_code) \r\n"
				+ "WHERE A.tahasil_code =:tahasilCode \r\n"
				+ "GROUP BY A.tahasil_code ORDER BY A.tahasil_name ASC) X WHERE X.total_plot > 0";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		entity.close();
		return results;
	}

	public List<TahasilTeamUseRequestDto> getTahasilLoginDetails(String tahasilCode) {
		String nativeQuery = "SELECT tahasil_code, password FROM public.tahasil_login_details  "
				+ "  where tahasil_code=:tahasilCode ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		List<TahasilTeamUseRequestDto> loginDetailsList = mapToDTO(resultList);

		entity.close();
		return loginDetailsList;
	}

	private List<TahasilTeamUseRequestDto> mapToDTO(List<Object[]> resultList) {
		return resultList.stream()
				.map(objects -> new TahasilTeamUseRequestDto((String) objects[0], (String) objects[1]))
				.collect(Collectors.toList());
	}

	// tahasil_code
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getVillageInfoForTahasil(String tahasilCode) {
		String sqlQuery = "SELECT A.village_code, A.village_name, ( SELECT COUNT(DISTINCT Y.khata_no) \r\n"
				+ "FROM land_bank.khatian_information Y \r\n"
				+ "INNER JOIN land_bank.plot_information B ON Y.khatian_code = B.khatian_code \r\n"
				+ "WHERE Y.village_code = A.village_code ) AS total_katha, ( SELECT COUNT(plot_code) \r\n"
				+ "FROM land_bank.plot_information \r\n"
				+ "WHERE khatian_code IN ( SELECT khatian_code FROM land_bank.khatian_information \r\n"
				+ "WHERE village_code = A.village_code ) ) AS total_plot, ( SELECT SUM(area_acre) \r\n"
				+ "FROM land_bank.plot_information WHERE khatian_code IN ( SELECT khatian_code \r\n"
				+ "FROM land_bank.khatian_information WHERE village_code = A.village_code ) ) AS total_area, CAST(st_extent(ST_Transform(ss.geom, 3857)) AS character varying) as extent, COUNT(DISTINCT A.village_code) as totalVillage, A.thana_no as ps_name \r\n"
				+ "FROM land_bank.village_master A \r\n"
				+ "JOIN land_bank.village_boundary ss ON ss.village_code = A.village_code \r\n"
				+ "INNER JOIN land_bank.khatian_information as ki ON A.village_code = ki.village_code \r\n"
				+ "WHERE A.tahasil_code = :tahasilCode \r\n" + "GROUP BY A.village_code, A.village_name, ss.geom \r\n"
				+ "ORDER BY A.village_name ASC";

		Query query = entity.createNativeQuery(sqlQuery).setParameter("tahasilCode", tahasilCode);
		List<Object[]> results = query.getResultList();

		entity.close();
		return results;
	}

	// village Gis data return
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getVillageInfoForTahasilGisData() {
		String sqlQuery = "SELECT vill_code," + " vill_name ,"
				+ " CAST(st_extent(ST_Transform(geom, 3857)) AS character varying) as extent "
				+ " FROM land_bank.gis_village_boundary  " + " GROUP BY vill_code, vill_name ORDER BY vill_name";

		Query query = entity.createNativeQuery(sqlQuery);
		List<Object[]> data = query.getResultList();

		entity.close();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getKhatianPlotsForVillage(String villageCode) {

		String sqlQuery = "SELECT A.khatian_code, A.khata_no, B.plot_no, B.area_acre, C.village_code, C.village_name, D.tahasil_code, D.tahasil_name, E.district_code, E.district_name, B.plot_code, F.status, F.remark, CAST((select st_extent(ST_Transform(geom, 3857)) \r\n"
				+ "from land_bank.sjta_plot_boundary where plot_code= B.plot_code) AS character varying) as extent FROM land_bank.district_master E \r\n"
				+ "INNER JOIN land_bank.tahasil_master D ON E.district_code = D.district_code \r\n"
				+ "INNER JOIN land_bank.village_master c ON D.tahasil_code = C.tahasil_code \r\n"
				+ "INNER JOIN land_bank.khatian_information A ON c.village_code = A.village_code \r\n"
				+ "INNER JOIN land_bank.plot_information B ON A.khatian_code = B.khatian_code \r\n"
				+ "LEFT JOIN public.plot_verification F ON B.plot_code = F.plot_code \r\n"
				+ "WHERE A.village_code =:villageCode";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("villageCode", villageCode);
		List<Object[]> data = query.getResultList();

		entity.close();
		return data;

	}

	// get dist data
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistricts() {
		String nativeQuery = "SELECT district_code, district_name FROM land_bank.district_master order by district_name";
		Query query = entity.createNativeQuery(nativeQuery);
		List<Object[]> results = query.getResultList();
		log.info("Sucess..Execute and return the result");
		entity.close();
		return results;

	}

	// tahasil search for village
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getVillageInfoForTahasilAndSearchFunction(String tahasilCode, String tahasilVillageName) {
		String sqlQuery = "SELECT A.village_code, A.village_name, ( SELECT COUNT(DISTINCT Y.khata_no) \r\n"
				+ "FROM land_bank.khatian_information Y \r\n"
				+ "INNER JOIN land_bank.plot_information B ON Y.khatian_code = B.khatian_code \r\n"
				+ "WHERE Y.village_code = A.village_code ) AS total_katha, ( SELECT COUNT(plot_code) \r\n"
				+ "FROM land_bank.plot_information WHERE khatian_code IN ( SELECT khatian_code FROM land_bank.khatian_information WHERE village_code = A.village_code ) ) AS total_plot, ( SELECT SUM(area_acre) FROM land_bank.plot_information WHERE khatian_code IN ( SELECT khatian_code FROM land_bank.khatian_information WHERE village_code = A.village_code ) ) AS total_area, CAST(st_extent(ST_Transform(ss.geom, 3857)) AS character varying) as extent, COUNT(DISTINCT A.village_code) as totalVillage, A.thana_no as ps_name \r\n"
				+ "FROM land_bank.village_master A \r\n" + "JOIN land_bank.village_boundary ss \r\n"
				+ "ON ss.village_code = A.village_code \r\n" + "INNER JOIN land_bank.khatian_information as ki \r\n"
				+ "ON A.village_code = ki.village_code \r\n"
				+ "WHERE A.tahasil_code = :tahasilCode AND A.village_code = :tahasilVillageName \r\n"
				+ "GROUP BY A.village_code, A.village_name, ss.geom \r\n" + "ORDER BY A.village_name ASC";

		Query query = entity.createNativeQuery(sqlQuery).setParameter("tahasilCode", tahasilCode)
				.setParameter("tahasilVillageName", tahasilVillageName);
		List<Object[]> results = query.getResultList();

		entity.close();
		return results;
	}

	public Integer saveRecord(PlotVerification plotVerfication) {
		return null;
	}

}
