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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lease_case_payment", schema = "application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaseCasePaymantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lease_case_payment_id")
	private BigInteger leasecasepaymantId;

	@Column(name = "lease_case_plot_id")
	private BigInteger leaseCasePlotId;

	@Column(name = "mr_no")
	private String mrno;

	@Column(name = "mr_date")
	private Date mrdate;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "rsd_no")
	private String rsdno;

	@Column(name = "rsd_date")
	private Date rsddate;

	@Column(name = "created_by")
	private BigInteger createdBy;

}
