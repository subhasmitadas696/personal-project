package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TahasilTeamUseRequestDto {

	private Integer tahasilId;
	private String tahasilCode;
	private String tahasilUserName;
	private String password;
	String captchaId;
	Integer answer;
	String tahasilcodeselct;
	private String mobileNo;
	private String otp;
	
	
	
	public TahasilTeamUseRequestDto(String tahasilCode, String password) {
		super();
		this.tahasilCode = tahasilCode;
		this.password = password;
	}

	public TahasilTeamUseRequestDto(String tahasilCode, String password, String mobileNo, String otp) {
		super();
		this.tahasilCode = tahasilCode;
		this.password = password;
		this.mobileNo = mobileNo;
		this.otp = otp;
	}

	public TahasilTeamUseRequestDto(Integer tahasilId, String tahasilCode, String password, String mobileNo,
			String otp) {
		super();
		this.tahasilId = tahasilId;
		this.tahasilCode = tahasilCode;
		this.password = password;
		this.mobileNo = mobileNo;
		this.otp = otp;
	}
	
	public TahasilTeamUseRequestDto(Integer tahasilId, String tahasilCode, String password) {
		super();
		this.tahasilId = tahasilId;
		this.tahasilCode = tahasilCode;
		this.password = password;
	}
	
	
	
	
	
	
}
