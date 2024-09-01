/**
 * 
 */
package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDto {
	private String question;
	private String answer;
}
