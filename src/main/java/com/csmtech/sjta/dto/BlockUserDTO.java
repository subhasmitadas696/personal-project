/**
 * 
 */
package com.csmtech.sjta.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private Character status;
	private Boolean userBlockStatus;
	private String blockRemarks;

}
