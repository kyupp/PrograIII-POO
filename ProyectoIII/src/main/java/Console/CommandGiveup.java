/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Server.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandGiveup  extends Command{

    public CommandGiveup(String[] args) {
        super(CommandType.GIVEUP, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        threadServidor.isActive = false;
        sendResponse(threadServidor, "Usted ha decidido retirarse :<");
        
        CommandMessage msg = new CommandMessage(
                new String[]{"MESSAGE", "El usuario: " + threadServidor.name + " ha decidio retirarse", "true"}
        );
        
        threadServidor.getServer().broadcast(msg);
    }
    
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
    
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
}
