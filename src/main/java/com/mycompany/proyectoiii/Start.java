/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoiii;

import GUI.Client.ClientFrame;
import GUI.Server.FrameServer;

/**
 *
 * @author mathi
 */
public class Start {
    public static void Start(){
        startServer();
        startClient();
    }

    public static void startServer() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameServer().setVisible(true);
            }
        });
    }

    public static void startClient() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }
}
