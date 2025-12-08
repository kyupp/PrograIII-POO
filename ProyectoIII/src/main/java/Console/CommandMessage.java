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
public class CommandMessage extends Command{

    public CommandMessage(String[] args) {
        // args: [0] = "MESSAGE", args[1..n-2] = texto, args[n-1] = broadcast
        super(CommandType.MESSAGE, new String[]{ extractText(args) });

        // Convertir último argumento a boolean
        boolean broadcast = Boolean.parseBoolean(args[args.length - 1]);
        this.setIsBroadcast(broadcast);
    }

    /** 
     * Reconstruye el texto tomando todo entre args[1] y args[n-2]
     */
    private static String extractText(String[] args) {
        if (args.length < 3) { 
            return ""; 
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        threadServidor.getServer().getRefFrame()
            .writeMessage("[MSG] " + getParameters()[0] + " | Broadcast=" + isIsBroadcast());
    }
    
    @Override
    public void processInClient(Client client) {
        //Message "string"
        client.getRefFrame().writeMessage("Mensaje recibido: " + this.getParameters()[0]);
    }
    
}
