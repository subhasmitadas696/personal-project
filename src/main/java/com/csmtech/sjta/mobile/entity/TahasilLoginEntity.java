package com.csmtech.sjta.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tahasil_login_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TahasilLoginEntity {

	@Id
	@Column(name = "tahasil_login_id")
	private Integer tahasilId;

	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Column(name = "password")
	private String password;

	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "otp")
	private String otp;
}
