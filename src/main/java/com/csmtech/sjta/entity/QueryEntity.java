package com.csmtech.sjta.entity;

/**
 * @author prasanta.sethi
 */

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
public class QueryEntity {
	@Id
	private BigInteger queryId;
	private String name;
	private String mobileNo;
	private String query;
	private String status;
}
