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
    private int actualTurn;
    private Map<Player, List<String>> logs;
    private Player winner;
    private boolean isGameStarted = false;

    public Match(Player owner) {
        this.players = new ArrayList<>();
        this.players.add(owner);
        this.owner = owner;
        this.logs = new HashMap<>();
        this.logs.put(owner, new ArrayList<>());
        this.actualTurn = 0;
    }

    public void addPlayer(Player player){
        players.add(player);
        logs.put(player, new ArrayList<>());
    }

    public Player getPlayerInTurn(){
        if (players.isEmpty()) return null;
        return players.get(this.actualTurn);    
    }
    
    public int getActualTurn() {
        return actualTurn;
    }

    public void nextTurn(){
        if (players.isEmpty()) return;
        
        // Avanza al siguiente jugador, saltando a los que ya no están vivos/activos.
        do {
            this.actualTurn = (this.actualTurn + 1) % players.size();
        } while (!players.get(actualTurn).isAlive());
    }

    public Player getOwner() { return owner; }
    public List<Player> getPlayers() { return players; }
    public Player getWinner() { return winner; }
    public void setWinner(Player winner) { this.winner = winner; }

    /**
     * Busca un jugador por su ID (Nombre).
     */
    public Player getPlayer(String ID){
        for (Player player : this.players){
            if (player.getId().equals(ID)){
                return player;
            }
        }
        return null;
    }

    public void log(Player player, String text) {
        if (logs.containsKey(player)) {
            logs.get(player).add(LocalDateTime.now() + " - " + text);
        }
    }

    // Métodos auxiliares
    public boolean isMyTurn(Player p) {
        return getPlayerInTurn() != null && getPlayerInTurn().getId().equals(p.getId());
    }
    
    /**
     * Inicia la partida, elige un turno al azar y devuelve un comando para notificar.
     */
    public Command startGame(){
        if (players.size() < 2) {
            // No se puede iniciar con menos de 2 jugadores.
            return new CommandMessage(new String[]{"MESSAGE", "Se necesitan al menos 2 jugadores para iniciar.", "false"});
        }
        
        this.isGameStarted = true;
        this.actualTurn = new Random().nextInt(players.size());
        
        String actualPlayerName = players.get(actualTurn).getId();
        String[] message = {"MESSAGE", "¡La partida ha comenzado! Turno de: " + actualPlayerName, "true"};
        
        return new CommandMessage(message);    
    }
    public boolean isGameStarted() { return isGameStarted; }
}
