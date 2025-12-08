/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandReadyRequest extends Command {

    public CommandReadyRequest(String playerName) {
        super(CommandType.READY_REQUEST, new String[]{playerName});
        this.setIsBroadcast(false);
    }

    @Override
    public void processInClient(Client client) {
        String playerName = getParameters()[0];
        client.getRefFrame().writeMessage(
            "El jugador '" + playerName + "' quiere unirse. "
            + "Escribe: /approve " + playerName + " yes | no"
        );
    }

    @Override
    public void processForServer(ThreadServidor ts) {
        // Esto no se procesa en servidor → solo en cliente host
    }
}

