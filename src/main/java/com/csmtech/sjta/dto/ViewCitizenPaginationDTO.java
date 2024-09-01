package com.csmtech.sjta.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCitizenPaginationDTO {

	List<UserDetailsDTO> dto;
	Integer count;

}
