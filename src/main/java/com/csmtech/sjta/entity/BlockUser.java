/**
 * 
 */
package com.csmtech.sjta.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockUser {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String username;
	    private Boolean userBlockStatus;
	    private String blockRemarks;
}
