/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timebound;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Victor
 */
public class NewClass {
    
    public boolean miss;
    public int missValue;
    
    public void missChance() {
        
        missValue = ThreadLocalRandom.current().nextInt(0, 16);
        
        if(missValue == 14) { //numero al azar, pero solo uno, para representar 1/16 de probabilidad
            miss = true;
        }
        
        
        /*
         * 
         *
         *
        */
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
