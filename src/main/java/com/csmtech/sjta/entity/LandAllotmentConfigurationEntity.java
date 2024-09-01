package com.csmtech.sjta.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "land_allotment_configuration",schema = "application")
public class LandAllotmentConfigurationEntity {
	
	@Id
	@Column(name = "land_allotment_configuration_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger landId;
	
	@Column(name = "district_code" )
    private String selDistrictName;

    @Column(name = "tahasil_code")
    private String selTehsilName;

    @Column(name = "village_code" )
    private String selMouza;

    @Column(name = "khatian_code" )
    private String selKhataNo;

    @Column(name = "plot_code" )
    private String selPlotNo;

    @Column(name = "total_amount" )
    private BigDecimal txtTotalRakba;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_on")
    private Date updatedOn;

    
    //columnDefinition = "BIT DEFAULT 0"  hear the bit will 0
    @Transient
    @Column(name = "deleted_flag",insertable = false)
    private Boolean deletedFlag;

}
