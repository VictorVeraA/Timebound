/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timebound;

/**
 *
 * @author SERVIDOR
 */
public abstract class BattleAbleOverworldEntity extends OverworldEntity{
    
    private int healthPoints;
    private int powerPoints;
    private int offense;
    private int defense;
    private int speed;
    private int guts;
    private boolean ableToFight;
    private int selectedMove;
    /*
     * 0) Do nothing
     * 1) Attack
     * 2) Cure
     * 3) Assist, buff, debuff
    */
    
    private int categoryOfMoveSelected;

    public BattleAbleOverworldEntity(int xCoord, int yCoord, String name,
            String spriteSheetName, int rowsOfSprites, int columnsOfSprites,
            int hitboxWidth, int hitboxHeigth, int hitboxXCoord, int hitboxYCoord,
            int healthPoints, int powerPoints, int offense, int defense, int speed,
            int guts) {
        
        super(xCoord, yCoord, name, spriteSheetName, rowsOfSprites, columnsOfSprites,
                hitboxWidth, hitboxHeigth, hitboxXCoord, hitboxYCoord);
        
        this.healthPoints = healthPoints;
        this.powerPoints = powerPoints;
        this.offense = offense;
        this.defense = defense;
        this.speed = speed;
        this.guts = guts;
        
        this.ableToFight = true;
    }
    
    public void reduceHealthPoints(int reduction) {
        healthPoints -= reduction;
        
        if(healthPoints < 0) {
            ableToFight = false;
            healthPoints = 0;
        }
    }

    public void reducePowerPoints(int reduction) {
        powerPoints -= reduction;
        
        if(powerPoints < 0) {
            powerPoints = 0;
        }
    }
    
    public int getHealthPoints() {
        return healthPoints;
    }
    
    public void setHealthPoints(int healthPoints) {
        
        if(healthPoints < 0) {
            ableToFight = false;
            healthPoints = 0;
        }
        
        this.healthPoints = healthPoints;
    }
    
    public int getPowerPoints() {
        return powerPoints;
    }
    
    public void setPowerPoints(int powerPoints) {
        this.powerPoints = powerPoints;
    }
    
    public int getOffense() {
        return offense;
    }
    
    public void setOffense(int offense) {
        this.offense = offense;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public int getGuts() {
        return guts;
    }
    
    public void setGuts(int guts) {
        this.guts = guts;
    }
   
    public boolean isAbleToFight() {
        return ableToFight;
    }
    
    public void setAbleToFight(boolean condition) {
        this.ableToFight = condition;
    }
    
    public int getSelectedMove() {
        return selectedMove;
    }
    
    public void setSelectedMove(int selectedMove) {
        this.selectedMove = selectedMove;
    }
 
    public int getCategoryOfSelectedMove() {
        return categoryOfMoveSelected;
    }
    
    public void setCategoryOfSelectedMove(int categoryOfMoveSelected) {
        this.categoryOfMoveSelected = categoryOfMoveSelected;
    }
    
}
