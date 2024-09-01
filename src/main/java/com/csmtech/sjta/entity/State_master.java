package com.csmtech.sjta.entity;
import javax.persistence.Entity;
import javax.persistence.Column;
import java.util.List;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Date;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Data
@Table(name="state_master",schema = "land_bank")
@Entity
public class State_master {
@Id
@Column(name="state_code")
private String txtStateCode;
@Column(name="country_code")
private String selCountryCode;
@Transient
private String selCountryCodeVal;

@Column(name="state_name")
private String txtStateName;

}