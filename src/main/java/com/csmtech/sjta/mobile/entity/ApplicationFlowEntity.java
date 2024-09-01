package com.csmtech.sjta.mobile.entity;

import java.math.BigInteger;
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

@Entity
@Table(name = "application_flow", schema = "application")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationFlowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flow_id")
	private BigInteger flowId;

	@Column(name = "land_application_id")
	private BigInteger landApplicationId;

	@Column(name = "application_flow_id")
	private BigInteger applicationFlowId;

	@Column(name = "action_role_id")
	private BigInteger actionRoleId;

	@Column(name = "action_datetime")
	private Date actionDateTime;

	@Column(name = "created_on")
	@CreationTimestamp
	private Date createdOn;

	@Column(name = "created_by")
	private BigInteger createdBy;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date updatedOn;

	@Column(name = "updated_by")
	private BigInteger updatedBy;

	@Column(name = "deleted_flag", insertable = false)
	private Boolean deletedFlag;
}
