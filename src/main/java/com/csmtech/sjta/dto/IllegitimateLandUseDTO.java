/**
 * 
 */
package com.csmtech.sjta.dto;

import java.math.BigInteger;

import org.json.JSONObject;
import java.util.List;
import java.util.Map;

import javax.management.loading.PrivateClassLoader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllegitimateLandUseDTO {
	private String districtCode;
	private String districtName;
	private BigInteger totalApplicant;
	private BigInteger pending;
	private BigInteger assigned;
	private BigInteger inspectionCompleted;
	private BigInteger applicationOnHold;
	private BigInteger closed;
	private BigInteger applDiscarded;
	private String tahasilCode;
	private String tahasilName;
	private String villageCode;
	private String villageName;
	private String grievanceNo;
	private Short grievanceStatus;
	private String applicantName;
	private List<Map<String, String>> dataList;
}
