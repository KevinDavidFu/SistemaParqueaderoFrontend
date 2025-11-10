package com.parqueadero.frontend.client;

import com.parqueadero.frontend.dto.TarifaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TarifaApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.parqueadero.base-url}")
    private String baseUrl;

    public TarifaApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TarifaDTO> obtenerTodas() {
        String url = baseUrl + "/api/tarifas";
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
        
        return dataList.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public TarifaDTO crear(String tipo, Double precioPorHora) {
        String url = baseUrl + "/api/tarifas";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("tipo", tipo);
        body.add("precioPorHora", String.valueOf(precioPorHora));
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        
        return mapToDTO(data);
    }

    private TarifaDTO mapToDTO(Map<String, Object> map) {
        return TarifaDTO.builder()
            .id((Integer) map.get("id"))
            .tipo((String) map.get("tipo"))
            .precioPorHora(((Number) map.get("precioPorHora")).doubleValue())
            .activa((Boolean) map.get("activa"))
            .creadoEn((String) map.get("creadoEn"))
            .build();
    }
}