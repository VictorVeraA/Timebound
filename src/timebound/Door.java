/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timebound;

/**
 *
 * @author Victor
 */
public abstract class Door extends Structures{
    
    private int mapIndex; //A donde te lleva
    private int appearIn;
    
    public Door(int xCoord, int yCoord, String name, String spriteSheetName,
            int rowsOfSprites, int columnsOfSprites, int hitboxWidth, int hitboxHeigth,
            int hitboxXCoord, int hitboxYCoord) {
        
        super(xCoord, yCoord, name, spriteSheetName, rowsOfSprites, columnsOfSprites,
                hitboxWidth, hitboxHeigth, hitboxXCoord, hitboxYCoord);
    }
    
}
