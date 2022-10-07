
package timebound;

import java.awt.BorderLayout;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Victor
 */
public class TimeBound implements Runnable{
    
    private static final long MPF = 16; //milliseconds Per Frame, 60 FPS
    
    private final JFrame gameFrame;
    private GameDisplayPane gameWindow;
    private static final int WINDOW_WIDTH = 512; 
    private static final int WINDOW_HEIGTH = 480;
    
    private static int gameStatus;
    private static int oldGameStatus;
    /*
     * 0) Walking
     * 1) Fighting
     * 2} In transision to Fighting
     * 3) In transition to Overworld
    */
    
    private int battleMenuIndex;
    /*
     * 0) general
     * 1) general > PSI types
     * 2) general > PSI types > Select PSI
     * 3) general > Goods 
    */
    
    private final Thread graphicThread;
    private Reproductor mp3;
    
    private int globalXCoord;
    private int globalYCoord;
    private PlayerCharacter ness;
    private Vector<OverworldEntity> worldEntitysArray;
    private int entityInContactWithPlayerIndex;
    private int entityInCombatWithPlayerIndex;
    
    private static boolean isGameRunning;
    
    private long timeBeforeLoop;
    private long timeDifference;
    private long sleepTime;
    
    int i;
    
    ////// Fresh variables, right out of the oven //////
    
    private int dialogCounter;
    private Thread sfxSoundThread;
    private Reproductor sfxPlayer;
    private boolean playBattleStart;
    private int damageDealt;
    private boolean endOfBattle;
    
    //// Battle variables ////
    
    private Vector<BattleAbleOverworldEntity> entitysInCombat;
    private Vector<EnemyEntity> enemiesInCombat;
    //private Vector<Allies> alliesInCombat;   Not yet, Mr. Anderson
    private boolean endOfPlayerTurn;
    
    public int healthUpdateDialogIndex;
    
    //Player TO enemy //
    private int playerAttackDamage;
    private int playerDamageDealt;
    private int playerDamageReduction;
    
    private int enemyAttackDamage;
    private int enemyDamageDealt;
    private int enemyDamageReduction;
    
    public TimeBound(String title) {
        gameFrame = new JFrame(title);
        gameWindow = new GameDisplayPane(WINDOW_WIDTH, WINDOW_HEIGTH);
        
        mp3 = new Reproductor("Onett_theme");
        sfxPlayer = new Reproductor();
        
        new BackgroundMap();
        new TextConverter();
        
        ness = new PlayerCharacter();
        worldEntitysArray = new Vector();
        worldEntitysArray.add(new SpitefulCrow("Spiteful Crow A", 1440, 700));
        worldEntitysArray.add(new SpitefulCrow("Spiteful Crow B", 1480, 710));
        
        graphicThread = new Thread(this);
       
        sfxSoundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isGameRunning) {
                    if(playBattleStart) {
                        sfxPlayer.playSound("starting_combat");
                        playBattleStart = false;
                    }
                    try {
                        sfxSoundThread.sleep(sleepTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TimeBound.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    
            }
            
        });
        
        entitysInCombat = new Vector();
        enemiesInCombat = new Vector();
        
        startThread();
    }
    
    public static boolean isGameRunning() {
        return isGameRunning;
    }
    
    private void initialize() {
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gameFrame.add(ness.getKeyboard());
        
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(gameWindow, BorderLayout.CENTER);
        
        gameFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGTH);
        gameFrame.setResizable(false);

        mp3.loopSound();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
            
    
    private synchronized void startThread() {
        if(isGameRunning) {
            System.out.println("Game's already running!, returning..."); 
        } else {
            isGameRunning = true;
            graphicThread.start();
            sfxSoundThread.start();
        }
    }
    
    private synchronized void stopThread() {
        if(isGameRunning)
        {
            isGameRunning = false;
            try {
                graphicThread.join();
                sfxSoundThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }        
        } else {
            System.out.println("Game's already stopped!, returning..."); 
        }
    }
    
    public void playerBattleMenu() {
        
        switch(battleMenuIndex) {
            case 0: if(ness.getMenuXPosition() == 2 && ness.getMenuYPosition() == 0) {
                        ness.setMenuXPosition(1);
                    } else {
                
                        if(ness.getMenuYPosition() > 1) { //Rangos para este menu
                            if(ness.getMenuXPosition() == 2) {
                                ness.setMenuYPosition(1);
                            } else {
                                ness.setMenuYPosition(0);
                            }
                        } else if(ness.getMenuYPosition() < 0) {
                            ness.setMenuYPosition(1);
                        }

                        if(ness.getMenuXPosition() > 2) {
                            ness.setMenuXPosition(0);
                        } else if(ness.getMenuXPosition() < 0) {
                            if(ness.getMenuYPosition() == 0) {
                                ness.setMenuXPosition(1);
                            } else {
                                ness.setMenuXPosition(2);
                            }
                        }
                    }
                    break;
                    
            case 1: if(ness.getMenuYPosition() > 2) {
                        ness.setMenuYPosition(0);
                    } else if(ness.getMenuYPosition() < 0) {
                        ness.setMenuYPosition(2);
                    }
                    if(ness.getMenuXPosition() != 0) {
                        ness.setMenuXPosition(0);
                    }
                    break;
                    
            case 2:
            case 4: if(ness.getMenuYPosition() != 0) {
                        ness.setMenuYPosition(0);
                    }
            
                    if(ness.getMenuXPosition() > 2) {
                        ness.setMenuXPosition(0);
                    } else if(ness.getMenuXPosition() < 0) {
                        ness.setMenuXPosition(2);
                    }
                    break;
                    
            case 3: if(ness.getMenuYPosition() > 1) { //Rangos para este menu
                        ness.setMenuYPosition(0);
                    } else if(ness.getMenuYPosition() < 0) {
                        ness.setMenuYPosition(1);
                    }
                    if(ness.getMenuXPosition() > 2) {
                        ness.setMenuXPosition(0);
                    } else if(ness.getMenuXPosition() < 0) {
                        ness.setMenuXPosition(2);
                    }
                    break;
        } //Hasta aqui todo bien
        
        if(ness.getPlayerChoice() == -1) {
            switch(battleMenuIndex) {
                case 1: battleMenuIndex = 0;
                        gameWindow.setLocationInMapHierarchy(0);
                        ness.setMenuYPosition(1);
                        break;
                case 2:
                case 3:
                case 4: ness.setMenuYPosition(battleMenuIndex-2);
                        battleMenuIndex = 1;
                        gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                        ness.setMenuXPosition(0);
                        break;
            }
        } else if(ness.getPlayerChoice() == 1) {
            
            /*
             * Un switch para saber en que parte del menu estoy
             * El segundo para saber en que renglo estoy del menu
             * Y el ultimo para encontrar la columna.
             * Revisar breaks.
            */
            
            switch(battleMenuIndex) {
                case 0: switch(ness.getMenuYPosition()) {
                            case 0: ness.setCategoryOfSelectedMove(1);
                                    ness.setSelectedMove(0);
                                    endOfPlayerTurn = true;
                                    break;
                            case 1: switch(ness.getMenuXPosition()) {
                                        case 0: System.out.println("Ness uses the power of PSI!");
                                                battleMenuIndex = 1;
                                                gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                                                ness.setMenuYPosition(0);
                                                break;
                                        case 1: ness.setCurrentStatus(2);
                                                gameWindow.setMessageToPrint(0, "@ Ness prepares for the enemy _  attack!");
                                                System.out.println("Ness prepares for the enemy attack!");
                                                dialogCounter++;
                                                //gameWindow.setTextDialogsToRead(1);
                                                gameWindow.setInCombatDialog(true);
                                                break;
                                        case 2: ness.setCurrentStatus(2);
                                                gameWindow.setMessageToPrint(0, "@ Ness tried to run away");
                                                System.out.println("Ness tried to run away!...");
                                                if(ThreadLocalRandom.current().nextInt(0, 100) > 20) {
                                                    gameWindow.setMessageToPrint(1, "@ And he did!");
                                                    System.out.println("And he did!");
                                                    endOfBattle = true;
                                                } else {
                                                    gameWindow.setMessageToPrint(1, "@But he failed!");
                                                    System.out.println("But he failed :(");
                                                }
                                                dialogCounter += 2;
                                                //gameWindow.setTextDialogsToRead(2);
                                                gameWindow.setInCombatDialog(true);
                                                break;
                                    }
                                    break;
                        }
                        ness.setMenuXPosition(0);
                        ness.setMenuYPosition(0);
                        break; //Hasta aqui todas las opciones del primer menu
                        
                        
                case 1: switch(ness.getMenuYPosition()) {
                            case 0: System.out.println("Ness chose an Offensive PSI attack!");
                                    battleMenuIndex = 2;
                                    break;
                            case 1: System.out.println("Ness chose a recovery PSI!");
                                    battleMenuIndex = 3;
                                    break;
                            case 2: System.out.println("Ness chose an assisting PSI!");
                                    battleMenuIndex = 4;
                                    break;
                        }
                        ness.setMenuXPosition(0);
                        ness.setMenuYPosition(0);
                        gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                        break; //Hasta aqui las opciones para el menu de PSI
                        
                        
                case 2: switch(ness.getMenuXPosition()) {
                            case 0: ness.setCurrentStatus(2);
                                    System.out.println("Ness used PSI Rockin Alpha!");
                                    gameWindow.setMessageToPrint(0,
                                            "@ Ness tried..._  PSI Rockin Alpha");
                                    dialogCounter++;
                                    gameWindow.setInCombatDialog(true);
                                    break;
                            case 1: System.out.println("Ness used PSI Rockin Beta!");
                                    break;
                            case 2: System.out.println("Ness used PSI Rockin Gamma!");
                                    //EnemyEntity enemy = (EnemyEntity) worldEntitysArray[entityInCombatWithPlayerIndex];
                                    //enemy.setWillingToFight(false);
                                    break;
                        }
                        ness.setMenuXPosition(0);
                        ness.setMenuYPosition(0);
                        battleMenuIndex = 0;
                        gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                        break; //Menu de PSI ofensivo
                        
                        
                case 3: switch(ness.getMenuXPosition()) {
                            case 0: if(ness.getMenuYPosition() == 0) {
                                        System.out.println("Ness casted Lifeup Alpha!");
                                        ness.setHealthPoints(ness.getHealthPoints() + 5);
                                    } else {
                                        System.out.println("Ness casted Healing Alpha!");
                                    }
                                    break;
                            case 1: if(ness.getMenuYPosition() == 0) {
                                        System.out.println("Ness casted Lifeup Beta!");
                                        ness.setHealthPoints(ness.getHealthPoints() + 10);
                                    } else {
                                        System.out.println("Ness casted Healing Beta!");
                                    }
                                    break;
                            case 2: if(ness.getMenuYPosition() == 0) {
                                        System.out.println("Ness casted Lifeup Gamma!");
                                        ness.setHealthPoints(ness.getHealthPoints() + 15);
                                    } else {
                                        System.out.println("Ness casted Healing Gamma!");
                                    }
                                    break;
                        }
                        battleMenuIndex = 0;
                        gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                        break; // Menu de PSI defensivo
                        
                        
                case 4: switch(ness.getMenuXPosition()) {
                            case 0: System.out.println("Ness used Brainshock Alpha!");
                                    break;
                            case 1: System.out.println("Ness used Brainshock Beta!");
                                    break;
                            case 2: System.out.println("Ness used Brainshock Gamma!");
                                    break;
                        }
                        battleMenuIndex = 0;
                        gameWindow.setLocationInMapHierarchy(battleMenuIndex);
                        break; // Menu de PSI de asistencia
                        
                        
            }
            
            
            
        }
               
    }
    
    public void enemyTurn() {
        for(int i = 0; i < enemiesInCombat.size(); i++) {
            enemiesInCombat.get(i).selectMove();
        }
    }
    
    /* Movimiento de
     * todos los elementos en pantalla
     *
     * Timertask para los movimientos de los NPC?
     */
    
    public void gameStateUpdate() {
        
        if(ness.getPlayerChoice() != 0) {
            ness.resetPlayerChoice();
        }
        
        ness.updatePlayerCharacter();
        
        if(gameStatus == 0) { // && gameStatus == 3
            
            globalXCoord = ness.getXCoord() - 120;  //Respeta asi las coordenadas de la imagen original
            globalYCoord = ness.getYCoord() - 96 + 2; //podria estar en el else(?) v
            
            if(collisionDetection()) { //Mover esto un poco, para los multiples enemigos
                if(worldEntitysArray.get(entityInContactWithPlayerIndex) instanceof EnemyEntity) {    
                    setGameStatus(2);
                    ness.setCurrentStatus(3);
                    mp3.pauseSound();
                    
                    entitysInCombat.add(ness);
                
                    EnemyEntity enemy = (EnemyEntity) worldEntitysArray.get(entityInContactWithPlayerIndex);
                    enemiesInCombat.add(enemy);
                    
                    entitysInCombat.add(enemy);
                    
                    gameWindow.paintScreenTransitionToCombat();
                    playBattleStart = true;
                }
            }
            
            //if(gameStatus == 3) {
            // MOVE TO NESS,GO,FIGHT HIM, DESTROY HIM();
            //}   
        }
        
        if(gameStatus == 2 && gameWindow.wasInScreenTransition()) {
            setGameStatus(1);
            sfxPlayer.pauseSound();
            //sfxPlayer.sleepThenPause(500);
            mp3.loopSound(enemiesInCombat.get(0).getBattleMusic()); //musica del primero
        }
        
        if(gameStatus == 1) {
            
            if(gameWindow.isScreenTransitionFinished() && ness.getCurrentStatus() == 3) {
                ness.setCurrentStatus(2);
            }
            
            if(ness.getCurrentStatus() == 1) {
                if(!endOfPlayerTurn) {
                    playerBattleMenu();
                } else {
                    ness.setCurrentStatus(2);
                    enemyTurn();
                    executeCombat();
                }
                       
                if(ness.getCurrentStatus() == 2) { //En el instante en el que el jugador elige algo
                    gameWindow.setReadingDialogs(true);
                }
            } else if(ness.getCurrentStatus() == 2) { //Si el jugador eligio algo
                gameWindow.setReadingDialogs(true);
                
                if(ness.getPlayerChoice() == 1) {
                    gameWindow.plusOneTextDialogsRead();
                } else if(ness.getPlayerChoice() == -1) {
                    gameWindow.setTextDialogsRead(gameWindow.getTextDialogsToRead()); //Debe cambiar
                }
                
                if(gameWindow.isFinishedReadingDialogs()) {
                    
                    if(endOfBattle) {
                        setGameStatus(3);
                        gameWindow.paintScreenTransition();
                        endOfBattle = false;
                    } else {
                        ness.setCurrentStatus(1);
                        endOfPlayerTurn = false;
                    }
                    dialogCounter = 0;
                }
            }
        }
        
        if(gameStatus == 3 && gameWindow.wasInScreenTransition()){
            setGameStatus(0);
            ness.setCurrentStatus(0);
            mp3.playSound("Onett_theme");
        }
        
        /*
            Derrotar a un enemigo en combate
        */
        
        if(gameStatus == 1 && !enemiesAbleToFight(enemiesInCombat) && 
                !gameWindow.isInCombatDialog()) { 
            
            worldEntitysArray.removeAll(enemiesInCombat);
            gameWindow.removeOverworldEntityFromScreen(enemiesInCombat);
            enemiesInCombat.clear();
            entitysInCombat.clear();
            
            setGameStatus(3);
            ness.setCurrentStatus(3);
            gameWindow.paintScreenTransition();
        }
        
    }
    
    /*Se pasan los elementos que han de imprimirse */
    
    public void updateGraphics() { //No permitirle al usuario salir de los bordes de la imagen
        
        if(gameStatus != oldGameStatus) { //Si hubo algun cambio //Una vez
            switch(gameStatus) {
                case 0: BackgroundMap.setMap(0);
                        gameWindow.setScreenStatus(0);
                        break;
                case 1: gameWindow.setMapSection(enemiesInCombat.get(0).getBattleBackground());
                        gameWindow.setEnemyBattleSprite(enemiesInCombat.get(0).getBattleSprite()); //Esto puede que cambie
                        gameWindow.setEnemyBattleSpriteWidth(enemiesInCombat.get(0).getBattleSpriteWidth());
                        gameWindow.setEnemyBattleSpriteHeigth(enemiesInCombat.get(0).getBattleSpriteHeigth());
                        gameWindow.setScreenStatus(gameStatus);
                        break;                        
                case 2: BackgroundMap.setMap(1); //Transicion a pelea
                        break;
            }
            setOldGameStatus(gameStatus);
        }
        
        if(gameStatus != 1 && gameStatus != 3) { //Si no es pelea o transicion al mundo
            gameWindow.setMapSection(BackgroundMap.getMapSection(globalXCoord, globalYCoord));
            
            gameWindow.setPlayerSprite(ness.getSprite());

            gameWindow.setGlobalXCoord((globalXCoord)*2 ) ; //240 es constante por el centro de la ventana, 192 too
            gameWindow.setGlobalYCoord((globalYCoord - 2)*2 ); //El "*2" es constante, debido a la escala
            
            for(i = 0; i < worldEntitysArray.size(); i++) {
                
                if(isEntityVisible( worldEntitysArray.get(i).getXCoord(), 
                        worldEntitysArray.get(i).getYCoord()) ) {
                    
                    if(!gameWindow.isEntityAlreadyInScreen(worldEntitysArray.get(i))) {
                        gameWindow.setOverworldEntityOnScreen(worldEntitysArray.get(i));
                    }
                } else {
                    gameWindow.removeOverworldEntityFromScreen(worldEntitysArray.get(i));
                }
            }
        } else {
            
            if(healthUpdateDialogIndex == gameWindow.getTextDialogsRead() ||
                   gameWindow.isStartingCombat()) {
                gameWindow.setPlayerHealth(ness.getHealthPoints());
                gameWindow.setPlayerPowerPoints(ness.getPowerPoints());
            }
            
            if(ness.getCurrentStatus() == 1) {
                gameWindow.setMenuPointerYPosition(ness.getMenuYPosition());
                gameWindow.setMenuPointerXPosition(ness.getMenuXPosition());
            }
        }
        
    }
    
    public void renderGraphics() {
        gameWindow.repaint();
        
    }
    
    public boolean collisionDetection() { //returns true if player collided with an object
        
        for(int i = 0; i < worldEntitysArray.size(); i++) {
            
            if(worldEntitysArray.get(i).isPlayerInEntityHitbox(ness.getXCoord(), ness.getYCoord())) {
                ness.revertLastMove();
                ness.setCurrentSpriteRow(0);
                
                entityInContactWithPlayerIndex = i;
                return true;   
            }
        }
        
        return false;
    }

    @Override
    public void run() {
        initialize();
        
        timeBeforeLoop = System.currentTimeMillis();
        
        while(isGameRunning) {
            gameStateUpdate();
            updateGraphics();
            renderGraphics();

            timeDifference = System.currentTimeMillis() - timeBeforeLoop;
            sleepTime = MPF - timeDifference;

            if(sleepTime <= 0) {
                sleepTime = 5; //sleep a little
            }

            try {
                graphicThread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeBound.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

            timeBeforeLoop = System.currentTimeMillis();
        }

        stopThread();
    }
    
    public boolean isEntityVisible(int entityXCoord, int entityYCoord) {
        
        boolean entityVisible = false;
        
        if((entityXCoord + 16 >= globalXCoord && entityXCoord <= globalXCoord + 256) &&
           (entityYCoord + 24 >= globalYCoord && entityYCoord <= globalYCoord + 240) ) {
            entityVisible = true;
        }
        
        return entityVisible;
    }
    
    public static int getGameStatus() {
        return gameStatus;
    }
    
    public static void setGameStatus(int status) {
        oldGameStatus = gameStatus;
        gameStatus = status;
    }
    
    public static void setOldGameStatus(int status) {
        oldGameStatus = status;
    }
    
    private void defineTurnOrder() {
        
        BattleAbleOverworldEntity bubbleSortAux;
        
        for(int i = 0; i < entitysInCombat.size(); i++) {
            for(int j = 0; j < entitysInCombat.size(); j++) {
                if(entitysInCombat.get(j).getSpeed() < entitysInCombat.get(i).getSpeed()) {
                    bubbleSortAux = entitysInCombat.get(i);
                    entitysInCombat.set(i, entitysInCombat.get(j));
                    entitysInCombat.set(j, bubbleSortAux);
                }
            }
        }
    }
    
    private void executeCombat() {
        defineTurnOrder();
        
        for(int i = 0; i < entitysInCombat.size(); i++) {
            if(entitysInCombat.get(i) instanceof EnemyEntity) {
                
                EnemyEntity enemy = (EnemyEntity) entitysInCombat.get(i);
                switch(entitysInCombat.get(i).getCategoryOfSelectedMove()) {
                    case 0: gameWindow.setMessageToPrint(dialogCounter,
                                    "@ The " + enemy.getName() + " " + enemy.getSelectedMoveDescription());
                            dialogCounter++;
                            break;
                            
                    case 1: gameWindow.setMessageToPrint(dialogCounter,
                                    "@ The " + enemy.getName() + " " + enemy.getSelectedMoveDescription());
                            dialogCounter++;
                            if(!attackMissed()) {
                                
                                if(attackCritted(enemy.getGuts())) {
                                    enemyAttackDamage = 4*enemy.getOffense();
                                    enemyDamageDealt = enemyAttackDamage - ness.getDefense() +
                                            ThreadLocalRandom.current().nextInt(-2,3);
                                    
                                    gameWindow.setMessageToPrint(dialogCounter,
                                        "@ SMAAAAAAASH!");
                                    dialogCounter++;
                                    
                                    ness.reduceHealthPoints(enemyDamageDealt);
                                    
                                    gameWindow.setMessageToPrint(dialogCounter,
                                        "@ " + enemyDamageDealt + " HP of damage_  to " + ness.getName() + "!.");
                                    healthUpdateDialogIndex = dialogCounter;
                                    dialogCounter++;
                                } else {
                                    
                                    if(!attackDodged(ness.getSpeed(), enemy.getSpeed())) {
                                        
                                        enemyAttackDamage = enemy.getOffense();
                                        
                                        enemyDamageDealt = enemyAttackDamage - ness.getDefense() +
                                                ThreadLocalRandom.current().nextInt(-2,3);

                                        ness.reduceHealthPoints(enemyDamageDealt);

                                        gameWindow.setMessageToPrint(dialogCounter,
                                            "@ " + enemyDamageDealt + " HP of damage_  to " + ness.getName() + "!.");
                                        healthUpdateDialogIndex = dialogCounter;
                                        dialogCounter++;
                                    } else {
                                        gameWindow.setMessageToPrint(dialogCounter,
                                            "@ " + ness.getName() + " dodged quickly!");
                                        dialogCounter++;
                                    }
                                }
                            } else {
                                gameWindow.setMessageToPrint(dialogCounter,
                                    "@ Just missed!");
                                dialogCounter++;
                            }
                            break;
                }
            } else if(entitysInCombat.get(i) instanceof PlayerCharacter) {
                
                int enemyIndex = entitysInCombat.indexOf(enemiesInCombat.get(0));
                switch(ness.getCategoryOfSelectedMove()) {
                    case 1: switch(ness.getSelectedMove()) {
                                case 0: System.out.println("Ness bashes against the enemy!");
                                        gameWindow.setMessageToPrint(dialogCounter,
                                                "@ Ness bashes against the enemy");
                                        dialogCounter++;
                                       
                                        if(!attackMissed()) {
                                
                                            if(attackCritted(ness.getGuts())) {
                                                playerAttackDamage = 4*ness.getOffense();
                                                playerDamageDealt = playerAttackDamage - 
                                                        entitysInCombat.get(enemyIndex).getDefense() +
                                                        ThreadLocalRandom.current().nextInt(-2,3);

                                                gameWindow.setMessageToPrint(dialogCounter,
                                                    "@ SMAAAAAAASH!");
                                                dialogCounter++;

                                                entitysInCombat.get(enemyIndex).reduceHealthPoints(playerDamageDealt);

                                                gameWindow.setMessageToPrint(dialogCounter,
                                                    "@ " + playerDamageDealt + " HP of damage_  to " +
                                                            entitysInCombat.get(enemyIndex).getName() + "!.");
                                                dialogCounter++;
                                            } else {

                                                if(!attackDodged(entitysInCombat.get(enemyIndex).getSpeed(), ness.getSpeed())) {

                                                    playerAttackDamage = ness.getOffense();

                                                    playerDamageDealt = playerAttackDamage -
                                                            entitysInCombat.get(enemyIndex).getDefense() +
                                                            ThreadLocalRandom.current().nextInt(-2,3);

                                                    entitysInCombat.get(enemyIndex).reduceHealthPoints(playerDamageDealt);

                                                    gameWindow.setMessageToPrint(dialogCounter,
                                                        "@ " + playerDamageDealt + " HP of damage_  to " +
                                                                entitysInCombat.get(enemyIndex).getName() + "!.");
                                                    dialogCounter++;
                                                } else {
                                                    gameWindow.setMessageToPrint(dialogCounter,
                                                        "@ " + entitysInCombat.get(enemyIndex).getName() +
                                                                " dodged quickly!");
                                                    dialogCounter++;
                                                }
                                            }
                                        } else {
                                            gameWindow.setMessageToPrint(dialogCounter,
                                                "@ Just missed!");
                                            dialogCounter++;
                                        }
                                        
                                        if(!entitysInCombat.get(enemyIndex).isAbleToFight()) {
                                            gameWindow.setMessageToPrint(dialogCounter, "@ The enemy surrenders, you won!");
                                            dialogCounter++;
                                        }
                                        break;      
                            }
                            break; 
                }
            }
        }
        
        endOfPlayerTurn = false;
        
        if(dialogCounter != 0) {
            gameWindow.setInCombatDialog(true);
            gameWindow.setTextDialogsToRead(dialogCounter);
        }
    }
    
    public boolean attackMissed() {
        if(ThreadLocalRandom.current().nextInt(0, 16) == 15) {
            return true;
        }
        
        return false;
    }
    
    public boolean attackCritted(int gutsValue) {
        double critChance = (double) gutsValue/500;
        double defaultCritChance = 0.05;
        
        if(critChance > defaultCritChance) {
            if(critChance > (ThreadLocalRandom.current().nextDouble(0, 1) ) ) {
                return true;
            }
        } else {
            if(defaultCritChance > (ThreadLocalRandom.current().nextDouble(0, 1) ) ) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean attackDodged(int targetSpeed, int attackerSpeed) {
        double dodgeChance = (double) (2*targetSpeed - attackerSpeed)/500;
        
        if(dodgeChance > (ThreadLocalRandom.current().nextDouble(0, 1) ) ) {
            return true;
        }
        
        return false;
    }
    
    public boolean enemiesAbleToFight(Vector<EnemyEntity> enemyVector) {
        
        for(EnemyEntity enemy : enemyVector) {
            if(enemy.isAbleToFight()) {
                return true;
            }
        }
        
        return false;
    }
    
}