package com.parqueadero.frontend.client;

import com.parqueadero.frontend.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ClienteApiClient {
    private final RestTemplate restTemplate;

    @Value("${api.parqueadero.base-url}")
    private String baseUrl;

    public ClienteApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ClienteDTO> obtenerTodos() {
        String url = baseUrl + "/api/clientes";
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
        
        return dataList.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public ClienteDTO registrar(String nombre, String documento, String telefono, 
                                 String email, String tipoCliente, Double descuento) {
        String url = baseUrl + "/api/clientes";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("nombre", nombre);
        body.add("documento", documento);
        if (telefono != null) body.add("telefono", telefono);
        if (email != null) body.add("email", email);
        if (tipoCliente != null) body.add("tipoCliente", tipoCliente);
        if (descuento != null) body.add("descuento", String.valueOf(descuento));
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        
        return mapToDTO(data);
    }

    private ClienteDTO mapToDTO(Map<String, Object> map) {
        return ClienteDTO.builder()
            .id((Integer) map.get("id"))
            .nombre((String) map.get("nombre"))
            .documento((String) map.get("documento"))
            .telefono((String) map.get("telefono"))
            .email((String) map.get("email"))
            .tipoCliente((String) map.get("tipoCliente"))
            .descuento(map.get("descuento") != null ? ((Number) map.get("descuento")).doubleValue() : 0.0)
            .build();
    }
}
