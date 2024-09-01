package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface ReceiptRepository {

	public List<Object[]> fetchFeePayment(BigInteger id);

	public List<Object[]> fetchFormMFeePayment(BigInteger id);

	public List<Object[]> fetchFinalFeePayment(BigInteger id);

}
