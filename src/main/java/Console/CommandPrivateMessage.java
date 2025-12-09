/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Client.Client;
import GUI.Server.ThreadServidor;
import GUI.Server.Server;
import javax.swing.SwingUtilities;
import java.io.IOException;

public class CommandPrivateMessage extends Command {
    /**
     * args: [0] = "PRIVATE_MESSAGE", [1] = nombreDestino, [2..n] = texto
     * Pero internamente usaremos parameters:
     * [0]=remitente (se rellenará en server.processForServer)
     * [1]=destinatario
     * [2]=texto
     */
    public CommandPrivateMessage(String[] args) {
        super(CommandType.PRIVATE_MESSAGE, buildParameters(args));
        this.setIsBroadcast(false);
    }

    private static String[] buildParameters(String[] args) {
        if (args == null || args.length < 3) {
            // mal uso: devolver arreglo seguro
            return new String[] {"", "", ""};
        }

        String destinatario = args[1];
        // reconstruir texto desde args[2]..args[n-1]
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String texto = sb.toString().trim();

        // remitente lo pondrá el servidor (o el cliente lo puede rellenar antes de enviar)
        return new String[] {"", destinatario, texto};
    }

    /**
     * Cuando el servidor recibe este comando, debe rellenar el remitente (nombre del thread)
     * y luego enviar el objeto Command al destinatario encontrado.
     */
    @Override
    public void processForServer(ThreadServidor threadServidor) {
        Server server = threadServidor.getServer();

        // rellenar remitente
        String remitente = threadServidor.getClientName();
        String destinatario = getParameters()[1];
        String texto = getParameters()[2];

        // actualizar parameters para que el cliente receptor lo lea correctamente
        getParameters()[0] = remitente;

        server.getRefFrame().writeMessage(
            "[PRIVATE] De " + remitente + " para " + destinatario + ": " + texto
        );

        // Since ThreadServidor no longer calls executeCommand, we handle sending here.
        // The server's sendPrivate method will find the target and send 'this' command.
        server.sendPrivate(this);
    }

    @Override
    public void processInClient(Client client) {
        // cliente receptor recibe esto
        String remitente = getParameters().length > 0 ? getParameters()[0] : "";
        String texto = getParameters().length > 2 ? getParameters()[2] : "";

        SwingUtilities.invokeLater(() -> {
            client.getRefFrame().appendLog("[Privado] " + remitente + ": " + texto);
        });
    }
}
