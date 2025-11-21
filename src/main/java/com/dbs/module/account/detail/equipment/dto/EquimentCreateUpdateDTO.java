package com.dbs.module.account.detail.equipment.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EquimentCreateUpdateDTO {

    private Integer id;

    @NotNull(message = "Accound id cannot be null!")
    private Integer accountId;

    @NotNull(message = "Name cannot be null!")
    private Integer name;

    @NotNull(message = "Type equipment cannot be null!")
    private Integer typeEquipment;

    private Integer brand;

    @NotNull(message = "Quantity cannot be null!")
    private Double qty;

    @NotNull(message = "Quantity UOM cannot be null!")
    private Integer qtyUom;

    @NotNull(message = "Capacity cannot be null!")
    private Double cap;

    @NotNull(message = "Capacity UOM cannot be null!")
    private Integer capUom;

    @NotNull(message = "Energy consumption cannot be null!")
    private Double con;

    @NotNull(message = "Energy consumption UOM cannot be null!")
    private Integer conUom;

    @NotNull(message = "Operating hours cannot be null!")
    private Integer noh;

    @NotNull(message = "Operating days cannot be null!")
    private Integer nod;

    @NotNull(message = "Gas conversion cannot be null!")
    private Double gasConv;

    @NotNull(message = "Gas conversion UOM cannot be null!")
    private Integer gasConvUom;

    private Boolean isDualFuel;

    @NotNull(message = "Fuel type 1 cannot be null!")
    private Integer fuelType1;

    private Integer fuelType2;

    private String description;
}
