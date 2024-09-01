package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.entity.PlotValuationEntity;

@Repository
public interface PlotValuationRepository extends JpaRepository<PlotValuationEntity, Integer> {

	@Query(value = "select * from application.plot_valuation where plot_code =:plotCode", nativeQuery = true)
	public PlotValuationEntity findByPlotCodeNative(String plotCode);

	@Modifying
	@Transactional
	@Query(value = "INSERT into application.plot_valuation(plot_land_inspection_id,plot_code,available_land,land_owner_name,"
			+ "land_address,khatian_code,type_of_land_use,tenure,ownership_record,ownership_record_doc,price_per_acre,total_price,created_on,created_by)"
			+ "VALUES(:plotLandInspectionId,:plotCode,:availableLand,:landUserName,:landAddress,:khatianCode,:typeOfLandUse,:tenure,"
			+ ":ownershipRecord,:fileUploadOwnershipDocument,:pricePerAcre,:totalPrice,:parsedDate,:createdBy)", nativeQuery = true)
	public int updatePlotValuation(@Param("plotLandInspectionId") Integer plotLandInspectionId,
			@Param("plotCode") String plotCode, @Param("availableLand") Short availableLand,
			@Param("landUserName") String landUserName, @Param("landAddress") String landAddress,
			@Param("khatianCode") String khatianCode, @Param("typeOfLandUse") Integer typeOfLandUse,
			@Param("tenure") String tenure, @Param("ownershipRecord") Short ownershipRecord,
			@Param("fileUploadOwnershipDocument") String fileUploadOwnershipDocument,
			@Param("pricePerAcre") BigDecimal pricePerAcreBigDecimal,
			@Param("totalPrice") BigDecimal totalPriceBigDecimal, @Param("parsedDate") Date parsedDate,
			@Param("createdBy") BigInteger createdBy);

}
