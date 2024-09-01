package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionGenerateDTO implements Serializable {
	
	/**
	 * @author rashmi.jena
	 */
	
	private static final long serialVersionUID = -4570800641623950660L;
	
	
	private String receiptNo;
	private String applicationNo;
	private String transactionNo;
	private BigDecimal amountPaid;
	private Short paymantMethod;
	private String applicantName;
	private String plotNo;
	private String khataNo;
	private String mouzaName;
	private String tahasilName;
	private String districtName;
	private String paymantDate;
	private String noRecord;
	private String addressLine2;
}
