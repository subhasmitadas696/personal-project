package com.csmtech.sjta.util;

import com.csmtech.sjta.entity.LandAllotmentConfigurationEntity;

public class LandAllotmentValidationCheck {

	private LandAllotmentValidationCheck() {
		super();
	}

	public static String BackendValidation(LandAllotmentConfigurationEntity dto) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0
				&& CommonValidator.blankCheckRdoDropChk(dto.getSelDistrictName().toString())) {
			errorStatus = 1;
			errMsg = "District Name  should not be empty !";
		}
		if (errorStatus == 0
				&& CommonValidator.blankCheckRdoDropChk(dto.getSelTehsilName().toString())) {
			errorStatus = 1;
			errMsg = "Tehsil Name  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(dto.getSelMouza().toString())) {
			errorStatus = 1;
			errMsg = "Mouza  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(dto.getSelKhataNo().toString())) {
			errorStatus = 1;
			errMsg = "Khata No.  should not be empty !";
		}
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(dto.getSelPlotNo().toString())) {
			errorStatus = 1;
			errMsg = "Plot No.  should not be empty !";
		}
		
		if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(dto.getTxtTotalRakba().toString())) {
			errorStatus = 1;
			errMsg = "Price Per Acer.  should not be empty !";
		}
		
		return errMsg;
	}
}
