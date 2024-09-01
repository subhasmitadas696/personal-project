/**
 * 
 */
package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppliedLandApplicationDTO {
	private BigInteger landApplicantId;
	private Integer createdBy;
	private String applicantNo;
    private String applicantName;
    private String mobileNo;
    private String districtName;
    private String tehsilName;
    private String mouzaName;
    private String khataNo;
    private String applicationStatus;
    private Short applicationStatusId;
    private Date actionOn;
    private String remark;
    private String plotNo;
    private Date createdOn;
    private String docName;
    private Short pendingroleid;
    private String districtCode;
    private String tahasilCode;
    private String villageCode;

}
