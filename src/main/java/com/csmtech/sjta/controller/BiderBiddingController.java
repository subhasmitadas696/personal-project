package com.csmtech.sjta.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.csmtech.sjta.dto.BidderDTO;
import com.csmtech.sjta.dto.Greeting;
import com.csmtech.sjta.service.BiddingUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BiderBiddingController {

	@Autowired
	private BiddingUserService serviceBidder;

	// here the increment value comes from db
	BigDecimal increment;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(BidderDTO message) throws Exception {
		// when null they will return
		if (message.getInitId() == null || message.getTenderid() == null || message.getPrice() == null) {
			return new Greeting(111);
		}

		String enteredVal = message.getPrice();
		BigDecimal valueofBig = new BigDecimal(message.getPrice());
		Boolean checkTimeValidate = serviceBidder.getTimeExpiredOrNot(message.getTenderid());
		BigDecimal checkBasePricee = serviceBidder.getBasePrice(message.getTenderid());
		increment = serviceBidder.getCheckBiddingIncresePrice(message.getTenderid());
//		String checkHighestPrice = "false" ;  //just check assign null to false
		Integer checkValidatePrice = null;
		List<BigDecimal> data = null;
		if (Boolean.TRUE.equals(checkTimeValidate)) {
			if (enteredVal.equalsIgnoreCase("0")) {
				checkValidatePrice = 1;
				BigDecimal gteHighestPrice = serviceBidder.getHighestPriceCheck(message.getTenderid());
				if (gteHighestPrice == null) {
					// minus
					checkBasePricee = checkBasePricee.subtract(increment);
					valueofBig = checkBasePricee;
				} else {
					valueofBig = gteHighestPrice;
				}
			} else {
//				checkHighestPrice = serviceBidder.getBaseHighestPrice(message.getTenderid(), valueofBig);
				// Compare the base value to come value
				BigDecimal getGighestPrice = serviceBidder.getHighestPriceForBid(message.getTenderid());
				if(getGighestPrice!=null) {
					Integer validate = valueofBig.compareTo(getGighestPrice);
					if (validate == 1) {
						checkValidatePrice = 1;
					} else if (validate < 0 || validate == 0) {
						return new Greeting(333);
					}
				}else {
					checkValidatePrice=1;
				}
				
			}
			if (checkValidatePrice == 1) {
				// check value comparison
				if (enteredVal.equalsIgnoreCase("0")) {
					log.info("assigned the value of base value ...!!");
					data = returnData(valueofBig);
				} else {
					// save here the data what will come in input user
					Integer updateDataAndTrackRecord = serviceBidder.updateInsertSelectForBiddingUser(message);
					log.info("Update Record For bidding  " + updateDataAndTrackRecord);
					data = returnData(valueofBig);
				}
				BigDecimal gteHighestPrice = serviceBidder.getHighestPriceCheck(message.getTenderid());
				BigInteger getCountAuction = serviceBidder.getCheckBiddingCount(message.getTenderid());
				BigDecimal getMaxBidValue = serviceBidder.getMaxBidPriceForUser(message.getInitId(),
						message.getTenderid());
				return new Greeting(data, 200, gteHighestPrice, getCountAuction, message.getTenderid(), getMaxBidValue,
						message.getInitId());
			} else {
				// Price Validate
				return new Greeting(333);
			}
		} else {
			// Time Expired
			return new Greeting(222);
		}
	}

	public List<BigDecimal> returnData(BigDecimal startValue) {
		List<BigDecimal> values = new ArrayList<>();
		for (BigDecimal current = startValue.add(BigDecimal.valueOf(10)); values.size() < 5; current = current
				.add(increment)) {
			values.add(current);
		}
		return values;
	}

}
