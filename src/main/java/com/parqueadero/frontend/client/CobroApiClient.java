package com.parqueadero.frontend.client;

import com.parqueadero.frontend.dto.CobroResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CobroApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.parqueadero.base-url}")
    private String baseUrl;

    public CobroApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CobroResponseDTO registrarSalida(String placa) {
        String url = baseUrl + "/cobro";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("placa", placa);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        
        return CobroResponseDTO.builder()
            .success((Boolean) response.get("success"))
            .message((String) response.get("message"))
            .total(response.get("total") != null ? ((Number) response.get("total")).doubleValue() : null)
            .horas(response.get("horas") != null ? ((Number) response.get("horas")).doubleValue() : null)
            .precioPorHora(response.get("precioPorHora") != null ? ((Number) response.get("precioPorHora")).doubleValue() : null)
            .vehiculo((String) response.get("vehiculo"))
            .build();
    }
}