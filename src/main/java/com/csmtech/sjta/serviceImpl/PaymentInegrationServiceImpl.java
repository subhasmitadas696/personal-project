package com.csmtech.sjta.serviceImpl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.TranctionDetails;
import com.csmtech.sjta.service.PaymentInegrationService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentInegrationServiceImpl implements PaymentInegrationService {

	private final String currency = "INR";
	private final String key = "rzp_test_HGNq7txE68BT4w";
	private final String secertKey = "c2jyJVB5aXAdWQr9Ua5MB9dB";

	@SuppressWarnings("unused")
	private TranctionDetails prepareTranctionDetails(Order order) {
		String orderId = order.get("id");
		String curency = order.get("currency");
		Integer amount = order.get("amount");
		Integer actualamount = amount / 100;
		log.info(" :: return prepareTranctionDetails() Data ..!!");
		return new TranctionDetails(orderId, curency, actualamount, key);
	}

	@Override
	public TranctionDetails createTranction(Integer amount) {
		try {
			JSONObject js = new JSONObject();
			js.put("amount", (amount * 100));
			js.put("currency", currency);

			RazorpayClient client = new RazorpayClient(key, secertKey);
			Order order = client.orders.create(js);
			log.info(" :: return createTranction() Data ..!!");
			return prepareTranctionDetails(order);

		} catch (Exception e) {
			log.info(" :: Exception Occure ..!!!");
			log.error(e.getMessage());
		}
		log.info(" :: Nothing Happen !!!");
		return null;
	}

}
