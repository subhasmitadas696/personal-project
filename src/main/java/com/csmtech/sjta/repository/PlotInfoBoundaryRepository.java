package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Plot_info_Boundary;

public interface PlotInfoBoundaryRepository extends JpaRepository<Plot_info_Boundary, Serializable> {
	@Query(value = "select pi.plot_code,pi.plot_no,pb.village_code,"
			+ "ki.khata_no,pi.area_acre,pi.kissam,"
			+ "CAST(ST_extent(ST_Transform(pb.geom, 3857)) AS character varying) AS extent,kd.digital_file "
			+ "from land_bank.plot_information pi inner join land_bank.sjta_plot_boundary pb "
			+ "on pi.plot_code = pb.plot_code inner join land_bank.khatian_information ki on "
			+ "pi.khatian_code = ki.khatian_code inner join land_bank.khatian_digital_information kd "
			+ "on pi.khatian_code = kd.khatian_code where pb.village_code =:villageCode "
			+ "group by pi.plot_code,pi.plot_no,pb.village_code,ki.khata_no,pi.area_acre,pi.kissam,kd.digital_file ", nativeQuery = true)
	List<Object[]> plotinfoForTableView(String villageCode); 

}