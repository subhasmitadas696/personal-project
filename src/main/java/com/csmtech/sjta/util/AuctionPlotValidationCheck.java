package com.csmtech.sjta.util;

import com.csmtech.sjta.dto.SourcerCreationDto;

public class AuctionPlotValidationCheck {

	private AuctionPlotValidationCheck() {
		super();
	}

	public static String BackendValidation(SourcerCreationDto sourcerCreationDto) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0
				&& CommonValidator.blankCheckRdoDropChk(sourcerCreationDto.getSelDistrictName().toString())) {
			errorStatus = 1;
			errMsg = "District Name  should not be empty !";
		}
		if (errorStatus == 0
				&& CommonValidator.blankCheckRdoDropChk(sourcerCreationDto.getSelTehsilName().toString())) {
			errorStatus = 1;
			errMsg = "Tehsil Name  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(sourcerCreationDto.getSelMouza().toString())) {
			errorStatus = 1;
			errMsg = "Mouza  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(sourcerCreationDto.getSelKhataNo().toString())) {
			errorStatus = 1;
			errMsg = "Khata No.  should not be empty !";
		}


		return errMsg;
	}
}
