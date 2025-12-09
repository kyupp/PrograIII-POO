/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Player;

import Fighters.Fighter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabri
 */
public class Player {
    private String id;
    private List<Fighter> team;

    // Scores
    private boolean isAlive = true;
    private int successfulAttacks;
    private int failedAttacks;
    private int deaths;
    private int wins;
    private int defeats;
    private int surrenders;

    public Player(String id) {
        this.id = id;
        this.team = new ArrayList<>();
    }

    public void addFighter(Fighter fighter) { 
        team.add(fighter); 
    }

    public String getId() {
        return id;
    }

    public List<Fighter> getTeam() {
        return team;
    }

    public int getSuccessfulAttacks() {
        return successfulAttacks;
    }

    public int getFailedAttacks() {
        return failedAttacks;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getDefeats() {
        return defeats;
    }

    public int getSurrenders() {
        return surrenders;
    }

    public boolean isAlive() {
        return isAlive;
    }
    
    public void surrender() {
        this.surrenders++;
        this.isAlive = false;
    }
    
}
