package com.parqueadero.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDTO {
    private Integer id;
    private String tipo;
    private Double precioPorHora;
    private Boolean activa;
    private String creadoEn;
}