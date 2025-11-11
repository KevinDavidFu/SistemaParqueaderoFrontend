package com.parqueadero.frontend.dto;


public class VehiculoDTO {
    private Integer id;
    private String placa;
    private String modelo;
    private String tipo;
    private String ingreso;
    private String salida;
    private Double totalPagado;
    private Boolean activo;

    // Constructor vacío
    public VehiculoDTO() {}

    // Constructor completo
    public VehiculoDTO(Integer id, String placa, String modelo, String tipo, 
                       String ingreso, String salida, Double totalPagado, Boolean activo) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ingreso = ingreso;
        this.salida = salida;
        this.totalPagado = totalPagado;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getIngreso() { return ingreso; }
    public void setIngreso(String ingreso) { this.ingreso = ingreso; }

    public String getSalida() { return salida; }
    public void setSalida(String salida) { this.salida = salida; }

    public Double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(Double totalPagado) { this.totalPagado = totalPagado; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}