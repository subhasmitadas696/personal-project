package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleMenuDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long moduleId;
    private String moduleName;
    private List<MenuDTO> menus;
    private Long selectedMenu;
//    private Integer menuCount;

}
