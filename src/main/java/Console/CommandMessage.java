/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import javax.swing.SwingUtilities;
import java.util.Arrays;

/**
 *
 * @author diego
 */
public class CommandMessage extends Command {

    /**
     * args:
     * 0 = "MESSAGE"
     * 1 = mensaje...
     * n-1 = "true" / "false"  (broadcast)
     */
    public CommandMessage(String[] args) {
        super(
            CommandType.MESSAGE,
            buildParameters(args)
        );

        boolean broadcast = Boolean.parseBoolean(args[args.length - 1]);
        this.setIsBroadcast(broadcast);
    }

    /**
     * parameters = [senderName, messageText]
     *
     * Esto es CRUCIAL porque el SERVER usa:
     * parameters[1] para buscar el destino en mensajes privados.
     */
    private static String[] buildParameters(String[] args) {

        // Seguridad: mínimo → MESSAGE mensaje broadcast
        if (args.length < 3)
            return new String[]{"", ""};

        // Reconstruir texto desde args[1] hasta args[n-2]
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            sb.append(args[i]).append(" ");
        }
        String texto = sb.toString().trim();

        // IMPORTANTE:
        // el parámetro 0 lo pone el client en processInClient
        // aquí sólo dejamos espacio
        return new String[]{"", texto};
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {

        // AQUI colocamos EL REMITENTE
        getParameters()[0] = threadServidor.getClientName();

        threadServidor.getServer().getRefFrame().writeMessage(
            "[MSG] " + getParameters()[0] + ": " + getParameters()[1]
        );

        // Since ThreadServidor no longer calls executeCommand, we do it here.
        if (isIsBroadcast()) {
            threadServidor.getServer().broadcast(this);
        }
    }

    @Override
    public void processInClient(Client client) {

        // Cuando lo recibe el cliente, solo imprime:
        String sender = (getParameters().length > 0) ? getParameters()[0] : "Servidor";
        String msg = (getParameters().length > 1) ? getParameters()[1] : "";

        final String finalMsg;
        if (sender == null || sender.isEmpty() || sender.equalsIgnoreCase("Servidor")) {
            finalMsg = msg; // Mensajes del sistema (errores, confirmaciones)
        } else {
            finalMsg = sender + ": " + msg; // Mensajes de otros jugadores
        }

        SwingUtilities.invokeLater(() -> {
            client.getRefFrame().appendLog(finalMsg);
        });
    }
}
