/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectoiii;

import GUI.Server.Server;
import javax.swing.JOptionPane;

/**
 *
 * @author gabri
 */
public class ProyectoIII {

    public static void main(String[] args) {
        // Opciones para el diálogo de selección
        Object[] options = {"Server + 1 Client", "Client Only", "Server Only (Consola)"};
        int choice = JOptionPane.showOptionDialog(null,
                "¿Qué te gustaría iniciar?",
                "Selector de Inicio - Proyecto III",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: // "Server + 1 Client"
                Start.Start();
                break;
            case 1: // "Client Only"
                Start.startClient();
                break;
            case 2: // "Server Only (Consola)"
                System.out.println("Iniciando servidor en modo consola...");
                new Server();
                break;
        }
    }
}
