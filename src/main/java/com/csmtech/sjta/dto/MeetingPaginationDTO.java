/**
 * 
 */
package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingPaginationDTO {
	List<MeetingScheduleDTO> dtodata;
	BigInteger count;

}
