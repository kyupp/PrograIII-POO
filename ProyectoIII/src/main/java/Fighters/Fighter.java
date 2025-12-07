/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabri
 */
public class Fighter {
    private String name;
    private Type type;
    private int life;  // inicia en 100%
    private List<Weapon> weapons;

    public Fighter(String name, Type type) {
        this.name = name;
        this.type = type;
        this.life = 100;
        this.weapons = new ArrayList<>();
    }

    public void addWeapon(Weapon weapon) { 
        weapons.add(weapon); 
    }

    public void takeDamage(int dmg) {
        life = Math.max(0, life - dmg);
    }

    public boolean isDead() { 
        return life == 0; 
    }
}
