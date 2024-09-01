package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.LandInformationReportRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandInformationReportRepositoryImpl implements LandInformationReportRepository {
	
	@PersistenceContext
	@Autowired
	private EntityManager entity;
	
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistrictReportData(Integer pagesize,Integer offset) {
		String sqlQuery = "SELECT dm.district_name, dm.district_code, " +
		        "COUNT(DISTINCT tm.tahasil_code) AS tahasilCount, " +
		        "COUNT(DISTINCT vm.village_code) AS villageCount, " +
		        "COUNT(DISTINCT ki.khatian_code) AS khataCount, " +
		        "COUNT(DISTINCT pi.plot_code) AS plotCount, " +
		        "CAST(SUM(area_acre) AS varchar)  as sumAreaAcer ,  "+
		        "CAST(ST_AsText(ST_Envelope(ST_Transform(ddb.geom, 3857))) as varchar) as extent  "+
//		        "CAST(st_extent(ST_Transform(ddb.geom,3857)) as varchar) as extent " +
		        "FROM land_bank.district_master dm " +
		        "JOIN land_bank.tahasil_master tm ON (tm.district_code = dm.district_code) " +
		        "JOIN land_bank.village_master vm ON (vm.tahasil_code = tm.tahasil_code) " +
		        "JOIN land_bank.khatian_information ki ON (vm.village_code = ki.village_code) " +
		        "JOIN land_bank.plot_information pi ON (pi.khatian_code = ki.khatian_code)  "+
		        "LEFT JOIN land_bank.district_boundary ddb ON(ddb.district_code=dm.district_code) " +
		        "GROUP BY dm.district_code,ddb.geom " +
		        "ORDER BY district_name OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", pagesize).setParameter("offset", offset);
		log.info("getDistrictReportData execution  success..!!");
		return query.getResultList();
	}
	
	
	
	@Transactional
	@Override
	public BigInteger getCountForDistrictReportData() {
		String sqlQuery = " SELECT COUNT(*) AS total_count FROM (  "
				+ "SELECT dm.district_name, dm.district_code,   "
				+ "COUNT(tm.tahasil_code) AS tahasilCount,  "
				+ "COUNT(vm.village_code) AS villagecount,  "
				+ "COUNT(ki.khatian_code) AS khataCount,  "
				+ "COUNT(pi.plot_code) AS plotCount,  "
				+ "SUM(pi.area_acre) AS sumAreaAcer  "
				+ "FROM land_bank.district_master dm  "
				+ "JOIN land_bank.tahasil_master tm ON (tm.district_code = dm.district_code)  "
				+ "JOIN land_bank.village_master vm ON (vm.tahasil_code = tm.tahasil_code)  "
				+ "JOIN land_bank.khatian_information ki ON (vm.village_code = ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON (pi.khatian_code = ki.khatian_code)  "
				+ "GROUP BY dm.district_code  "
				+ ") AS subquery ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		log.info("getCountForDistrictReportData data return success..!!");
		return result;
	}
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getTahasilReportData(Integer pagesize,Integer offset,String districtCode) {
		String sqlQuery = "  select tm.tahasil_name,tm.tahasil_code,count(DISTINCT vm.village_code) as villageCount,  "
				+ "count(DISTINCT ki.khatian_code) as khataCount,count(DISTINCT pi.plot_code) as plotCount,  "
				+ "CAST(SUM(area_acre) AS varchar)  as sumAreaAcer , "
				+ "CAST(st_extent(ST_Transform(tb.geom,3857)) as varchar) as extent  "
				+ "from land_bank.tahasil_master tm  "
				+ "JOIN land_bank.village_master vm ON(vm.tahasil_code=tm.tahasil_code)  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code)  "
				+ "JOIN land_bank.tahasil_boundary tb ON(tb.tahasil_code=tm.tahasil_code) "
				+ "WHERE tm.district_code=:districtCode   "
				+ "GROUP BY tm.tahasil_code ORDER BY tm.tahasil_name "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("limit", pagesize)
		.setParameter("offset", offset)
		.setParameter("districtCode", districtCode);
		log.info("getTahasilReportData data return success..!!");
		return query.getResultList();
	}
	
	
	@Transactional
	@Override
	public BigInteger getCountForTahasilReportData(String districtCode) {
		String sqlQuery = "SELECT COUNT(*) AS total_count from(  "
				+ "select tm.tahasil_name,tm.tahasil_code,count(vm.village_code) as villageCount,  "
				+ "count(ki.khatian_code) as khataCount,count(pi.plot_code) as plotCount,SUM(pi.area_acre) sumAreaAcer  "
				+ "from land_bank.tahasil_master tm  "
				+ "JOIN land_bank.village_master vm ON(vm.tahasil_code=tm.tahasil_code)  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code)  "
				+ "WHERE tm.district_code=:districtCode   "
				+ "GROUP BY tm.tahasil_code ORDER BY tm.tahasil_name) as subQuery";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("districtCode", districtCode);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		log.info("getCountForTahasilReportData execution  success..!!");
		return result;
	}
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getVillageReportData(Integer pagesize,Integer offset,String thasilCode) {
		String sqlQuery = "select vm.village_name,vm.village_code,count(DISTINCT ki.khatian_code) as khataCount,  "
				+ "count(DISTINCT pi.plot_code) as plotCount,"
				+ "CAST(SUM(area_acre) AS varchar)  as sumAreaAcer,   "
				+ " CAST(st_extent(ST_Transform(vb.geom,3857)) as varchar) as extent   "
				+ "from land_bank.village_master vm  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code) LEFT JOIN land_bank.village_boundary vb ON(vb.village_code=vm.village_code)  "
				+ "WHERE vm.tahasil_code=:thasilCode   " 
				+ "GROUP BY vm.village_code ORDER BY vm.village_name  "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("limit", pagesize)
		.setParameter("offset", offset)
		.setParameter("thasilCode", thasilCode);
		log.info("getVillageReportData execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	public BigInteger getCountForVillageReportData(String thasilCode) {
		String sqlQuery = " select count(*) from (select vm.village_name,vm.village_code,count(ki.khatian_code) as khataCount,  "
				+ "count(pi.plot_code) as plotCount,SUM(pi.area_acre) sumAreaAcer  "
				+ "from land_bank.village_master vm  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code)  "
				+ "WHERE vm.tahasil_code=:thasilCode   "
				+ "GROUP BY vm.village_code ORDER BY vm.village_name) subQuery ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("thasilCode", thasilCode);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		log.info("getCountForVillageReportData execution  success..!!");
		return result;
	}
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getKhataReportData(Integer pagesize,Integer offset,String villageCode) {
		String sqlQuery = "select ki.khata_no,ki.khatian_code, count(DISTINCT pi.plot_code) as plotCount,  "
				+ "CAST(SUM(pi.area_acre) AS varchar) ,  "
//				+ "(select CAST(SUM(area_acre) AS varchar) from land_bank.plot_information where khatian_code=ki.khatian_code ) as sumAreaAcer,  "
				+ "ki.owner_name ,ki.marfatdar_name ,ki.sotwa ,ki.publication_date , dm.district_code,tm.tahasil_code,vm.village_code,  "
				+ "CAST(st_extent(ST_Transform(geom,3857)) as varchar) as extent  "
				+ "from land_bank.khatian_information ki   "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code) "
				+ "JOIN land_bank.village_master vm ON(vm.village_code=ki.village_code) "
				+ "JOIN land_bank.tahasil_master tm ON(vm.tahasil_code=tm.tahasil_code) "
				+ "JOIN land_bank.district_master dm ON(tm.district_code=dm.district_code)  "
				+ "LEFT JOIN land_bank.sjta_plot_boundary_view spb ON(spb.plot_code=pi.plot_code)  "
				+ "WHERE ki.village_code=:thasilCode   " 
				+ "GROUP BY ki.khatian_code , dm.district_code,tm.tahasil_code,vm.village_code  "
				+ "ORDER BY ki.khata_no  "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("limit", pagesize)
		.setParameter("offset", offset)
		.setParameter("thasilCode", villageCode);
		log.info("getKhataReportData execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	public BigInteger getCountForKahtaReportData(String villageCode) {
		String sqlQuery = "select count(*) from (select ki.khata_no,ki.khatian_code, count(DISTINCT pi.plot_code) as plotCount,SUM(DISTINCT pi.area_acre) sumAreaAcer,  "
				+ "ki.owner_name ,ki.marfatdar_name ,ki.sotwa ,ki.publication_date , dm.district_code,tm.tahasil_code,vm.village_code,  "
				+ "CAST(st_extent(ST_Transform(geom,3857)) as varchar) as extent  "
				+ "from land_bank.khatian_information ki   "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code) "
				+ "JOIN land_bank.village_master vm ON(vm.village_code=ki.village_code) "
				+ "JOIN land_bank.tahasil_master tm ON(vm.tahasil_code=tm.tahasil_code) "
				+ "JOIN land_bank.district_master dm ON(tm.district_code=dm.district_code)  LEFT JOIN land_bank.sjta_plot_boundary_view spb ON(spb.village_code=ki.khatian_code)  "
				+ "WHERE ki.village_code=:thasilCode   " 
				+ "GROUP BY ki.khatian_code , dm.district_code,tm.tahasil_code,vm.village_code  "
				+ "ORDER BY ki.khata_no) as subquery  ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("thasilCode", villageCode);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		log.info("getCountForKahtaReportData execution  success..!!");
		return result;
	}
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPlotReportData(Integer pagesize,Integer offset,String khataCode) {
		String sqlQuery = "select pi.plot_no,pi.plot_code,kissam, CAST(area_acre as varchar) , dm.district_code,tm.tahasil_code,vm.village_code,ki.khatian_code,ki.khata_no ,"
				+ "CAST(st_extent(ST_Transform(spb.geom,3857)) as varchar) as extent  "
				+ "from  land_bank.plot_information pi "
				+ " JOIN land_bank.khatian_information ki ON(ki.khatian_code=pi.khatian_code) "
				+ "JOIN land_bank.village_master vm ON(vm.village_code=ki.village_code) "
				+ "JOIN land_bank.tahasil_master tm ON(vm.tahasil_code=tm.tahasil_code) "
				+ "JOIN land_bank.district_master dm ON(tm.district_code=dm.district_code) LEFT JOIN land_bank.sjta_plot_boundary spb ON(spb.plot_code=pi.plot_code)   "
				+ "WHERE pi.khatian_code=:khataCode   " 
				+ "GROUP BY pi.plot_code , dm.district_code,tm.tahasil_code,vm.village_code,ki.khatian_code  "
				+ "ORDER BY pi.plot_no  "
				+ "OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("limit", pagesize)
		.setParameter("offset", offset)
		.setParameter("khataCode", khataCode);
		log.info("getPlotReportData execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	public BigInteger getCountForPlotReportData(String khataCode) {
		String sqlQuery = " select count(*) from (select pi.plot_no,pi.plot_code,kissam, area_acre   "
				+ "from  land_bank.plot_information pi   "
				+ "WHERE pi.khatian_code=:khataCode   "
				+ "GROUP BY pi.plot_code ORDER BY pi.plot_no) subquery ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("khataCode", khataCode);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		entity.close();
		log.info("getCountForPlotReportData execution  success..!!");
		return result;
	}

	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistrictReportDataForReport() {
		String sqlQuery = "SELECT dm.district_name, dm.district_code, " +
		        "COUNT(DISTINCT tm.tahasil_code) AS tahasilCount, " +
		        "COUNT(DISTINCT vm.village_code) AS villageCount, " +
		        "COUNT(DISTINCT ki.khatian_code) AS khataCount, " +
		        "COUNT(DISTINCT pi.plot_code) AS plotCount, " +
		        "CAST(SUM(pi.area_acre) as varchar) AS sumAreaAcer " +
		        "FROM land_bank.district_master dm " +
		        "JOIN land_bank.tahasil_master tm ON (tm.district_code = dm.district_code) " +
		        "JOIN land_bank.village_master vm ON (vm.tahasil_code = tm.tahasil_code) " +
		        "JOIN land_bank.khatian_information ki ON (vm.village_code = ki.village_code) " +
		        "JOIN land_bank.plot_information pi ON (pi.khatian_code = ki.khatian_code) " +
		        "GROUP BY dm.district_code " +
		        "ORDER BY district_name  ";
		Query query = entity.createNativeQuery(sqlQuery);
		log.info("getDistrictReportDataForReport execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getTahasilReportDataForReport(String districtCode) {
		String sqlQuery = "  select tm.tahasil_name,tm.tahasil_code,count(DISTINCT vm.village_code) as villageCount,  "
				+ "count(DISTINCT ki.khatian_code) as khataCount,count(DISTINCT pi.plot_code) as plotCount,CAST(SUM(pi.area_acre)as varchar) sumAreaAcer  "
				+ "from land_bank.tahasil_master tm  "
				+ "JOIN land_bank.village_master vm ON(vm.tahasil_code=tm.tahasil_code)  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code)  "
				+ "WHERE tm.district_code=:districtCode   "
				+ "GROUP BY tm.tahasil_code ORDER BY tm.tahasil_name ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("districtCode", districtCode);
		log.info("getTahasilReportDataForReport execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getVillageReportDataForReport(String thasilCode) {
		String sqlQuery = "select vm.village_name,vm.village_code,count(DISTINCT ki.khatian_code) as khataCount,  "
				+ "count(DISTINCT pi.plot_code) as plotCount,CAST(SUM(pi.area_acre)as varchar) sumAreaAcer  "
				+ "from land_bank.village_master vm  "
				+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code)  "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code)  "
				+ "WHERE vm.tahasil_code=:thasilCode   " 
				+ "GROUP BY vm.village_code ORDER BY vm.village_name  ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("thasilCode", thasilCode);
		log.info("getVillageReportDataForReport execution  success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getKhataReportDataForReport(String villageCode) {
		String sqlQuery = "select ki.khata_no,ki.khatian_code, count(DISTINCT pi.plot_code) as plotCount,  "
				+ "CAST(SUM(pi.area_acre) AS varchar) ,  "
//				+ "(select CAST(SUM(area_acre) AS varchar) from land_bank.plot_information where khatian_code=ki.khatian_code ) as sumAreaAcer,  "
				+ "ki.owner_name ,ki.marfatdar_name ,ki.sotwa ,ki.publication_date , dm.district_code,tm.tahasil_code,vm.village_code,  "
				+ "CAST(st_extent(ST_Transform(geom,3857)) as varchar) as extent  "
				+ "from land_bank.khatian_information ki   "
				+ "JOIN land_bank.plot_information pi ON(pi.khatian_code=ki.khatian_code) "
				+ "JOIN land_bank.village_master vm ON(vm.village_code=ki.village_code) "
				+ "JOIN land_bank.tahasil_master tm ON(vm.tahasil_code=tm.tahasil_code) "
				+ "JOIN land_bank.district_master dm ON(tm.district_code=dm.district_code)  "
				+ "  JOIN land_bank.sjta_plot_boundary_view spb ON(spb.plot_code=pi.plot_code)  "
				+ "WHERE ki.village_code=:thasilCode   " 
				+ "GROUP BY ki.khatian_code , dm.district_code,tm.tahasil_code,vm.village_code  "
				+ "ORDER BY ki.khata_no  ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("thasilCode", villageCode);
		log.info("getKhataReportDataForReport execution  success..!!");
		return query.getResultList();
	}
	
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPlotReportDataForReport(String khataCode) {
		String sqlQuery = "select pi.plot_no,pi.plot_code,kissam, CAST(area_acre as varchar) as  area_acre  "
				+ "from  land_bank.plot_information pi   "
				+ "WHERE pi.khatian_code=:khataCode   " 
				+ "GROUP BY pi.plot_code ORDER BY pi.plot_no ";
		Query query = entity.createNativeQuery(sqlQuery)
		.setParameter("khataCode", khataCode);
		log.info("getPlotReportDataForReport execution  success..!!");
		return query.getResultList();
	}



	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public String ReverseRecordForDistrict(String districtCode) {
       String sql="select dm.district_name from land_bank.district_master dm WHERE dm.district_code=:districtCode";
       Query query = entity.createNativeQuery(sql)
    			.setParameter("districtCode", districtCode);
       log.info("ReverseRecordForDistrict execution  success..!!");
    			return (String) query.getSingleResult();
	}


	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> ReverseRecordForTahasil(String tahasilCode) {
		 String sql=" select dm.district_name,tm.tahasil_name from land_bank.district_master dm "
		 		+ "JOIN land_bank.tahasil_master tm ON(tm.district_code=dm.district_code) WHERE tm.tahasil_code=:tahasilCode ";
	       Query query = entity.createNativeQuery(sql)
	    			.setParameter("tahasilCode", tahasilCode);
	       log.info("ReverseRecordForTahasil execution  success..!!");
	    			return query.getResultList();
	}


	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> ReverseRecordForVillage(String villageCode) {
		 String sql="select dm.district_name,tm.tahasil_name,vm.village_name  from land_bank.district_master dm "
		 		+ "JOIN land_bank.tahasil_master tm ON(tm.district_code=dm.district_code)  "
		 		+ "JOIN land_bank.village_master vm ON(vm.tahasil_code=tm.tahasil_code) "
		 		+ "WHERE vm.village_code=:villageCode ";
	       Query query = entity.createNativeQuery(sql)
	    			.setParameter("villageCode", villageCode);
	       log.info("ReverseRecordForVillage execution  success..!!");
	    			return query.getResultList();
	}


	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> ReverseRecordForKhata(String khataCode) {
		 String sql=" select dm.district_name,tm.tahasil_name,vm.village_name,ki.khata_no  from land_bank.district_master dm "
		 		+ "JOIN land_bank.tahasil_master tm ON(tm.district_code=dm.district_code)  "
		 		+ "JOIN land_bank.village_master vm ON(vm.tahasil_code=tm.tahasil_code) "
		 		+ "JOIN land_bank.khatian_information ki ON(vm.village_code=ki.village_code) "
		 		+ "WHERE ki.khatian_code=:khataCode ";
	       Query query = entity.createNativeQuery(sql)
	    			.setParameter("khataCode", khataCode);
	       log.info("ReverseRecordForKhata  execution  success..!!");
	    			return query.getResultList();
	}
	
	
}
