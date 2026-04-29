/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.grafo_oaxaca;

import javax.swing.JFrame;

/**
 *
 * @author Dario
 */
public class NewClass {
    public static void main(String[] args) {
        // 1. Configurar la ventana principal
        JFrame ventana = new JFrame("Mapa de Rutas - Oaxaca");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1300, 950); // Un poco más grande que el panel
        ventana.setLocationRelativeTo(null); // Centrar en pantalla

        // 2. Instanciar el panel de presentación que creamos
        PanelMapa mapaOaxaca = new PanelMapa();
        ventana.add(mapaOaxaca);
        ventana.setVisible(true);
    }
}
