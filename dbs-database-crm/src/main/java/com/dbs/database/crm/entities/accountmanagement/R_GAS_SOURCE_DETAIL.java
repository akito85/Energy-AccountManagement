package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "R_GAS_SOURCE_DETAIL")
public class R_GAS_SOURCE_DETAIL extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_GAS_SOURCE_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "R_GAS_SOURCE_DETAIL_SEQ", allocationSize = 1, name = "R_GAS_SOURCE_DETAIL_SEQ")
    private Integer gasSourceDetailId;

    @Column(name = "GAS_SOURCE_ID")
    private Integer gasSourceId;

    @Column(name = "DOCUMENT_NUMBER", length = 100)
    private String documentNumber;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "M3")
    private Double m3; // MMBTU Value

    @Column(name = "BTU")
    private Double btu; // SCF(GHV) Value

    @Column(name = "SG")
    private Double sg;

    @Column(name = "N2")
    private Double n2;

    @Column(name = "CO2")
    private Double co2;

    @Column(name = "DESCRIPTION")
    private String description;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + gasSourceDetailId;
        }
    }
}
