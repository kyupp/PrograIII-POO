/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import Fighters.Fighter;
import Fighters.Weapon;
import GUI.Server.ThreadServidor;
import Player.Player;

/**
 *
 * @author mathi
 */
public class CommandReload extends Command {

    public CommandReload(String[] parameters) {
        super(CommandType.RELOAD, parameters);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
        if (!threadServidor.isActive){
            sendResponse(threadServidor, "ERROR: Usted no esta participando en el juego.");
            return;
        }
        
        Player player = threadServidor.getGamePlayer();
        
        if (!usedWeapons(player)){
            sendResponse(threadServidor, "ERROR: Usted todavia posee armas.");
            return;
        }
        
        
        for (Fighter fighter : player.getTeam()){
            for (Weapon weapon : fighter.getWeapons()){
                weapon.unSetUsed();
            }
        }
        
        sendResponse(threadServidor, "Armms cargadas correctamente!");
    }
    
    public boolean usedWeapons(Player player){
        for (Fighter fighter : player.getTeam()){
            for (Weapon weapon : fighter.getWeapons()){
                if (!weapon.isUsed()){
                    return false;
                }
            }
        }
        return true;
    }
    
    private void sendResponse(ThreadServidor thread, String message) {
        try {
            thread.sendPrivateMessage(message);
        } catch (Exception e) {
            System.out.println("Error enviando respuesta: " + e.getMessage());
        }
    }
    
}
