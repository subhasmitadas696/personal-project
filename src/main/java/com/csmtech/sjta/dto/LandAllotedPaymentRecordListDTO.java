package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LandAllotedPaymentRecordListDTO {

	@Id
	private BigInteger paymentId;
	private String paymentAmount;
	private Date dateTime;
	private Short paymentType;
	private String paymentRId;
	private String paymentRsign;
	private String paymentROrderId;
	private String paymentStatus;
}
