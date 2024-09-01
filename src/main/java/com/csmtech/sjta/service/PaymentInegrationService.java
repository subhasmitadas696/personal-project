package com.csmtech.sjta.service;

import com.csmtech.sjta.dto.TranctionDetails;

public interface PaymentInegrationService {
	
	public TranctionDetails createTranction(Integer amount);

}
