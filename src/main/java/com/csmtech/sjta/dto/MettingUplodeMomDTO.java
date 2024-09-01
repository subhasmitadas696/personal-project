package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MettingUplodeMomDTO {

	private String filename;
	private BigInteger meetingId;
	private BigInteger createdBy;
	private Short mettingLevleId;
	private List<MettingUplodeMomAuctionApprovalDTO> landActionRecord;
	private List<MettingUplodeMomPlotsRecordDTO> plotsRecord;
	private Integer auctionFlag;
	private Short noPlotsAvaliableForMeetingFlag;
	private Integer directAuctionHideFlag;
}
