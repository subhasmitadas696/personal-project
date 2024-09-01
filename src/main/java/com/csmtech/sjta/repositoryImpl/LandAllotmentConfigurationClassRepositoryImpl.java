package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAllotmentConfigurationDTO;
import com.csmtech.sjta.repository.LandAllotmentConfigurationClassRepository;

@Repository
public class LandAllotmentConfigurationClassRepositoryImpl implements LandAllotmentConfigurationClassRepository {
	
	@PersistenceContext
	@Autowired
	private EntityManager entity;
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<LandAllotmentConfigurationDTO> getAllRecord(Integer pageRequest){
		String queryStr=" SELECT land_allotment_configuration_id as landId, "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as selDistrictName,  "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as selTehsilName,   "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as selMouza,    "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as selKhataNo , "
				+ " (select plot_no from land_bank.plot_information where plot_code=ap.plot_code) as selPlotNo, "
				+ "  ap.total_amount as txtTotalRakba "
				+ "  FROM application.land_allotment_configuration ap   "
				+ "  WHERE deleted_flag='0' "
				+ "  limit :limit ";
		Query query = entity.createNativeQuery(queryStr,LandAllotmentConfigurationDTO.class).setParameter("limit", pageRequest);
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<LandAllotmentConfigurationDTO> getAllRecordById(BigInteger landId) {
		String queryStr=" SELECT land_allotment_configuration_id as landId, "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as selDistrictName,  "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as selTehsilName,   "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as selMouza,    "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as selKhataNo , "
				+ " (select plot_no from land_bank.plot_information where plot_code=ap.plot_code) as selPlotNo, "
				+ "  ap.total_amount as txtTotalRakba "
				+ "  FROM application.land_allotment_configuration ap  "
				+ "  WHERE deleted_flag='0' AND "
				+ "  ap.land_allotment_configuration_id = :landId ";
		Query query = entity.createNativeQuery(queryStr,LandAllotmentConfigurationDTO.class).setParameter("landId", landId);
		return  query.getResultList();
	}
	
	@Transactional
	@Override
	public Integer updateLandAllotmentConfiguration(BigInteger landId) {
	    String nativeQuery = "UPDATE application.land_allotment_configuration "
	            + "SET deleted_flag = B'1' "
	            + "WHERE land_allotment_configuration_id = :landId";

	    return entity.createNativeQuery(nativeQuery)
	            .setParameter("landId", landId)
	            .executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<LandAllotmentConfigurationDTO> findLandAllotmentConfigurationById(BigInteger landId) {
	    String nativeQuery = "SELECT  "
	            + "land_allotment_configuration_id  as landId, "
	            + "district_code as selDistrictName, "
	            + "tahasil_code as selTehsilName , "
	            + "village_code as selMouza, "
	            + "khatian_code as selKhataNo , "
	            + "plot_code as selPlotNo , "
	            + "total_amount as txtTotalRakba "
	            + "FROM application.land_allotment_configuration "
	            + "WHERE land_allotment_configuration_id = :landId";

	    return entity.createNativeQuery(nativeQuery, LandAllotmentConfigurationDTO.class)
	            .setParameter("landId", landId)
	            .getResultList();
	}

}
