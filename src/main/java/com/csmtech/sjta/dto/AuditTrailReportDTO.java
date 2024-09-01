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
public class AuditTrailReportDTO implements Serializable {

	/**
	 *@author rashmi.jena 
	 */
	private static final long serialVersionUID = -9219576216369731782L;
	
	
	private BigInteger activityLogId;
	private String url;
	private String serverIp;
	private String userAgent;
	private BigInteger userRole;
	private BigInteger userId;
	private String userName;
	private Date createdOn;
	private String uniqueNumber;
	private String roleName;
	private String deviceType;
	private String changeField;
	private String actionRemerk;
}
