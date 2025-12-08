/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import GUI.Server.ThreadServidor;

/**
 *
 * @author mathi
 */
public class CommandReady extends Command {

    public CommandReady(String[] parameters) {
        super(CommandType.READY, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("READY");
        threadServidor.getServer().setPlayersReady(threadServidor.getServer().getPlayersReady() + 1);
    }
    
}
