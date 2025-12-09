package Console;

import GUI.Server.ThreadServidor;

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
        threadServidor.sendConfirmation("Bienvenido al servidor, " + newName);
    }
}