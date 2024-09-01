package com.csmtech.sjta.mobile.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.mobile.dto.VillageDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class VillageDetailsRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings("unchecked")
	public List<VillageDTO> getVillageDetails(String tahasilCode) {
		List<VillageDTO> villageList = new ArrayList<>();

		try {
			String nativeQuery = "select v.village_code, v.village_name,(SELECT COUNT(plot_code) FROM land_bank.plot_information"

					+ "	where khatian_code in (select khatian_code from land_bank.khatian_information "
					+ "where village_code = v.village_code) and plot_code not in (select plot_code from public.tahasil_plot_survey)) as total_plot from land_bank.village_master v "
					+ "where  v.tahasil_code =:tahasilCode  group by v.village_code,v.village_name ";
			Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);

			List<Object[]> resultList = query.getResultList();
			villageList = mapToDTO(resultList);
		} finally {
			entity.close();
		}
		return villageList;
	}

	private List<VillageDTO> mapToDTO(List<Object[]> resultList) {
		return resultList.stream()
				.map(objects -> new VillageDTO((String) objects[0], (String) objects[1], (BigInteger) objects[2]))
				.collect(Collectors.toList());
	}

	public List<VillageDTO> getVillageDetailsByPlot(String villageCode) {
		List<VillageDTO> villageList = new ArrayList<>();

		try {
			String nativeQuery = "SELECT v.village_code,v.village_name,t.tahasil_code,t.tahasil_name,d.district_name,\r\n"
					+ "p.plot_no,p.kissam,k.khata_no,total_area.total_area AS sum_area_acre,\r\n"
					+ "ST_Y(ST_Centroid(pb.geom)) AS latitude,ST_X(ST_Centroid(pb.geom)) AS longitude,"
					
					+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =p.plot_code) as coordinates,"
					+ "d.district_code,p.plot_code,k.khatian_code"
					+ " FROM\r\n"
					+ "land_bank.district_master d INNER JOIN land_bank.tahasil_master t ON t.district_code = d.district_code \r\n"
					+ "INNER JOIN land_bank.village_master v ON t.tahasil_code = v.tahasil_code \r\n"
					+ "INNER JOIN land_bank.khatian_information k ON v.village_code = k.village_code  \r\n"
					+ "INNER JOIN land_bank.plot_information p ON k.khatian_code = p.khatian_code \r\n"
					+ "LEFT JOIN (SELECT v.village_code AS sum_village_code,SUM(p.area_acre) AS total_area  \r\n"
					+ "FROM land_bank.village_master v INNER JOIN land_bank.khatian_information k \r\n"
					+ "ON v.village_code = k.village_code INNER JOIN land_bank.plot_information p \r\n"
					+ "ON k.khatian_code = p.khatian_code WHERE v.village_code =:villageCode GROUP BY\r\n"
					+ "v.village_code) AS total_area ON v.village_code = total_area.sum_village_code\r\n"
					+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(pb.plot_code=p.plot_code)\r\n"
					+ "WHERE v.village_code =:villageCode and p.plot_code not in (select plot_code from public.tahasil_plot_survey) ";
			Query query = entity.createNativeQuery(nativeQuery).setParameter("villageCode", villageCode);
			log.info("query starts executing");
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = query.getResultList();
			log.info("result fetched from db: " + resultList);
			if (resultList.size() > 0 && resultList != null) {
				villageList = mapToVillageDTO(resultList);
			} else {
				return villageList;
			}
		} finally {
			entity.close();
		}
		return villageList;
	}

	private List<VillageDTO> mapToVillageDTO(List<Object[]> resultList) {
		return resultList.stream()
				.map(objects -> new VillageDTO((String) objects[0], (String) objects[1], (String) objects[2],
						(String) objects[3], (String) objects[4], (String) objects[5],
						(objects[6] != null)?(String)objects[6].toString() :"",
						(objects[7] != null)?(String)objects[7].toString() : "", ((BigDecimal) objects[8]).setScale(4),
						(objects[9] != null) ? (String) objects[9].toString() : "",
						(objects[10] != null) ? (String) objects[10].toString() : "",
						(objects[11] != null) ? (String) objects[11].toString() : "",
						(objects[12] != null) ? (String) objects[12].toString() : "",
						(objects[13] != null) ? (String) objects[13].toString() : "",
						(objects[14] != null) ? (String) objects[14].toString() : ""))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> fetchLandUseVerificationDetails(String tahasilCode) {
		List<Object[]> resultList = new ArrayList<>();
		try {
			String nativeQuery = "select tps.plot_survey_id,d.district_code,d.district_name,tps.tahasil_code,t.tahasil_name,tps.village_code,v.village_name,\r\n"
					+ "tps.remarks,tps.plot_code,tps.plot_no,pi.kissam,k.khata_no,tps.khatian_code,pi.area_acre,tps.survey_date,\r\n"
					+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,\r\n"
					+"(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pi.plot_code) as coordinates "
					+ "from public.tahasil_plot_survey tps inner join land_bank.tahasil_master t on tps.tahasil_code = t.tahasil_code\r\n"
					+ "inner join land_bank.district_master d on t.district_code = d.district_code\r\n"
					+ "inner join land_bank.village_master v on tps.village_code = v.village_code\r\n"
					+ "left join land_bank.plot_information pi on tps.plot_code = pi.plot_code\r\n"
					+ "left join land_bank.khatian_information k on tps.khatian_code = k.khatian_code\r\n"
					+ "left join land_bank.sjta_plot_boundary pb on tps.plot_code = pb.plot_code\r\n"
					+ "where tps.tahasil_code =:tahasilCode and tps.remarks is not null order by  tps.survey_date desc";
			Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);
			log.info("query starts executing for fetchLandUseVerificationCompletedDetails");
			resultList = query.getResultList();
		}finally {
			entity.close();
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> tahasilPlotList(String tahasilCode, Integer pageNumber, Integer pageSize) {
		List<Object[]> resultList = new ArrayList<>();
		try {
			String nativeQuery = "select tps.plot_survey_id,tm.tahasil_code,tm.tahasil_name,pi.plot_code,pi.plot_no,\r\n"
					+ "tps.remarks,vm.village_code,vm.village_name,ki.khatian_code,ki.khata_no from land_bank.tahasil_master tm\r\n"
					+ "inner join land_bank.village_master vm on tm.tahasil_code = vm.tahasil_code\r\n"
					+ "left join land_bank.khatian_information ki on vm.village_code = ki.village_code\r\n"
					+ "inner join land_bank.plot_information pi on pi.khatian_code = ki.khatian_code\r\n"
					+ "left join public.tahasil_plot_survey tps on pi.plot_code = tps.plot_code\r\n"
					+ "where tm.tahasil_code =:tahasilCode group by tm.tahasil_code,tm.tahasil_name,tps.plot_survey_id,vm.village_code,\r\n"
					+ "vm.village_name,ki.khatian_code,ki.khata_no,pi.plot_code,pi.plot_no order by pi.plot_code asc LIMIT :pageSize  OFFSET :offset";
			int offset = (pageNumber -1)*pageSize;
			Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode).setParameter("pageSize", pageSize)
					.setParameter("offset", offset);
			log.info("query starts executing for tahasilPlotList");
			resultList = query.getResultList();
		}finally {
			entity.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public BigInteger countTahasilPlotList(String tahasilCode) {
		BigInteger result ;
	try {	
		String nativeQuery = "select count(*) from land_bank.tahasil_master tm\r\n"
				+ "inner join land_bank.village_master vm on tm.tahasil_code = vm.tahasil_code\r\n"
				+ "left join land_bank.khatian_information ki on vm.village_code = ki.village_code\r\n"
				+ "inner join land_bank.plot_information pi on pi.khatian_code = ki.khatian_code\r\n"
				+ "left join public.tahasil_plot_survey tps on pi.plot_code = tps.plot_code\r\n"
				+ "where tm.tahasil_code =:tahasilCode ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode);
		log.info("query starts executing for count of tahasilPlotList");
		result = (BigInteger)query.getSingleResult();
	}finally {
		entity.close();
	}
	return result;
	}
}
