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
public class TenderAndAdvertizeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	BigInteger tenderAdvertisementId;
	BigInteger tenderTypeId;
	String title;
//	String upload_doc;
	Date start_date;
	Date close_date;
	BigInteger district_id;
	BigInteger tehsil_id;
	BigInteger mouza_id;
	BigInteger katha_no_id;
	BigInteger plot_no_id;
	BigInteger created_by;

}
