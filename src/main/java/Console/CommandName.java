package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import Player.Player;

public class CommandName extends Command {

    public CommandName(String[] args) {
        super(CommandType.NAME, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // Validar que venga el nombre
        if (getParameters().length < 2) return;

        String newName = getParameters()[1];

        // 1. Asignar el nombre al Hilo para identificarlo en los logs y búsquedas
        threadServidor.setClientName(newName);

        // 2. Confirmar al servidor y al cliente
        threadServidor.getServer().getRefFrame().writeMessage("Cliente identificado como: " + newName);
        
        // 3. Notificar al cliente que el nombre fue registrado
        threadServidor.sendConfirmation("Nombre '" + newName + "' registrado en el servidor.");
    }

    @Override
    public void processInClient(Client client) {
        String name = getParameters()[1];
        // The localPlayer is already initialized in ClientFrame constructor
        client.getRefFrame().getLocalPlayer().setId(name); // Ensure the local player object has the correct ID
        client.getRefFrame().setPlayerID(name); // Update UI display
        client.getRefFrame().appendLog("Identificado como: " + name);
    }
}