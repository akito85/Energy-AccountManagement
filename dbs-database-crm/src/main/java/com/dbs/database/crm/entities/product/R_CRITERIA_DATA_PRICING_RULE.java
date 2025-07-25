package com.dbs.database.crm.entities.product;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.utils.BooleanToYNStringConverter;

import lombok.Data;


/**
 * The persistent class for the R_CRITERIA_DATA_PRICING_RULE database table.
 * 
 */
@Entity
@Data
@Table(name = "R_CRITERIA_DATA_PRICING_RULE")
public class R_CRITERIA_DATA_PRICING_RULE extends DefaultBaseEntities implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="R_CRITERIA_DATA_PRICING_RULE_SEQ", sequenceName="R_CRITERIA_DATA_PRICING_RULE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="R_CRITERIA_DATA_PRICING_RULE_SEQ")
	private Integer id;

        @Column(name="CURRENCY")
	private String currency;

        @Column(name="DATA")
	private String data;

	@Column(name="PRICING_RULE_ID")
	private Integer pricingRuleId;
	
	@Column(name="IS_DELETED")
	@Convert(converter= BooleanToYNStringConverter.class)
	private Boolean isDeleted;

	@Column(name="ENTITY_ID")
	private Integer entityId;


    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + pricingRuleId;
        }
    }


}