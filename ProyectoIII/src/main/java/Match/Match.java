/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Match;

import Console.Command;
import Console.CommandMessage;
import Player.Player;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author gabri
 */
public class Match {
    private List<Player> players;
    private Player owner;
    private int actualTurn = -1;
    private Map<Player, List<String>> logs;
    private Player winner;

    public Match(Player owner) {
        this.players = new ArrayList<>();
        this.players.add(owner);
        this.owner = owner;
        this.logs = new HashMap<>();
    }

    public int getActualTurn() {
        return actualTurn;
    }
    
    public void addPlayer(Player player){
        players.add(player);
        logs.put(player, new ArrayList<>());
    }
    
    public Player getPlayerInTurn(){
        return players.get(this.actualTurn);
    }
    
    public void nextTurn(){
        this.actualTurn = (this.actualTurn + 1)%players.size();
    }

    public Player getOwner() {
        return owner;
    }
    
    public void log(Player player, String text) {
        logs.get(player).add(LocalDateTime.now() + " - " + text);
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public Command startGame(){
        Random rand = new Random();
        actualTurn = rand.nextInt(players.size());
        
        String actualPlayer = players.get(actualTurn).getId();
        
        String[] message = {"MESSAGE", "Turno del jugador: " + actualPlayer, "true"};
        
        System.out.println("PARTIDA INICIADA");
        
        return new CommandMessage(message);    
    }
    
    public Player getPlayer(String ID){
        for (Player player : this.players){
            if (player.equals(ID)){
                return player;
            }
        }
        return null;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
    
    
}
