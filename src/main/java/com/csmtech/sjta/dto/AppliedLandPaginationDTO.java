package com.csmtech.sjta.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppliedLandPaginationDTO {

	List<AppliedLandApplicationDTO> dtodata;
	Integer count;

}
