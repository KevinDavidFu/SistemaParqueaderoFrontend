package com.parqueadero.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String documento;
    private String telefono;
    private String email;
    private String tipoCliente;
    private Double descuento;
}