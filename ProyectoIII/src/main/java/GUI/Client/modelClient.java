/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Client;

import Console.Command;
import Console.CommandFactory;
import Console.CommandUtil;
import java.io.IOException;

/**
 *
 * @author mathi
 */
public class modelClient {

    private Client client;
    
    public modelClient(Client client) {
        this.client = client;
    }
    
    /**
     * Ahora procesa fighters localmente antes de enviar al servidor
     */
    public String comprobarComando(String comandoIngresado) {
        if (comandoIngresado.length() > 0) {
            String args[] = CommandUtil.tokenizerArgs(comandoIngresado);
            if (args.length > 0) {
                switch (args[0].toUpperCase()) {
                    default:
                        Command comando = CommandFactory.getCommand(args);
                        return enviarComandoServer(comando);
                }
            }
        }
        return "";
    }
    
    /**
     * Envía comando al servidor
     */
    public String enviarComandoServer(Command comando) {
        if (comando != null) {
            try {
                client.objectSender.writeObject(comando);
                client.objectSender.flush();
            } catch (IOException ex) {
                return "Error: No se pudo enviar el comando al servidor\n";
            }
        } else {
            return "Error: Comando desconocido\n";
        }
        return "";
    }

}