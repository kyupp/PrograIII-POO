/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Console.Command;
import Console.CommandType;
import GUI.Server.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandAttack extends Command{

    public CommandAttack(String[] args) { //ATTACK Andres 5 7
        super(CommandType.ATTACK, args);
    }
    
    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
    }
            
//    @Override
//    public void processInClient(Client client) {
//        System.out.println("Procesando un attack");
//    }
    
    
}
