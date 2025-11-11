package com.parqueadero.frontend.dto;


public class TarifaDTO {
    private Integer id;
    private String tipo;
    private Double precioPorHora;
    private Boolean activa;
    private String creadoEn;
    
    // Constructor vacío
    public TarifaDTO() {}
    
    // Constructor completo
    public TarifaDTO(Integer id, String tipo, Double precioPorHora, Boolean activa, String creadoEn) {
        this.id = id;
        this.tipo = tipo;
        this.precioPorHora = precioPorHora;
        this.activa = activa;
        this.creadoEn = creadoEn;
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(Double precioPorHora) { this.precioPorHora = precioPorHora; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    public String getCreadoEn() { return creadoEn; }
    public void setCreadoEn(String creadoEn) { this.creadoEn = creadoEn; }
}