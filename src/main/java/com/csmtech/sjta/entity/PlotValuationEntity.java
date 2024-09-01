package com.csmtech.sjta.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "plot_valuation", schema = "application")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlotValuationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plot_valuation_id")
	private Integer plotValuationId;

	@Column(name = "plot_land_inspection_id")
	private Integer plotLandInspectionId;

	@Column(name = "khatian_code")
	private String khatianCode;

	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "available_land")
	private Short availableLand;

	@Column(name = "land_owner_name")
	private String landOwnerName;

	@Column(name = "land_address")
	private String landAddress;

	@Column(name = "type_of_land_use")
	private Integer typeOfLandUse;

	@Column(name = "tenure")
	private String tenure;

	@Column(name = "ownership_record")
	private Short ownershipRecord;

	@Column(name = "ownership_record_doc")
	private String ownershipRecordDoc;

	@Column(name = "price_per_acre")
	private BigDecimal pricePerAcre;

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@Column(name = "created_on")
	private Date createdDate;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "deleted_flag", insertable = false, updatable = false)
	private Boolean deletedFlag;

}
