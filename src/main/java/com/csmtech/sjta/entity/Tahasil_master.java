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
@Table(name = "tahasil_master", schema = "land_bank")
@Entity
public class Tahasil_master {
	@Id
	@Column(name = "tahasil_code")
	private String txtTahasilCode;

	@Transient
	private String distTahasilCode;

	@Column(name = "district_code")
	private String selDistrictCode;
	@Transient
	private String selDistrictCodeVal;

	@Transient
	private String selStateCode;

	@Transient
	private String selStateCodeVal;

	@Transient
	private String selCountryCode;

	@Transient
	private String selCountryCodeVal;

	@Column(name = "tahasil_name")
	private String txtTahasilName;

	@Transient
	private String lastTahasilCode;
}