package com.csmtech.sjta.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Date;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Table(name = "district_master", schema = "land_bank")
@Entity
public class DistrictMaster {
	@Id
	@Column(name = "district_code")
	private String txtDistrictCode;
	@Column(name = "state_code")
	private String selStateCode;
	@Transient
	private String selStateCodeVal;

	@Transient
	private String selCountryCode;

	@Transient
	private String selCountryCodeVal;

	@Column(name = "district_name")
	private String txtDistrictName;

	@Transient
	private String extent;
	
}