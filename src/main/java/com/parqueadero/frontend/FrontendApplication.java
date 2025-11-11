package com.parqueadero.frontend;

import com.parqueadero.frontend.window.MainFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.swing.*;

/**
 * Clase principal que inicia el contexto de Spring Boot y muestra
 * la interfaz gráfica de Swing en el Event Dispatch Thread.
 */
@SpringBootApplication
public class FrontendApplication {

    public static void main(String[] args) {
        // Desactivar modo headless para permitir GUI
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(FrontendApplication.class, args);
    }

    /**
     * CommandLineRunner que se ejecuta después de que Spring Boot
     * haya inicializado completamente el contexto de la aplicación.
     * 
     * Muestra la ventana principal en el Event Dispatch Thread de Swing.
     */
    @Bean
    @Order(1)
    public CommandLineRunner showMainFrame(MainFrame mainFrame) {
        return args -> {
            SwingUtilities.invokeLater(() -> {
                mainFrame.initializeUI();
                mainFrame.setVisible(true);
            });
        };
    }
}