package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.csmtech.sjta.entity.Land_applicant;

public interface LandApplicantJPARepository extends JpaRepository<Land_applicant, Integer> {
	@Query(value = "select * From land_application where deleted_flag ='0' and land_application_id =:bigInsteger", nativeQuery = true)
	Land_applicant findByIntId(Integer bigInsteger);
	
	@Query(value = "select land_application_id,application_no,created_by From land_application where deleted_flag ='0' and land_application_id =:bigInteger", nativeQuery = true)
	Land_applicant findById(BigInteger bigInteger);

	@Query(value = "select * From land_application where deleted_flag='0' ", nativeQuery = true)
	List<Land_applicant> findAllByBitDeletedFlag();

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE land_application SET assign_status =:status where land_application_id =:applicantId", nativeQuery = true)
//	public int updateAssignStatusToCO(Integer status, Integer applicantId);

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE land_application SET co_remarks =:coRemarks,co_uploaded_photo =:coUploadedPhoto,"
//			+ "inspection_date =:inspectionDate,latitude =:latitude,longitude =:longitude where plot_land_inspection_id =:plotLandInspectionId", nativeQuery = true)
//	public int coSubmitInspection(String coRemarks, String coUploadedPhoto, Date inspectionDate, Integer plotLandInspectionId,String latitude,String longitude);
	
//	@Query(value = "select * From land_application where assign_status =:assignStatus and deleted_flag ='0'", nativeQuery = true)
//	List<Land_applicant> findByAssignStatus(Integer assignStatus);
}
