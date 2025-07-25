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
public class FileConfigDto implements Serializable {

    private static final long serialVersionUID = 6739708604074040516L;

    private String fileExt;
}
