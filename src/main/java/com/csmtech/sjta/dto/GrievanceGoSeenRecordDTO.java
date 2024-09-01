package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrievanceGoSeenRecordDTO implements Serializable {

	/**
	 * @author RRJ
	 */
	private static final long serialVersionUID = -447974308173191769L;

	private String GrievanceNo;
	private String districtCode;
	private String tahasilCode;
	private String villageCode;
	private String khatianCode;
	private String plotNo;
	private Integer GrievanceId;
	private Date CreatedOn;
	private String extent;
	private String remarkByGO;

}
