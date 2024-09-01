//package com.csmtech.sjta.mobile.repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Repository
//@Slf4j
//public class PlotLandInspectionRepository {
//	
//	@PersistenceContext
//	@Autowired
//	private EntityManager entity;
//	
//	public List<PlotLandInspectionDto> findByCoRemarks(String type){
//		List<PlotLandInspectionDto> dtoList = new ArrayList<>();
//		String nativeQuery ="select district_code,tahasil_code,village_code,khatian_code,plot_code";
//		if(type.equalsIgnoreCase("P")) {
//			nativeQuery += " ORDER BY schedule_inspection_date DESC ";
//		} else if (type.equalsIgnoreCase("C")) {
//			nativeQuery += " ORDER BY inspection_date ASC ";
//		}
//		Query query = entity.createNativeQuery(nativeQuery);//.setParameter("grievanceStatus", grievanceStatus);
//		try {
//			log.info("query starts executing");
//			@SuppressWarnings("unchecked")
//			List<Object[]> resultList = query.getResultList();
//			log.info("result fetched from db: " + resultList);
//			if (resultList != null && resultList.size() > 0) {
//				grievanceList = mapToGrievanceDto(resultList);
//				log.info("result fetched from db in dto: " + grievanceList);
//			} else {
//				return grievanceList;
//			}
//
//			return grievanceList;
//
//		} finally {
//			entity.close();
//		}
//
//	}
//	
//}
