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
public class CommandPrivateMessage extends Command{

    public CommandPrivateMessage(String[] args) {
        super(CommandType.PRIVATE_MESSAGE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
    }
    
    @Override
    public void processInClient(Client client) {
        //private_message Andres "Hola mundo"
        client.getRefFrame().writeMessage("Mensaje para " + this.getParameters()[1] + ": " + this.getParameters()[2]);
    }
}
