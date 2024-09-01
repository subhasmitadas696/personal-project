package com.csmtech.sjta.mobile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.mobile.entity.TahasilPlotSurveyDetailsEntity;

@Repository
public interface TahasilPlotSurveyDetailsRepository extends JpaRepository<TahasilPlotSurveyDetailsEntity, Integer>{

	@Query(value= "select tpsd.plot_survey_id,tpsd.land_type_status_id,lt.land_type_status,tpsd.land_use_id,\r\n"
			+ "lu.land_use,tpsd.build_up_type_name,tpsd.latitude,tpsd.longitude,tpsd.image\r\n"
			+ "from public.tahasil_plot_survey_details tpsd left join public.m_land_type_status lt \r\n"
			+ "on tpsd.land_type_status_id = lt.land_type_status_id \r\n"
			+ "left join public.m_land_use lu on tpsd.land_use_id = lu.land_use_id where tpsd.plot_survey_id =:plotSurveyId",nativeQuery = true)
	List<Object[]> findByPlotSurveyId(Integer plotSurveyId);

}
