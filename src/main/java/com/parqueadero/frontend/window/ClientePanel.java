package com.parqueadero.frontend.window;

import com.parqueadero.frontend.client.ClienteApiClient;
import com.parqueadero.frontend.dto.ClienteDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestionar clientes del parqueadero
 * Permite registrar clientes y gestionar descuentos según tipo
 */
public class ClientePanel extends JPanel {

    private final ClienteApiClient clienteService;
    
    private JTextField txtNombre;
    private JTextField txtDocumento;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JComboBox<String> comboTipoCliente;
    private JSpinner spinnerDescuento;
    private JTable tableClientes;
    private DefaultTableModel tableModel;

    public ClientePanel(ClienteApiClient clienteService) {
        this.clienteService = clienteService;
        initializeUI();
        cargarClientes();
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
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Documento
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Documento:"), gbc);
        
        gbc.gridx = 3;
        txtDocumento = new JTextField(15);
        panel.add(txtDocumento, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        panel.add(txtTelefono, gbc);

        // Email
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 3;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Tipo de Cliente
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tipo de Cliente:"), gbc);
        
        gbc.gridx = 1;
        comboTipoCliente = new JComboBox<>(new String[]{"Eventual", "Regular", "VIP"});
        panel.add(comboTipoCliente, gbc);

        // Descuento
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Descuento (%):"), gbc);
        
        gbc.gridx = 3;
        SpinnerModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 5.0);
        spinnerDescuento = new JSpinner(spinnerModel);
        panel.add(spinnerDescuento, gbc);

        // Botones
        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnRegistrar = new JButton("✓ Registrar Cliente");
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.addActionListener(e -> registrarCliente());
        panel.add(btnRegistrar, gbc);

        gbc.gridx = 2;
        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        btnLimpiar.setBackground(new Color(149, 165, 166));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panel.add(btnLimpiar, gbc);

        gbc.gridx = 3;
        JButton btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarClientes());
        panel.add(btnActualizar, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Clientes Registrados"));

        String[] columnas = {"ID", "Nombre", "Documento", "Teléfono", "Email", "Tipo", "Descuento"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableClientes = new JTable(tableModel);
        tableClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarCliente() {
        String nombre = txtNombre.getText().trim();
        String documento = txtDocumento.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String tipoCliente = (String) comboTipoCliente.getSelectedItem();
        Double descuento = (Double) spinnerDescuento.getValue();

        if (nombre.isEmpty() || documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nombre y Documento son obligatorios", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            clienteService.registrar(
                nombre, 
                documento, 
                telefono.isEmpty() ? null : telefono,
                email.isEmpty() ? null : email,
                tipoCliente,
                descuento
            );
            
            JOptionPane.showMessageDialog(this, 
                "Cliente registrado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarFormulario();
            cargarClientes();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar cliente: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.obtenerTodos();
            tableModel.setRowCount(0);
            
            for (ClienteDTO c : clientes) {
                Object[] row = {
                    c.getId(),
                    c.getNombre(),
                    c.getDocumento(),
                    c.getTelefono() != null ? c.getTelefono() : "-",
                    c.getEmail() != null ? c.getEmail() : "-",
                    c.getTipoCliente(),
                    String.format("%.1f%%", c.getDescuento())
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar clientes: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDocumento.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        comboTipoCliente.setSelectedIndex(0);
        spinnerDescuento.setValue(0.0);
    }
}