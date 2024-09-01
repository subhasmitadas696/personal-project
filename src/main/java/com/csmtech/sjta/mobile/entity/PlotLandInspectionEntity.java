package com.csmtech.sjta.mobile.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plot_land_inspection")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlotLandInspectionEntity {
	
	@Id
	@Column(name ="plot_land_inspection_id")
	private Integer plotLandInspectionId;
	
	@Column(name= "district_code")
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
	
	@Column(name = "inspection_date")
	private Date inspectionDate;
	
	@Column(name = "created_by")
	private BigInteger createdBy;
	
	@Column(name = "created_datetime")
	private Date createdDate;
	
	@Column(name ="updated_by")
	private BigInteger updatedBy;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "latitude")
	private String latitude;
	
	@Column(name = "longitude")
	private String longitude;
	
	@Column(name ="approve_status")
	private Short approveStatus ;
	
	@Column(name ="approve_by")
	private BigInteger approveBy ;
	
	@Column(name= "scheduled_inspection_date")
	private Date scheduledInspectionDate;
	
	@Column(name = "tahasil_remarks")
	private String tahasilRemarks;
	
	@Column(name = "tahasil_uploaded_photo")
	private String tahasilUploadedPhoto;
	
	@Column(name ="tahasil_latitude")
	private String tahasilLatitude;

	@Column(name ="tahasil_longitude")
	private String tahasilLongitude;
	
	@Column(name ="tahasil_status")
	private Short tahasilStatus;
	
	@Column(name = "tahasildar_inspected_by")
	private String tahasildarInspectedBy; 
	
	@Column(name = "tahasildar_inspection_date")
	private Date tahasildarInspectionDate;
}
