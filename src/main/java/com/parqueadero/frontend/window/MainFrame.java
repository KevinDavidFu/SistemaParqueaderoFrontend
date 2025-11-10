package com.parqueadero.frontend.window;

import com.parqueadero.frontend.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación
 * Contiene pestañas para gestionar: Vehículos, Tarifas, Clientes y Cobros
 */
@Component
public class MainFrame extends JFrame {

    // Inyección de servicios API
    @Autowired
    private VehiculoApiClient vehiculoService;
    
    @Autowired
    private TarifaApiClient tarifaService;
    
    @Autowired
    private ClienteApiClient clienteService;
    
    @Autowired
    private CobroApiClient cobroService;

    // Paneles de las pestañas
    private VehiculoPanel vehiculoPanel;
    private TarifaPanel tarifaPanel;
    private ClientePanel clientePanel;
    private CobroPanel cobroPanel;

    public MainFrame() {
        setTitle("Sistema de Gestión de Parqueadero");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Inicializa la interfaz gráfica con todas las pestañas
     */
    public void initializeUI() {
        setLayout(new BorderLayout());

        // Crear el panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Inicializar paneles con los servicios inyectados
        vehiculoPanel = new VehiculoPanel(vehiculoService);
        tarifaPanel = new TarifaPanel(tarifaService);
        clientePanel = new ClientePanel(clienteService);
        cobroPanel = new CobroPanel(cobroService, vehiculoService);

        // Agregar pestañas con iconos
        tabbedPane.addTab("🚗 Vehículos", vehiculoPanel);
        tabbedPane.addTab("💲 Tarifas", tarifaPanel);
        tabbedPane.addTab("👤 Clientes", clientePanel);
        tabbedPane.addTab("💵 Cobro", cobroPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        JLabel footerLabel = new JLabel("Sistema Parqueadero v1.0 | Kevin David © 2025");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }
}