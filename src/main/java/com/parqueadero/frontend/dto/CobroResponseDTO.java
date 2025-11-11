package com.parqueadero.frontend.dto;


public class CobroResponseDTO {
    private Boolean success;
    private String message;
    private Double total;
    private Double horas;
    private Double precioPorHora;
    private String vehiculo;
    
    // Constructor vacío
    public CobroResponseDTO() {}
    
    // Constructor completo
    public CobroResponseDTO(Boolean success, String message, Double total, 
                           Double horas, Double precioPorHora, String vehiculo) {
        this.success = success;
        this.message = message;
        this.total = total;
        this.horas = horas;
        this.precioPorHora = precioPorHora;
        this.vehiculo = vehiculo;
    }
    
    // Getters y Setters
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    
    public Double getHoras() { return horas; }
    public void setHoras(Double horas) { this.horas = horas; }
    
    public Double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(Double precioPorHora) { this.precioPorHora = precioPorHora; }
    
    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }
}