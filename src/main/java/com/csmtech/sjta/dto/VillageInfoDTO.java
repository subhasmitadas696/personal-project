package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VillageInfoDTO implements Serializable {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = 8989142196719303747L;

	private String villageCode;
    private String villageName;
    private BigInteger totalVillage;
    private BigInteger totalKatha;
    private BigInteger totalPlot;
    private String totalArea;
    private String extent;
    private String psName;
}
