package com.dbs.common.library.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckMT940Dto implements Serializable {
    private static final long serialVersionUID = -6715731100641049385L;

    private boolean isMT940;
    private String bankName;
}
