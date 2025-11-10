package com.parqueadero.frontend.window;

import com.parqueadero.frontend.client.VehiculoApiClient;
import com.parqueadero.frontend.dto.VehiculoDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestionar vehículos
 * Permite registrar, listar y eliminar vehículos
 */
public class VehiculoPanel extends JPanel {

    private final VehiculoApiClient vehiculoService;
    
    private JTextField txtPlaca;
    private JTextField txtModelo;
    private JComboBox<String> comboTipo;
    private JTable tableVehiculos;
    private DefaultTableModel tableModel;

    public VehiculoPanel(VehiculoApiClient vehiculoService) {
        this.vehiculoService = vehiculoService;
        initializeUI();
        cargarVehiculos();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de formulario
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Vehículo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Placa
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Placa:"), gbc);
        
        gbc.gridx = 1;
        txtPlaca = new JTextField(15);
        panel.add(txtPlaca, gbc);

        // Modelo
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Modelo:"), gbc);
        
        gbc.gridx = 3;
        txtModelo = new JTextField(15);
        panel.add(txtModelo, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1;
        comboTipo = new JComboBox<>(new String[]{"Carro", "Moto", "Bicicleta", "Camioneta"});
        panel.add(comboTipo, gbc);

        // Botones
        gbc.gridx = 2; gbc.gridy = 1;
        JButton btnRegistrar = new JButton("✓ Registrar");
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(e -> registrarVehiculo());
        panel.add(btnRegistrar, gbc);

        gbc.gridx = 3;
        JButton btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarVehiculos());
        panel.add(btnActualizar, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Vehículos"));

        String[] columnas = {"ID", "Placa", "Modelo", "Tipo", "Ingreso", "Estado", "Total Pagado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableVehiculos = new JTable(tableModel);
        tableVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableVehiculos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEliminar = new JButton("🗑️ Eliminar Seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        actionPanel.add(btnEliminar);
        
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void registrarVehiculo() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        String modelo = txtModelo.getText().trim();
        String tipo = (String) comboTipo.getSelectedItem();

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "La placa es obligatoria", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            vehiculoService.registrar(placa, modelo, tipo);
            JOptionPane.showMessageDialog(this, 
                "Vehículo registrado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            txtPlaca.setText("");
            txtModelo.setText("");
            cargarVehiculos();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVehiculos() {
        try {
            List<VehiculoDTO> vehiculos = vehiculoService.obtenerTodos();
            tableModel.setRowCount(0);
            
            for (VehiculoDTO v : vehiculos) {
                Object[] row = {
                    v.getId(),
                    v.getPlaca(),
                    v.getModelo() != null ? v.getModelo() : "-",
                    v.getTipo(),
                    v.getIngreso() != null ? v.getIngreso().substring(0, 16) : "-",
                    v.getActivo() ? "Activo ✓" : "Pagado",
                    String.format("$%.2f", v.getTotalPagado())
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar vehículos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVehiculo() {
        int selectedRow = tableVehiculos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un vehículo", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String placa = (String) tableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar vehículo " + placa + "?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehiculoService.eliminar(placa);
                JOptionPane.showMessageDialog(this, 
                    "Vehículo eliminado", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarVehiculos();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}