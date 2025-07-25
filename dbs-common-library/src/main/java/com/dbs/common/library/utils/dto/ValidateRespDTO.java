/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.utils.dto;

import lombok.Data;

/**
 *
 * @author RachmatY
 */
@Data
@SuppressWarnings("java:S1068")
public class ValidateRespDTO {
    private Boolean status;
    private String message;
}
