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
public class CommandUseWildcard extends Command {

    public CommandUseWildcard(String[] parameters) {
        super(CommandType.USE_WILDCARD, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        System.out.println("USE_WILDCARD");
        //TODO: Logica
    }
    
}
