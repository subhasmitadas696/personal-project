package com.csmtech.sjta.mobile.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.csmtech.sjta.mobile.entity.PlotLandInspectionEntity;

public interface LandInspectionRepository extends JpaRepository<PlotLandInspectionEntity, Integer> {

	@Modifying
	@Transactional
	@Query(value = "INSERT into application.plot_land_inspection(district_code,tahasil_code,village_code,khatian_code,plot_code,scheduled_inspection_date)"
			+ "VALUES(:districtCode,:tahasilCode,:villageCode,:khatianCode,:plotCode,:scheduledInspectionDate)", nativeQuery = true)
	public int updateAssignStatusToCO(String districtCode, String tahasilCode, String villageCode, String khatianCode,
			String plotCode, Date scheduledInspectionDate);

	@Query(value = "SELECT plot_land_inspection_id FROM application.plot_land_inspection ORDER BY plot_land_inspection_id DESC LIMIT 1", nativeQuery = true)
	public Object getLastInsertId();

	@Modifying
	@Transactional
	@Query(value = "INSERT into application.land_inspection_application(plot_land_inspection_id,land_application_id, created_by)"
			+ "VALUES(:lastInsertId, :appId, :createdBy)", nativeQuery = true)
	public int updateLandAppToCO(BigInteger lastInsertId, BigInteger appId, BigInteger createdBy);

	@Query(value = "SELECT COUNT(*) FROM application.land_inspection_application WHERE plot_land_inspection_id = :lastInsertId AND land_application_id = :appId ", nativeQuery = true)
	public Object checkDuplicateData(BigInteger lastInsertId, BigInteger appId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE application.plot_land_inspection SET co_remarks =:coRemarks,co_uploaded_photo =:coUploadedPhoto,"
			+ "inspection_date =:inspectionDate,latitude =:latitude,longitude =:longitude,approve_status =:status "
			+ "where plot_land_inspection_id =:plotLandInspectionId", nativeQuery = true)
	public int coSubmitInspection(String coRemarks, String coUploadedPhoto, Date inspectionDate,
			Integer plotLandInspectionId, String latitude, String longitude, Integer status);

	@Query(value = "select * from application.plot_land_inspection where plot_code =:plotCode", nativeQuery = true)
	public PlotLandInspectionEntity findByPlotCode(String plotCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,"
			+ "pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,"
			+ "pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.co_remarks is not null and pl.deleted_status = '0' order by pl.inspection_date desc", nativeQuery = true)
	public List<Object[]> findByCoRemarks();

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,"
			+ "pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,"
			+ "pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.co_remarks is null and pl.village_code =:villageCode and pl.deleted_status = '0' order by pl.scheduled_inspection_date", nativeQuery = true)
	public List<Object[]> findByNullCoRemarks(String villageCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,"
			+ "pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,"
			+ "pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.co_remarks is null and pl.deleted_status = '0' order by pl.scheduled_inspection_date", nativeQuery = true)
	public List<Object[]> findByNullCoRemarks();

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,"
			+ "pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,"
			+ "pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,pl.created_datetime,pl.scheduled_inspection_date,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,"
			+ "pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.plot_land_inspection_id =:plotLandInspectionId and pl.deleted_status = '0' order by pl.inspection_date desc", nativeQuery = true)
	public List<Object[]> viewInspectionDetails(Integer plotLandInspectionId);

	@Query(value = "select pl.village_code,v.village_name,COUNT(pl.plot_code) as total_plot from application.plot_land_inspection pl "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code where pl.co_remarks is null and "
			+ "pl.deleted_status = '0' group by pl.village_code,v.village_name ", nativeQuery = true)
	public List<Object[]> getVillageInformation(Integer plotLandInspectionId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE application.plot_land_inspection SET tahasil_remarks =:tahasilRemarks,tahasil_uploaded_photo =:fileName,"
			+ "tahasil_latitude =:tahasilLatitude,tahasil_longitude =:tahasilLongitude,tahasildar_inspected_by =:tahasildarInspectedBy,tahasildar_inspection_date =:tahasildarInspectionDate,tahasil_status =:status "
			+ "where plot_land_inspection_id =:plotLandInspectionId", nativeQuery = true)
	public int saveTahasildarPlotAction(String tahasilRemarks, String fileName, Integer plotLandInspectionId,
			String tahasilLatitude, String tahasilLongitude, Integer tahasildarInspectedBy,
			Date tahasildarInspectionDate, Integer status);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,"
			+ "pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,"
			+ "pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.tahasil_remarks is not null and pl.tahasil_code =:tahasilCode and pl.deleted_status = '0' order by pl.tahasildar_inspection_date desc", nativeQuery = true)
	public List<Object[]> findByTahasildarRemarks(String tahasilCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,"
			+ "pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,"
			+ "pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.tahasil_remarks is null and pl.tahasil_code =:tahasilCode and pl.deleted_status = '0' order by pl.scheduled_inspection_date", nativeQuery = true)
	public List<Object[]> findByTahasildarNullRemarks(String tahasilCode);

	@Query(value = "select pl.village_code,v.village_name,COUNT(pl.plot_code) as total_plot from application.plot_land_inspection pl "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code where pl.tahasil_remarks is null and pl.tahasil_code =:tahasilCode and "
			+ "pl.deleted_status = '0' group by pl.village_code,v.village_name ", nativeQuery = true)
	public List<Object[]> getVillageInformationForTahasil(String tahasilCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,"
			+ "pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,"
			+ "pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date "
			+ "from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on "
			+ "pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code "
			+ "left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code "
			+ "left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code "
			+ "left join land_bank.village_master v on pl.village_code = v.village_code "
			+ "left join land_bank.plot_information pi on pl.plot_code = pi.plot_code "
			+ "where pl.tahasil_remarks is null and pl.tahasil_code =:tahasilCode and pl.village_code =:villageCode and pl.deleted_status = '0' order by pl.scheduled_inspection_date", nativeQuery = true)
	public List<Object[]> findByTahasildarNullRemarks(String villageCode, String tahasilCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,\r\n"
			+ " pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,\r\n"
			+ " (select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,\r\n"
			+ " pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,\r\n"
			+ " pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date,ki.marfatdar_name,ki.sotwa,ls.total_area,ls.purchase_area,la.applicant_name,la.application_no\r\n"
			+ " from application.plot_land_inspection pl inner join land_bank.cadastral_plot_boundary pb on \r\n"
			+ " pl.plot_code = pb.plot_code left join land_bank.district_master md on pl.district_code = md.district_code \r\n"
			+ " left join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code \r\n"
			+ " left join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code \r\n"
			+ " left join land_bank.village_master v on pl.village_code = v.village_code \r\n"
			+ " left join land_bank.plot_information pi on pl.plot_code = pi.plot_code \r\n"
			+ " left join public.land_schedule ls on pl.plot_code = ls.plot_code\r\n"
			+ " left join public.land_application la on ls.land_application_id = la.land_application_id\r\n"
			+ "  where pl.tahasil_remarks is not null and pl.tahasil_code =:tahasilCode and pl.deleted_status = '0' order by pl.tahasildar_inspection_date desc", nativeQuery = true)

	public List<Object[]> viewTahasildarInspectionDetailsWeb(String tahasilCode);

	@Query(value = "select pl.plot_land_inspection_id,pl.district_code,pl.tahasil_code,pl.village_code,pl.khatian_code,pl.plot_code,pl.co_remarks,pl.co_uploaded_photo,pl.inspection_date,  \r\n"
			+ "pl.created_datetime,pl.latitude,pl.longitude,pl.scheduled_inspection_date,ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,  \r\n"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pl.plot_code) as coordinates,  \r\n"
			+ "pi.plot_no,ki.khata_no,md.district_name as districtName,t.tahasil_name,v.village_name,pi.area_acre,  \r\n"
			+ "pl.tahasil_remarks,pl.tahasil_latitude,pl.tahasil_longitude,pl.tahasildar_inspected_by,pl.tahasil_uploaded_photo,pl.tahasildar_inspection_date,ki.marfatdar_name,ki.sotwa,ls.total_area,ls.purchase_area,la.applicant_name,la.application_no  \r\n"
			+ "from application.plot_land_inspection pl \r\n"
			+ "join land_bank.cadastral_plot_boundary pb on pl.plot_code = pb.plot_code \r\n"
			+ "join land_bank.district_master md on pl.district_code = md.district_code   \r\n"
			+ "join land_bank.tahasil_master t on pl.tahasil_code = t.tahasil_code   \r\n"
			+ "join land_bank.khatian_information ki on pl.khatian_code = ki.khatian_code   \r\n"
			+ "join land_bank.village_master v on pl.village_code = v.village_code   \r\n"
			+ "join land_bank.plot_information pi on pl.plot_code = pi.plot_code   \r\n"
			+ "join land_schedule ls on pl.plot_code = ls.plot_code  \r\n"
			+ "join land_application la on ls.land_application_id = la.land_application_id AND la.deleted_flag = '0' AND la.app_status != 0  \r\n"
			+ "where pl.tahasil_remarks is not null and pl.plot_code = :plotCode and pl.deleted_status = '0' order by pl.plot_land_inspection_id", nativeQuery = true)
	public List<Object[]> viewTahasildarValuationForm(String plotCode);
	
	@Query(value = "select pv.plot_code, pv.khatian_code, available_land, land_owner_name, land_address, lu.land_use, tenure, ownership_record, ownership_record_doc, price_per_acre, total_price, ki.khata_no, pi.plot_no  from application.plot_valuation pv JOIN land_bank.khatian_information ki using(khatian_code) "
			+ " JOIN land_bank.plot_information pi using(plot_code) JOIN public.m_land_use lu ON pv.type_of_land_use = lu.land_use_id where plot_code =:plotCode", nativeQuery = true)
	public List<Object[]> viewTahasildarValuationFormByplotCode(String plotCode);

	@Query(value = "select distinct la.land_application_id FROM public.land_application la inner join public.land_schedule ls on "
			+ "la.land_application_id = ls.land_application_id inner join application.plot_land_inspection pl on "
			+ "ls.plot_code = pl.plot_code where pl.plot_code =:plotCode  and la.land_application_id in "
			+ "(select land_application_id from application.application_flow)", nativeQuery = true)
	public List<Object[]> fetchLandApplicationIDs(String plotCode);
	
	@Query(value = "select u.fcm_token from user_details u inner join user_role ur on u.user_id = ur.user_id where ur.role_id = 11 and u.status = '0'", nativeQuery = true)
	public String fetchFcmToken();
}
