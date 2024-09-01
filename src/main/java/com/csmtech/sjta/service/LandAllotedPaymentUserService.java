package com.csmtech.sjta.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import com.csmtech.sjta.dto.LandAllotedPaymentRecordListDTO;
import com.csmtech.sjta.dto.LandAllotementPaymantRecordDTO;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;

public interface LandAllotedPaymentUserService {

	public List<LandAllotementPaymantRecordDTO> getLandAllotementDetails(String parms);

	public Integer tranctionCount(String orderId, String paymentSignature, String paymentId, BigDecimal tranctionAmount,
			BigInteger userId, BigInteger landAllotedId);

	public List<LandAllotedPaymentRecordListDTO> getPayments(String parms);

	public List<LandAllotementResponesDTO> getLandAllotementDetailsOffcer(String parms);

	public BigInteger countLandAllortUser();

	public Integer updateWinnerDocument(String data);

}
