package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceCreationPlotSecondProcessDTO implements Serializable {

	/**
	 * @author rashmi.jena
	 */
	private static final long serialVersionUID = -1852382256679288564L;
	
	private BigInteger intId;
	private String selDistrictName;
	private String selTehsilName;
	private String selMouza;
	private String selKhataNo;
	private String selPlotNo;
	private String txtTotalRakba;
	private String selVarieties;
	private String venue;
	private String txtrMeetingPurpose54;
	private Short selMeetingLevel55;
	private Date txtMeetingDate53;
	private Integer[] selectedCCOfficers;
	private Integer[] selectedBCCOfficers;
	private List<PlotDetails> plotDetails;
	private BigInteger createdBy;

}
