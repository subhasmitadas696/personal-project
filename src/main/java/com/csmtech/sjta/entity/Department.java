/**
 * 
 */
package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guru.prasad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_department",schema = "public")
public class Department {

	@Id
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "department_name")
	private String departmentName;
	
}
