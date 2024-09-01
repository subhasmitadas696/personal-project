package com.csmtech.sjta.mobile.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.mobile.entity.TahasilPlotEntity;

@Repository
public interface TahasilPlotSurveyRepository extends JpaRepository<TahasilPlotEntity, Integer> {

	TahasilPlotEntity findTopByOrderByPlotSurveyIdDesc();
	
	@Query(value ="select t.plot_survey_id,t.tahasil_code,t.remarks,t.plot_no,t.survey_date,t.village_code,t.khatian_code,t.plot_code "
			+ "from tahasil_plot_survey t where t.tahasil_code =:tahasilCode and t.remarks is null and t.status = '0' order by t.",nativeQuery  = true )
	List<Object[]> findByNullRemarks(String tahasilCode);
	
	@Query(value ="select t.plot_survey_id,t.tahasil_code,t.remarks,t.plot_no,t.survey_date,t.village_code,t.khatian_code,t.plot_code "
			+ "from tahasil_plot_survey t where t.tahasil_code =:tahasilCode and t.remarks is not null and t.status = '0'",nativeQuery  = true )
	List<Object[]> findByRemarks(String tahasilCode);

	@Query(value ="select t.plot_survey_id,t.tahasil_code,t.remarks,t.plot_no,t.survey_date,t.village_code,t.khatian_code,t.plot_code "
			+ "from tahasil_plot_survey t where t.tahasil_code =:tahasilCode and t.remarks =:remarks and t.plot_no =:plotNo and t.plot_code =:plotCode and t.status = '0'",nativeQuery  = true )
	TahasilPlotEntity findByLatestEntry(String tahasilCode, String remarks, String plotNo, String plotCode);

	@Query(value ="select count(t.plot_code) from tahasil_plot_survey t where t.plot_code =:plotCode and t.status = '0'",nativeQuery = true)
	BigInteger findDuplicateRecordCount(String plotCode);
	
//	TahasilPlotEntity findByPlotSurveyId(Integer plotSurveyId);
//	TahasilPlotEntity findByTahasilCode(String tahasilCode); findFirstByOrderByScoreDesc()
	
//	@Query("select t.plot_survey_id,t.tahasil_code,t.remarks,t.plot_no,t.survey_date,t.village_code,t.khatian_code "
//			+ "from TahasilPlotEntity t where t.tahasil_code =:tahasilCode and t.remarks =:remarks and t.plot_no =:plotNo "
//			+ "and t.survey_date =:surveyDate and t.village_code =:villageCode and t.khatian_code =:khatianCode and t.status = '0'")
	
//	@Query("")
//	TahasilPlotEntity findSingleRowRecord(String tahasilCode,String remarks, String plotNo, Date surveyDate, String villageCode,String khatianCode);
	
//	TahasilPlotDto tahasilPlotDto;

}
