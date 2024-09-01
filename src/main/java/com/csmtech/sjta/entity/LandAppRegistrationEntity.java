package com.csmtech.sjta.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_DETAILS")
public class LandAppRegistrationEntity implements Serializable {

	/**
	 * @Auth Rashmi Ranjan Jena
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	private Long id;

	@Transient
	private Long citizenId;

	@Column(name = "user_name")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "mobile_no")
	private String mobileno;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "otp")
	private String otp;

	@Column(name = "user_type")
	private String userType; 

	@Column(name = "created_by")
	private Long createdBy;  

	@Column(name = "created_on")
	@CreationTimestamp
	private Date createdOn;

	@Column(name = "updated_by")
	private Long upatedBy;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private Date upatedOn;

	@Column(name = "status", insertable = false, updatable = false)
	private String status;

	@Column(name = "is_first_login")
	private Short isFirstLogin;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "tahasil_code")
	private String tahasilCode;
	
	@Column(name = "login_validation_time")
	private Date loginValidateTime;
	
	@Column(name = "login_validation")
	private Short loginValidate;

 }
