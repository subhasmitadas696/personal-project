package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandPaginationDTO {

	List<LandApplicantDTO> dtodata;
	BigInteger count;
}
