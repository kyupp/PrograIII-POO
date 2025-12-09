/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fighters;

import Fighters.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author gabri
 */
public class Weapon {
    private String name;
    private Map<Type, Integer> typePercentage;
    private boolean used;

    public Weapon(String name, Map typePercentage, boolean used) {
        this.name = name;
        this.typePercentage = new HashMap<>();
        this.used = used;
    }
    
    public int getDamageVs(Type type){
        return typePercentage.get(type);
    }
    
    public void generateRandomPercentaje(){
        for(Type t: Type.values()){
            int p = 20 + new Random().nextInt(81);
            typePercentage.put(t, p);
        }
    }
    
    public void setUsed() {
        used = true;
    }
    
    public void unSetUsed(){
        used = false;
    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return used;
    }

}
