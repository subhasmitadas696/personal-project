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
public class PlotDetails implements Serializable {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = 1L;

	private String plot_id;
	private String plot_name;
	private String total_area;
	private String purchase_area;
	private String khataNo;

	private String district;
	private String tahasil;
	private String mouza;

}
