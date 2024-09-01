package com.csmtech.sjta.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "plot_verification")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlotVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plot_verification_id")
	private Integer intId;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "tahasil_code")
	private String tahasilcode;

	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "khatian_code")
	private String khatianCode;

	@Column(name = "plot_code")
	private String plotCode;

	@Column(name = "status")
	private Short status;

	@Column(name = "remark")
	private String remarks;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date updatedOn;

	@Column(name = "deleted_flag", insertable = false, updatable = false)
	private Boolean deletedFlag;

}
