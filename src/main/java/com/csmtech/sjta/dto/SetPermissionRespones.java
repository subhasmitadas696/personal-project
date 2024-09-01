package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPermissionRespones {
	
	private Long roleId;
    private Long moduleId;
    private Long menuId;
}
