package com.parqueadero.frontend.window;

import com.parqueadero.frontend.client.CobroApiClient;
import com.parqueadero.frontend.client.VehiculoApiClient;
import com.parqueadero.frontend.dto.CobroResponseDTO;
import com.parqueadero.frontend.dto.VehiculoDTO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel para registrar la salida de vehículos y calcular el cobro
 */
public class CobroPanel extends JPanel {

    private final CobroApiClient cobroService;
    private final VehiculoApiClient vehiculoService;
    
    private JComboBox<String> comboVehiculos;
    private JTextArea txtResultado;
    private Map<String, String> vehiculoPlacaMap;

    public CobroPanel(CobroApiClient cobroService, VehiculoApiClient vehiculoService) {
        this.cobroService = cobroService;
        this.vehiculoService = vehiculoService;
        this.vehiculoPlacaMap = new HashMap<>();
        
        initializeUI();
        cargarVehiculosActivos();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        JPanel resultPanel = createResultPanel();
        add(resultPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Salida de Vehículo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Seleccione el vehículo para registrar su salida");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, gbc);

        // Combo de vehículos
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Vehículo Activo:"), gbc);
        
        gbc.gridx = 1;
        comboVehiculos = new JComboBox<>();
        comboVehiculos.setPreferredSize(new Dimension(300, 30));
        panel.add(comboVehiculos, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnCobrar = new JButton("💵 Registrar Salida y Cobrar");
        btnCobrar.setBackground(new Color(46, 204, 113));
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCobrar.addActionListener(e -> registrarSalida());
        btnPanel.add(btnCobrar);

        JButton btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarVehiculosActivos());
        btnPanel.add(btnActualizar);

        panel.add(btnPanel, gbc);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resultado del Cobro"));

        txtResultado = new JTextArea(10, 50);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtResultado.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void cargarVehiculosActivos() {
        try {
            List<VehiculoDTO> vehiculos = vehiculoService.obtenerTodos();
            comboVehiculos.removeAllItems();
            vehiculoPlacaMap.clear();
            
            int count = 0;
            for (VehiculoDTO v : vehiculos) {
                if (v.getActivo()) {
                    String display = String.format("%s - %s (%s)", 
                        v.getPlaca(), 
                        v.getTipo(), 
                        v.getModelo() != null ? v.getModelo() : "Sin modelo");
                    
                    comboVehiculos.addItem(display);
                    vehiculoPlacaMap.put(display, v.getPlaca());
                    count++;
                }
            }
            
            if (count == 0) {
                comboVehiculos.addItem("No hay vehículos activos");
                txtResultado.setText("ℹ️ No hay vehículos activos en el parqueadero.\n");
            } else {
                txtResultado.setText(String.format("✓ %d vehículo(s) activo(s) disponible(s) para cobro.\n", count));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar vehículos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarSalida() {
        String seleccion = (String) comboVehiculos.getSelectedItem();
        
        if (seleccion == null || seleccion.equals("No hay vehículos activos")) {
            JOptionPane.showMessageDialog(this, 
                "No hay vehículos disponibles para cobro", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String placa = vehiculoPlacaMap.get(seleccion);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            String.format("¿Registrar salida del vehículo %s?", placa), 
            "Confirmar Cobro", 
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            CobroResponseDTO response = cobroService.registrarSalida(placa);
            
            if (response.getSuccess()) {
                String resultado = String.format("""
                    ═══════════════════════════════════════════════════
                    ✓ COBRO REALIZADO EXITOSAMENTE
                    ═══════════════════════════════════════════════════
                    
                    Vehículo:        %s
                    Horas:           %.2f horas
                    Tarifa/Hora:     $%.2f COP
                    ─────────────────────────────────────────────────── 
                    TOTAL A PAGAR:   $%.2f COP
                    ═══════════════════════════════════════════════════
                    
                    Fecha y hora: %s
                    """, 
                    response.getVehiculo(),
                    response.getHoras(),
                    response.getPrecioPorHora(),
                    response.getTotal(),
                    java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                );
                
                txtResultado.setText(resultado);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Cobro exitoso\nTotal: $%.2f COP", response.getTotal()), 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                cargarVehiculosActivos();
                
            } else {
                txtResultado.setText("❌ ERROR: " + response.getMessage() + "\n");
                JOptionPane.showMessageDialog(this, 
                    response.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            txtResultado.setText("❌ ERROR: " + ex.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, 
                "Error al procesar cobro: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}