package com.csmtech.sjta.mobile.dto;

import java.util.List;

import com.csmtech.sjta.mobile.entity.NotificationDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseDto {
	
	private Integer status;
	private String message;
	private String statusMessage;
	private Integer count;
	private List<NotificationDetails> notifications;
	

}
