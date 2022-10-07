
package timebound;
/*
    Hacer una clase BattleAbleEntity?
*/
public class PlayerCharacter extends BattleAbleOverworldEntity{
    /*
    private static final StringBuffer[] BATTLE_TEXT = new StringBuffer[] {
    new StringBuffer("Ness ataca!"),
    new StringBuffer("hp menos para el enemigo!"),
    new StringBuffer("Ness pierde "),
    new StringBuffer("hp "),
    new StringBuffer("Ness abrio su bolsa!..._Pero no habia nada dentro."),
    new StringBuffer("Ness se prepara para el ataque_del enemigo!"),
    new StringBuffer("Ness intenta escapar de la pelea..._"),
    new StringBuffer("PSI Rockin"),
    new StringBuffer("Lifeup"),
    new StringBuffer("Alpha"),
    new StringBuffer("Beta"),
    new StringBuffer("Gamma")
    };*/
    
    private int lastXMove;
    private int lastYMove;
    private int menuYPosition;
    private int menuXPosition;
    
    private KeyboardFunctions keyboard;
    
    private int currentStatus;
    /*  What is the player doing?
     *  0) Walking
     *  1) On Menu
     *  2) Reading text
     *  3) Unable to do anything
     *
    */
    
    private int playerChoice;
    /*
     *  -1) Calcelar / No
     *   0) Idle
     *   1) Aceptar / Si
    */
    
    //// Battle variables ////
    
    private int level;
    private boolean ableToFight;
    
    private int typeOfPSI;
    /*
     * 0) Not using PSI
     * 1) Offensive PSI
     * 2) Defensive PSI
     * 3) Assist PSI
    */
    private int psiMove;
    /*
     * 0) PSI Rockin'
     * 1) PSI Flash
     * 2) Lifeup
     * 3) Healing
     * 4) 
    */
    
    public PlayerCharacter() {
        super(1540, 700, "Ness", "Ness_U2", 2, 8, 16, 12, 0, 12, 45, 20, 10, 4,
                9, 14);        
        super.setCurrentSpriteRowAndColumn(0, 4); //first sprite of down movement
        
        this.level = 18;
        
        //Debera tener:
        //Lifeup: Restores 75-125 HP - single ally
        //Rockin: 40-120 damage to all enemies
        //Flash: Status effects to all enemies (probabilidad)
        //Hypnosis: Puts one enemy to sleep (prob.)
        //Healing: Heals cold / sunstroke / sleep
        //Shield: Shields one ally
        //Paralysis: Paralyzes one enemy

        this.currentStatus = 0;
        
        this.keyboard = new KeyboardFunctions(); 
        
        //Action array de bools, para saber cual movimiento eligio, se ejecuta en el cobat manager
        //Un estado bool, para saber si esta en combate, o con CS, dont know.
        
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getTypeOfPSI() {
        return typeOfPSI;
    }
    
    public void setTypeOfPSI(int typeOfPSI) {
        this.typeOfPSI = typeOfPSI;
    }
    
    public int getPSIMove() {
        return psiMove;
    }
    
    public void setPSIMove(int psiMove) {
        this.psiMove = psiMove;
    }
    
    
    
    
    
    
    
    
    
    
    public void updatePlayerCharacter() {
        
        if(currentStatus != 3) {    
            if(keyboard.getActionStatus()) {
                playerChoice = 1;
                keyboard.toggleAction();
            } else if(keyboard.getCancelStatus()) {
                playerChoice = -1;
                keyboard.toggleCancel();
            } else if(keyboard.getMenuStatus()) {
                if(currentStatus == 1) { //Pause button
                    currentStatus = 0;
                } else {
                    currentStatus = 1;
                    menuYPosition = 0;
                }
                keyboard.toggleMenu();
            }

            if(currentStatus == 0) {
                if(keyboard.isPressingKeys()) {
                    lastXMove = keyboard.getXCoordIncrement();
                    lastYMove = keyboard.getYCoordIncrement();

                    setXCoord(getXCoord() + lastXMove);
                    setYCoord(getYCoord() + lastYMove);
                }

                this.setCurrentSpriteColumn(keyboard.getDirection());
                this.setCurrentSpriteRow(keyboard.getAlternateSprite());
            } else if(currentStatus == 1) {

                if(keyboard.isPressingKeys()) {

                    if(keyboard.isMoving(KeyboardFunctions.LEFT)) {
                        menuXPosition--;
                        keyboard.releaseKey(KeyboardFunctions.LEFT);
                    } else if(keyboard.isMoving(KeyboardFunctions.UP)) {
                        menuYPosition--;
                        keyboard.releaseKey(KeyboardFunctions.UP);
                    } else if(keyboard.isMoving(KeyboardFunctions.RIGHT)) {
                        menuXPosition++;
                        keyboard.releaseKey(KeyboardFunctions.RIGHT);
                    } else if(keyboard.isMoving(KeyboardFunctions.DOWN)) {
                        menuYPosition++;
                        keyboard.releaseKey(KeyboardFunctions.DOWN);
                    }

                }
            }
        }
    }
    
    public KeyboardFunctions getKeyboard() {
        return keyboard;
    }
    
    public void revertLastMove() {
        setXCoord(getXCoord() - lastXMove);
        setYCoord(getYCoord() - lastYMove);
    }
    
    public void setCurrentStatus(int status) {
        this.currentStatus = status;
    }
    
    public int getCurrentStatus() {
        return currentStatus;
    }
    
    public int getMenuYPosition() {
        return menuYPosition;
    }
    
    public int getMenuXPosition() {
        return menuXPosition;
    }
    
    public void setMenuYPosition(int yPosition) {
        this.menuYPosition = yPosition;
    }
    
    public void setMenuXPosition(int xPosition) {
        this.menuXPosition = xPosition;
    }
    
    public int getPlayerChoice() {
        return playerChoice;
    }
    
    public void setOPlayerChoice(int playerChoice) {
        this.playerChoice = playerChoice;
    }
    
    public void resetPlayerChoice() {
        playerChoice = 0;
    }
    
    
    
    
    
    
    
    /*
    public int calculateDamage(int move) {
        
    }*/
    
    //Calculate damage;
}
