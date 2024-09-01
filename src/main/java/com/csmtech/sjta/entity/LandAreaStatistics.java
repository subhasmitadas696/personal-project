package com.csmtech.sjta.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LandAreaStatistics {
	@Id
	private BigInteger id;
	private Integer plotArea;
	private String district;
	private String tahsil;
	private Integer village;
}
