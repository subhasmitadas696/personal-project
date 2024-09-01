package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TahasilDTO implements Serializable {

	/**
	 * RRJ
	 */
	private static final long serialVersionUID = 8217357177819103477L;

	    private String tahasilCode;
	    private String tahasilName;
	    private BigInteger totalMouza;
	    private BigInteger totalKatha;
	    private BigInteger totalPlot;
	    private String totalArea;
	    private String extent;
}
