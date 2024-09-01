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
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderAdvertisementDTO {

	BigInteger tenderAdvertisementId;
	Short tenderTypeId;
	String letterNo;
	String tenderType;
	String title;
	Date startDate;
	Date closeDate;
	String uploadDoc;
	Integer districtId;
	Integer tehsilId;
	Integer mouzaId;
	Integer khataNoId;
	Integer plotNoId;
	Integer createdBy;
	Integer updatedBy;
	Integer pageNumber;
	Integer pageSize;
	String pageType;

}
