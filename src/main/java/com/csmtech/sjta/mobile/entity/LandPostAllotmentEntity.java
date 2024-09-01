package com.csmtech.sjta.mobile.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "post_allotment_inspection", schema = "application")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandPostAllotmentEntity {

	@Id
	@Column(name = "post_allotment_inspection_id")
	private Integer postAllotmentInspectionId;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "khatian_code")
	private String khatianCode;

	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "co_remarks")
	private String coRemarks;

	@Column(name = "co_uploaded_photo")
	private String coUploadedPhoto;

	@Column(name = "scheduled_inspection_date")
	private Date scheduledInspectionDate;

	@Column(name = "inspection_date")
	private Date inspectionDate;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@Column(name = "created_datetime")
	private Date createdDate;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "lo_remarks")
	private String loRemarks;
	
	@Column(name = "total_area")
    private BigDecimal totalArea;

    @Column(name = "purchase_area")
    private BigDecimal purchaseArea;

    @Column(name = "price_per_acre")
    private BigDecimal pricePerAcre;

    @Column(name = "total_price_in_purchase_area")
    private BigDecimal totalPriceInPurchaseArea;
    
    @Column(name = "latitude")
	private String latitude;
 
	@Column(name = "longitude")
	private String longitude;
	
}
