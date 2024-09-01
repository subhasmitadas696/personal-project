package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LesaeCaseReportDTO implements Serializable {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = -8567094835691310419L;
	private String districtName;
	private String distrcitCode;
	private BigInteger noOfLeaseCseCount;
	private BigInteger totalPlotCount;
	private BigInteger dlscCount;
	private BigInteger tlscCount;
	private BigInteger mcCount;
	private String tahasilCode;
	private String tahasilName;
	private String villageCode;
	private String villageName;
	private String khataCode;
	private String khataNo;
	private String plotCode;
	private String plotNo;
	private Short fieldInquery;
	private Date dlsc;
	private Date tlsc;
	private Date mc;
	private Short noticeIssue;
	private Short considerMonyDeposite;
	private Short status;
	private String remerk;
	
}
