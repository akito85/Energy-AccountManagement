package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_FILE")
public class M_FILE implements Serializable {
    @Id
    @Column(name="FILE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_FILE_SEQ")
    @SequenceGenerator(sequenceName = "M_FILE_SEQ", allocationSize = 1, name = "M_FILE_SEQ")
    private Integer fileId;

    @NotBlank(message = "fileType cannot be null or empty")
    @Column(name="FILETYPE", length = 250, unique = true)
    private String fileType;

    @NotBlank(message = "fileName cannot be null or empty")
    @Column(name="FILENAME", length = 250)
    private String fileName;

    @NotBlank(message = "path cannot be null or empty")
    @Column(name="PATH", length = 250)
    private String path;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + fileId;
        }
    }

}
