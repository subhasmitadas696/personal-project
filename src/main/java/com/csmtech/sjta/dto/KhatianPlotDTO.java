package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhatianPlotDTO  {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = 5068926817836867829L;

	private String khatianCode;
	private String khataNo;
	private String plotNo;
	private String areaAcre;
	private String extent;
	private String villageCode;
	private String villageName;
	private String tahasilCode;
	private String tahasilName;
	private String districtCode;
	private String districtName;
	private String plotCode;
	private String remark;
	private Short status;

}
