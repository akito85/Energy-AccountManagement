package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_LOGIN_BACKGROUND")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_LOGIN_BACKGROUND extends BaseEntities implements Serializable {

    @Id
    @Column(name = "LOGIN_BACKGROUND_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_LOGIN_BACKGROUND_SEQ")
    @SequenceGenerator(sequenceName = "M_LOGIN_BACKGROUND_SEQ", allocationSize = 1, name = "M_LOGIN_BACKGROUND_SEQ")
    private Integer loginBackgroundId;

    @Column(name = "BACKGROUND_NAME", length = 50)
    private String backgroundName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column(name="IMAGE", length = 250)
    private String image;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + loginBackgroundId;
        }
    }
}
