/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Match;

import Player.Player;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabri
 */
public class Match {
    private List<Player> players;
    private Player owner;
    private int actualTurn;
    private Map<Player, List<String>> logs;
    private Player winner;

    public Match(Player owner) {
        this.players = new ArrayList<>();
        this.players.add(owner);
        this.owner = owner;
        this.logs = new HashMap<>();
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
