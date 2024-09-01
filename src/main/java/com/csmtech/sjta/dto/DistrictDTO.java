package com.csmtech.sjta.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DistrictDTO implements Serializable {
	
	 /**
	 * @RRJ
	 */
	private static final long serialVersionUID = 8498560056661295336L;
	
	
	private String districtCode;
	 private String districtName;

}
