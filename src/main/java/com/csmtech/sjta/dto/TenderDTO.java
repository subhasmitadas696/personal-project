package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderDTO implements Serializable  {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = -4245913106914020501L;
	
	private String combineValue;
	private String biddocsStart;
	private String biddocsEnd;
	private String foemMStart;
	private String foemMEnd;
	private String slotStart;
	private String slotEnd;
	private String auctionName;

}
