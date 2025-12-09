/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectoiii;

import GUI.Server.Server;

/**
 *
 * @author gabri
 */
public class ProyectoIII {

    public static void main(String[] args) {
        if (args.length > 0 && "--server".equals(args[0])) {
            // Iniciar en modo servidor sin GUI
            System.out.println("Iniciando servidor...");
            new Server(); // Asume que el constructor de Server inicia el servidor.
        } else {
            // Iniciar en modo cliente con GUI (comportamiento actual)
            Start.Start();
        }
    }
}
