package com.parqueadero.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {
    private Integer id;
    private String placa;
    private String modelo;
    private String tipo;
    private String ingreso;
    private String salida;
    private Double totalPagado;
    private Boolean activo;
}