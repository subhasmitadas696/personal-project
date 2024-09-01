package com.csmtech.sjta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuDataDto {

	private String menuId;
	private String menuName;
	private String menuurl;
	private String slNo;

	public MenuDataDto(String menuId, String menuName, String menuurl, String slNo) {
		super();
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuurl = menuurl;
		this.slNo = slNo;
	}
}
