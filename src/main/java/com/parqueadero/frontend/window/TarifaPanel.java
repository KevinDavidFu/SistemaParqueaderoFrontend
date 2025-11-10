package com.parqueadero.frontend.window;

import com.parqueadero.frontend.client.TarifaApiClient;
import com.parqueadero.frontend.dto.TarifaDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestionar tarifas
 * Permite crear y listar tarifas por tipo de vehículo
 */
public class TarifaPanel extends JPanel {

    private final TarifaApiClient tarifaService;
    
    private JTextField txtTipo;
    private JTextField txtPrecio;
    private JTable tableTarifas;
    private DefaultTableModel tableModel;

    public TarifaPanel(TarifaApiClient tarifaService) {
        this.tarifaService = tarifaService;
        initializeUI();
        cargarTarifas();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Crear Nueva Tarifa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tipo
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tipo de Vehículo:"), gbc);
        
        gbc.gridx = 1;
        txtTipo = new JTextField(15);
        panel.add(txtTipo, gbc);

        // Precio
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Precio por Hora (COP):"), gbc);
        
        gbc.gridx = 3;
        txtPrecio = new JTextField(15);
        panel.add(txtPrecio, gbc);

        // Botones
        gbc.gridx = 1; gbc.gridy = 1;
        JButton btnCrear = new JButton("✓ Crear Tarifa");
        btnCrear.setBackground(new Color(46, 204, 113));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.addActionListener(e -> crearTarifa());
        panel.add(btnCrear, gbc);

        gbc.gridx = 3;
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarTarifas());
        panel.add(btnActualizar, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tarifas Registradas"));

        String[] columnas = {"ID", "Tipo", "Precio/Hora", "Estado", "Fecha Creación"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableTarifas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableTarifas);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void crearTarifa() {
        String tipo = txtTipo.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (tipo.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Todos los campos son obligatorios", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double precio = Double.parseDouble(precioStr);
            
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "El precio debe ser mayor a 0", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            tarifaService.crear(tipo, precio);
            JOptionPane.showMessageDialog(this, 
                "Tarifa creada exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            txtTipo.setText("");
            txtPrecio.setText("");
            cargarTarifas();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "El precio debe ser un número válido", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al crear tarifa: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTarifas() {
        try {
            List<TarifaDTO> tarifas = tarifaService.obtenerTodas();
            tableModel.setRowCount(0);
            
            for (TarifaDTO t : tarifas) {
                Object[] row = {
                    t.getId(),
                    t.getTipo(),
                    String.format("$%.2f", t.getPrecioPorHora()),
                    t.getActiva() ? "Activa ✓" : "Inactiva",
                    t.getCreadoEn() != null ? t.getCreadoEn().substring(0, 10) : "-"
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar tarifas: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}