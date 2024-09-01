package com.csmtech.sjta.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleMenuDataDTO {

	private Short roleId;
	private Short moduleId;
	private String moduleName;
	private String moduleIcon;
	private Short slNo;
	List<Map<String, String>> menus;

	public ModuleMenuDataDTO(Short moduleId, String moduleName, String moduleIcon, Short slNo,
			List<Map<String, String>> menus) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.moduleIcon = moduleIcon;
		this.slNo = slNo;
		this.menus = menus;
	}

}
