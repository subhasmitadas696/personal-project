package com.csmtech.sjta.mobile.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csmtech.sjta.mobile.entity.LandPostAllotmentEntity;

@Repository
public interface LandPostAllotmentRepository extends JpaRepository<LandPostAllotmentEntity, Integer> {

	@Query(value = " select count(co_remarks) From application.post_allotment_inspection  where deleted_status='0' group by inspection_date order by inspection_date desc ", nativeQuery = true)
	Integer fetchCoRemarksCount();

	@Query(value = "select count(*) FROM application.post_allotment_inspection where co_remarks is null and deleted_status = '0' group by scheduled_inspection_date order by scheduled_inspection_date asc", nativeQuery = true)
	Integer fetchNullCoRemarksCount();

	@Query(value = "select * from application.post_allotment_inspection where plot_code =:plotCode and scheduled_inspection_date =:scheduledInspectionDate and deleted_status ='0'", nativeQuery = true)
	LandPostAllotmentEntity findByPlotCodeAndScheduledInspectionDate(String plotCode, Date scheduledInspectionDate);

	@Modifying
	@Transactional
	@Query(value = "INSERT into application.post_allotment_inspection(district_code,tahasil_code,village_code,khatian_code,plot_code,scheduled_inspection_date, lo_remarks,"
			+ " created_by, assign_status,total_area,purchase_area,price_per_acre,total_price_in_purchase_area)"
			+ "VALUES(:districtCode,:tahasilCode,:villageCode,:khatianCode,:plotCode,:scheduledInspectionDate,:loRemarks,:createdBy,:status,:totalArea,:purchaseArea,:pricePerAcre,:totalPrice)", nativeQuery = true)

	int updateAssignStatusToCO(String districtCode, String tahasilCode, String villageCode, String khatianCode,
			String plotCode, Date scheduledInspectionDate, String loRemarks, BigInteger createdBy, Integer status,
			BigDecimal totalArea, BigDecimal purchaseArea, BigDecimal pricePerAcre, BigDecimal totalPrice);

	@Query(value = "select distinct la.district_code,dm.district_name,la.tehsil_code,tm.tahasil_name,la.village_code,"
			+ "vm.village_name,la.khatian_code,ki.khata_no,laa.plot_code,pi.plot_no,laa.total_area,laa.purchase_area,"
			+ "laa.price_per_acer,laa.total_price_in_purchase_area "
			+ "from application.land_allotement laa inner join public.land_application la "
			+ "on laa.land_application_id = la.land_application_id "
			+ "inner join public.land_schedule ls on laa.land_application_id = ls.land_application_id "
			+ "inner join land_bank.district_master dm on la.district_code = dm.district_code "
			+ "inner join land_bank.tahasil_master tm on la.tehsil_code = tm.tahasil_code "
			+ "inner join land_bank.village_master vm on la.village_code = vm.village_code "
			+ "inner join land_bank.khatian_information ki on la.khatian_code = ki.khatian_code "
			+ "inner join land_bank.plot_information pi on laa.plot_code = pi.plot_code "
			+ "where laa.form_16_flag ='1' or laa.form_register_flag = '1' and laa.deleted_flag = '0' ", nativeQuery = true)
	List<Object[]> fetchPrePostAllotmentList();

	@Query(value = "select v.village_code, v.village_name,(SELECT COUNT(plot_code) FROM land_bank.plot_information "
			+ "where khatian_code in (select khatian_code from land_bank.khatian_information "
			+ "where village_code = v.village_code) and plot_code in "
			+ "(select plot_code from application.post_allotment_inspection where co_remarks is null)) as total_plot "
			+ "from land_bank.village_master v  group by v.village_code,v.village_name", nativeQuery = true)
	List<Object[]> getVillageDetails(String tahasilCode); // where v.tahasil_code =:tahasilCode

	@Query(value = "SELECT v.village_code,v.village_name,t.tahasil_code,t.tahasil_name,d.district_name,"
			+ "p.plot_no,p.kissam,k.khata_no,total_area.total_area AS sum_area_acre,"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centrallatitude,ST_X(ST_Centroid(pb.geom)) AS centrallongitude,"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =p.plot_code) as coordinates,"
			+ "d.district_code,p.plot_code,k.khatian_code,pal.scheduled_inspection_date,"
			+ "pal.lo_remarks,pal.total_area,pal.purchase_area,pal.price_per_acre,pal.total_price_in_purchase_area FROM "
			+ "land_bank.district_master d INNER JOIN land_bank.tahasil_master t ON t.district_code = d.district_code "
			+ "INNER JOIN land_bank.village_master v ON t.tahasil_code = v.tahasil_code "
			+ "INNER JOIN land_bank.khatian_information k ON v.village_code = k.village_code "
			+ "INNER JOIN land_bank.plot_information p ON k.khatian_code = p.khatian_code "
			+ "INNER JOIN application.post_allotment_inspection pal on p.plot_code= pal.plot_code "
			+ "LEFT JOIN (SELECT v.village_code AS sum_village_code,SUM(p.area_acre) AS total_area "
			+ "FROM land_bank.village_master v INNER JOIN land_bank.khatian_information k "
			+ "ON v.village_code = k.village_code INNER JOIN land_bank.plot_information p "
			+ "ON k.khatian_code = p.khatian_code WHERE v.village_code =:villageCode GROUP BY "
			+ "v.village_code) AS total_area ON v.village_code = total_area.sum_village_code "
			+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(pb.plot_code=p.plot_code) "
			+ "WHERE v.village_code =:villageCode and pal.co_remarks is null and p.plot_code in (select plot_code from application.post_allotment_inspection where co_remarks is null) order by pal.scheduled_inspection_date asc ", nativeQuery = true)
	List<Object[]> fetchPostAllotmentPendingPlotDetails(String villageCode);

	@Query(value = "select pa.post_allotment_inspection_id,pa.district_code,dm.district_name,pa.tahasil_code,tm.tahasil_name,pa.village_code,"
			+ "vm.village_name,pa.khatian_code,ki.khata_no,pa.plot_code,pi.plot_no,pi.kissam,pa.co_remarks,\r\n"
			+ "pa.co_uploaded_photo,pa.inspection_date,pa.scheduled_inspection_date,pa.lo_remarks,\r\n"
			+ "pa.total_area,pa.purchase_area,pa.price_per_acre,pa.total_price_in_purchase_area,\r\n"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,\r\n"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pi.plot_code) as coordinates\r\n"
			+ "from application.post_allotment_inspection pa \r\n"
			+ "inner join land_bank.district_master dm on pa.district_code = dm.district_code \r\n"
			+ "inner join land_bank.tahasil_master tm on pa.tahasil_code = tm.tahasil_code \r\n"
			+ "inner join land_bank.village_master vm on pa.village_code = vm.village_code \r\n"
			+ "inner join land_bank.khatian_information ki on pa.khatian_code = ki.khatian_code \r\n"
			+ "inner join land_bank.plot_information pi on pa.plot_code = pi.plot_code\r\n"
			+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(pb.plot_code=pi.plot_code)\r\n"
			+ "where pa.deleted_status = '0' and pa.co_remarks is not null order by pa.inspection_date desc", nativeQuery = true)
	List<Object[]> fetchPostAllotmentCompletedList();

	@Query(value = "select pa.post_allotment_inspection_id,pa.district_code,dm.district_name,pa.tahasil_code,tm.tahasil_name,pa.village_code,"
			+ "vm.village_name,pa.khatian_code,ki.khata_no,pa.plot_code,pi.plot_no,pi.kissam,pa.co_remarks,\r\n"
			+ "pa.co_uploaded_photo,pa.inspection_date,pa.scheduled_inspection_date,pa.lo_remarks,\r\n"
			+ "pa.total_area,pa.purchase_area,pa.price_per_acre,pa.total_price_in_purchase_area,\r\n"
			+ "ST_Y(ST_Centroid(pb.geom)) AS centralLatitude,ST_X(ST_Centroid(pb.geom)) AS centralLongitude,\r\n"
			+ "(select ST_AsText(geom) from land_bank.cadastral_plot_boundary where plot_code =pi.plot_code) as coordinates\r\n"
			+ "from application.post_allotment_inspection pa \r\n"
			+ "inner join land_bank.district_master dm on pa.district_code = dm.district_code \r\n"
			+ "inner join land_bank.tahasil_master tm on pa.tahasil_code = tm.tahasil_code \r\n"
			+ "inner join land_bank.village_master vm on pa.village_code = vm.village_code \r\n"
			+ "inner join land_bank.khatian_information ki on pa.khatian_code = ki.khatian_code \r\n"
			+ "inner join land_bank.plot_information pi on pa.plot_code = pi.plot_code\r\n"
			+ "LEFT JOIN land_bank.sjta_plot_boundary pb ON(pb.plot_code=pi.plot_code)\r\n"
			+ "where pa.deleted_status = '0' ORDER BY pa.scheduled_inspection_date asc", nativeQuery = true)
	List<Object[]> fetchPostAllotmentListDetails();

}
