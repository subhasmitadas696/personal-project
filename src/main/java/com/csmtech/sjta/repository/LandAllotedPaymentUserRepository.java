package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import com.csmtech.sjta.dto.LandAllotedPaymentRecordListDTO;
import com.csmtech.sjta.dto.LandAllotementPaymantRecordDTO;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;

public interface LandAllotedPaymentUserRepository {

	public List<LandAllotementPaymantRecordDTO> getLandAllotementDetails(BigInteger landAllotementId);

	public Integer insertPaymentTransaction(String orderId, String paymentSignature, String paymentId,
			BigDecimal tranctionAmount, BigInteger userId, BigInteger landAllotedId,String reciptNo);

	public List<LandAllotedPaymentRecordListDTO> getPayments(BigInteger landAllotedId);

	public List<LandAllotementResponesDTO> getLandAllotementDetails(Integer limit, Integer offset);

	public BigInteger countLandAllortUser();

	public Integer updateLandAllotementFormRegisterDocsAndFlag(BigInteger landAllotementId, String formRegisterDocs);

}
