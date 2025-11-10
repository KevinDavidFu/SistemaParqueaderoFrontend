package com.parqueadero.frontend.client;

import com.parqueadero.frontend.dto.VehiculoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class VehiculoApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.parqueadero.base-url}")
    private String baseUrl;

    public VehiculoApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<VehiculoDTO> obtenerTodos() {
        String url = baseUrl + "/api/vehiculos";
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
        
        return dataList.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public VehiculoDTO registrar(String placa, String modelo, String tipo) {
        String url = baseUrl + "/api/vehiculos";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("placa", placa);
        if (modelo != null && !modelo.isEmpty()) {
            body.add("modelo", modelo);
        }
        body.add("tipo", tipo);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        
        return mapToDTO(data);
    }

    public void eliminar(String placa) {
        String url = baseUrl + "/api/vehiculos?placa=" + placa;
        restTemplate.delete(url);
    }

    private VehiculoDTO mapToDTO(Map<String, Object> map) {
        return VehiculoDTO.builder()
            .id((Integer) map.get("id"))
            .placa((String) map.get("placa"))
            .modelo((String) map.get("modelo"))
            .tipo((String) map.get("tipo"))
            .ingreso((String) map.get("ingreso"))
            .salida((String) map.get("salida"))
            .totalPagado(map.get("totalPagado") != null ? 
                ((Number) map.get("totalPagado")).doubleValue() : 0.0)
            .activo((Boolean) map.get("activo"))
            .build();
    }
}