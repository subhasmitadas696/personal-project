package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.mobile.entity.PlotLandInspectionEntity;

/**
 * @author prasanta.sethi
 */
@Repository
public interface ViewAssignedDetailsRepository extends JpaRepository<PlotLandInspectionEntity, Integer> {
	@Query(value = "select pl.plot_land_inspection_id,pl.district_code, pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,"
			+ " pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,pl.created_datetime,pl.scheduled_inspection_date, pi.plot_no,"
			+ " ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre, pl.tahasil_remarks, pl.tahasil_uploaded_photo from application.plot_land_inspection pl"
			+ " inner join land_bank.cadastral_plot_boundary pb on pl.plot_code = pb.plot_code left join land_bank.district_master md"
			+ " on pl.district_code = md.district_code left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ " left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code left join land_bank.village_master v "
			+ " on pl.village_code = v.village_code left join land_bank.plot_information pi on pl.plot_code = pi.plot_code where pl.deleted_status = '0' ORDER BY pl.scheduled_inspection_date desc ", nativeQuery = true)
	List<Object[]> getAssignedDetails();
}
