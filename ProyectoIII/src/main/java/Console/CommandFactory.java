/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

/**
 *
 * @author diego
 */
public class CommandFactory {
    
    
    public static Command getCommand(String[] args){
        String type = args[0].toUpperCase();
        
        switch (type) {
            case "ATTACK":
                return new CommandAttack(args);
            case "MESSAGE":
                return new CommandMessage(args);
            case "PRIVATE_MESSAGE":
                return new CommandPrivateMessage(args);
            case "GIVEUP":
                return new CommandGiveup(args);
            case "NAME":
                return new CommandName(args);
            case "READY":
                return new CommandReady(args);
            case "USE_WILDCARD":
                return new CommandUseWildcard(args);
            case "RELOAD":
                return new CommandReload(args);
            case "SELECT_PLAYER":
                return new CommandSelectPlayer(args);
            case "PASS_TURN":
                return new CommandPassTurn(args);
            case "MUTAL_EXIT":
                return new CommandMutualExit(args);
            case "APPROVE_JOIN":
                return new CommandApproveJoin(args);
            case "CREATE_FIGHTER":
                return new CommandCreateFighter(args);
            default:
                return null;
        }
        
        
    }
    
}
