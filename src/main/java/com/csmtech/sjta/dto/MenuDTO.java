package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MenuDTO {
	
	private Long menuId;
    private String menuName;
    private Long selectedMenu;

}
