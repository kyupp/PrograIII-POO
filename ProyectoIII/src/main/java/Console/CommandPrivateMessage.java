/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandPrivateMessage extends Command {

    public CommandPrivateMessage(String[] args) {
        // args: [0] = "PRIVATE_MESSAGE", [1] = nombreDestino, [2..n] = texto
        super(
            CommandType.PRIVATE_MESSAGE,
            new String[]{ args[1], extractText(args) } 
        );
        
        this.setIsBroadcast(false); // Siempre privado
    }

    /**
     * Extrae el texto desde args[2] hasta el final.
     */
    private static String extractText(String[] args) {
        if (args.length < 3) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        threadServidor
            .getServer()
            .getRefFrame()
            .writeMessage(
                "[PRIVATE] De " + threadServidor.getClientName() +
                " para " + getParameters()[0] +
                ": " + getParameters()[1]
            );
    }

    @Override
    public void processInClient(Client client) {
        client.getRefFrame().writeMessage(
            "[Mensaje privado] " + getParameters()[1]
        );
    }
}
