
package timebound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameDisplayPane extends JPanel {
    
    //Algo que pinte, y algo que ponga el JPanel?
    
    private Vector<OverworldEntity> entityArray; //Cambiar a multiples vectores, utilizar solo cosas importantes (sprite, x, y)
    private BufferedImage mapSection;
    
    private BufferedImage[] screenTransition;
    private BufferedImage[] screenTransitionToCombat;
    private boolean inScreenTransition;
    private boolean wasInScreenTransition;
    private int screenTransitionIndex;
    private int screenStatus;
    
    private int globalXCoord;
    private int globalYCoord;
    private BufferedImage playerSprite;
    
    private BufferedImage playerBattleCardSprite;
    private BufferedImage[] playerBattleNumberDataDisplay;
    private int[] playerHealthArray;
    private int[] playerPowerPointsArray;
    
    private BufferedImage enemyBattleSprite;
    private int enemyBattleSpriteWidth;
    private int enemyBattleSpriteHeigth;
    private StringBuffer enemyName;
    
    private BufferedImage displayMessageBoard;
    private int coordManager;
    private StringBuffer message;
    private int sumaDeCharWidth;
    private int yTextPosition;
    
    private BufferedImage[] battleMenu;
    private boolean[] battleMenuLocation;
    private static final StringBuffer[] BATTLE_MENU_TEXT = new StringBuffer[]{
    new StringBuffer("Bash       Goods_PSI          Defend    Run Away"),
    new StringBuffer("Offensive_Recovery_Assist"),
    new StringBuffer("PSI Rockin         I     II     III"),
    new StringBuffer("Lifeup               I     II     III_Healing              I     II     III"),
    new StringBuffer("Brainshock        I     II     III")
    };
    /*
    private static final int[] BATTLE_MENU_X_COORD = new int[] {30};
    private static final int[] BATTLE_MENU_Y_COORD = new int[] {20};
    private static final int[] BATTLE_MENU_WIDTH = new int[] {320};
    private static final int[] BATTLE_MENU_HEIGTH = new int[] {};*/
    
    private BufferedImage menuPointer;
    private int pointerOnMenu; // menu 1, menu 2, it depends.
    private int menuPointerYPosition;
    private int menuPointerXPosition;
    
    
    
    private int durationCounter;
    private boolean screenTransitionFinished;
    private boolean inScreenTransitionToCombat;
    private boolean startingCombat;
    private int textDialogsRead;
    private int textDialogsToRead;
    private boolean finishedReadingDialogs;
    private boolean readingDialogs;
    private String[] messageToPrint;
    private boolean inCombatDialog;
    
    
    public GameDisplayPane(int width, int height) {
        super.setSize(new Dimension(width, height));
        super.setMaximumSize(new Dimension(width, height));
        super.setMinimumSize(new Dimension(width, height));

        this.screenTransition = new BufferedImage[10];
        this.screenTransitionToCombat = new BufferedImage[20];
        this.playerBattleNumberDataDisplay = new BufferedImage[10];
        this.playerHealthArray = new int[3];
        this.playerPowerPointsArray = new int[3];
        this.battleMenu = new BufferedImage[3];
        this.battleMenuLocation = new boolean[] {true, false, false};
        this.messageToPrint = new String[10];
        
        try{
            playerBattleCardSprite = ImageIO.read(new File("./src/resources/spritesheets/battle_card.png"));
            displayMessageBoard = ImageIO.read(new File("./src/resources/spritesheets/letter_sprites/text_board.png"));
            menuPointer = ImageIO.read(new File("./src/resources/spritesheets/letter_sprites/menu_pointer.png"));
        }catch(NullPointerException | IOException e) {
            System.out.println("No se encontro la carta de batalla");
            e.printStackTrace();
        }
        
        for(int i = 0; i < 20; i++) {
            if(i <= 2) {
                try {
                    battleMenu[i] = ImageIO.read(new File("./src/resources/spritesheets/letter_sprites/battle_menu_"+ (i+1) +".png"));
                } catch (IOException ex) {
                    Logger.getLogger(GameDisplayPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(i < 10) {    
                try{
                    screenTransition[i] = ImageIO.read(new File("./src/resources/maps/screen_transition/" + i + ".png"));
                    playerBattleNumberDataDisplay[i] = ImageIO.read(new File("./src/resources/spritesheets/battle_numbers/battle_card_numbers/" + i + ".png"));
                }catch(NullPointerException | IOException e) {
                    System.out.println("No se encontraron las transiciones de pantalla");
                    e.printStackTrace();
                }
            }
            
            try{
                screenTransitionToCombat[i] = ImageIO.read(new File("./src/resources/maps/battle_swirl/" + i + ".png"));
            }catch(NullPointerException | IOException e) {
                System.out.println("No se encontraron las transiciones de pantalla");
                e.printStackTrace();
            }
        }
        
        entityArray = new Vector();
    }
    
    public boolean isScreenTransitionFinished() {
        return screenTransitionFinished;
    }
    
    public boolean isStartingCombat() {
        return startingCombat;
    }
    
    public void setStartingCombat(boolean startingCombat) {
        this.startingCombat = startingCombat;
    }
    
    public int getTextDialogsRead() {
        return textDialogsRead;
    }
    
    public void setTextDialogsRead(int textDialogsRead) {
        this.textDialogsRead = textDialogsRead;
        if(textDialogsRead == textDialogsToRead) {
            finishedReadingDialogs = true;
        } else {
            finishedReadingDialogs = false;
        }
    }
    
    public int getTextDialogsToRead() {
        return textDialogsToRead;
    }
    
    public void setTextDialogsToRead(int textDialogsToRead) {
        this.textDialogsToRead = textDialogsToRead;
    }
    
    public boolean isFinishedReadingDialogs() {
        return finishedReadingDialogs;
    }
    
    public void setFinishedReadingDialogs(boolean status) {
        finishedReadingDialogs = status;
    }
    
    public void setReadingDialogs(boolean readingDialogs) {
        this.readingDialogs = readingDialogs;
    }
    
    public boolean isReadingDialogs() {
        return readingDialogs;
    }
    
    public void plusOneTextDialogsRead() {
        textDialogsRead++;
        if(textDialogsRead == textDialogsToRead) {
            finishedReadingDialogs = true;
        } else {
            finishedReadingDialogs = false;
        }
    }
    
    public void setMessageToPrint(int order, String message ) {
        messageToPrint[order] = message;
    }
    
    public boolean isInCombatDialog() {
        return inCombatDialog;
    }
    
    public void setInCombatDialog(boolean inCombatDialog) {
        this.inCombatDialog = inCombatDialog;
        finishedReadingDialogs = false;
    }
    
    
    
    
    
    
    public void setOverworldEntityOnScreen(OverworldEntity entity) {
        entityArray.add(entity);
    }
    
    public void removeOverworldEntityFromScreen(OverworldEntity entity) {
        entityArray.remove(entity);
    }
    
    public void removeOverworldEntityFromScreen(Vector entityVector) {
        entityArray.removeAll(entityVector);
    }
    
    public boolean isEntityAlreadyInScreen(OverworldEntity entity) {
        return entityArray.contains(entity);
    }
    
    public void setMapSection(BufferedImage mapSection) {
        this.mapSection = mapSection;
    }
    
    public void setPlayerSprite(BufferedImage playerSprite) {
        this.playerSprite = playerSprite;
    }
    
    public void setEnemyBattleSprite(BufferedImage enemySprite) {
        this.enemyBattleSprite = enemySprite;
    }
    
    public void setEnemyBattleSpriteWidth(int spriteWidth) {
        this.enemyBattleSpriteWidth = spriteWidth;
    }
    
    public void setEnemyBattleSpriteHeigth(int spriteHeigth) {
        this.enemyBattleSpriteHeigth = spriteHeigth;
    }
    
    public void setPlayerHealth(int playerHealth) {
        
        if(playerHealth < 100) {
            if(playerHealth < 10) {
                playerHealthArray[2] = 0;
                playerHealthArray[1] = 0;
                playerHealthArray[0] = playerHealth;
            } else {
                playerHealthArray[2] = 0;
                playerHealthArray[1] = playerHealth / 10;
                playerHealthArray[0] = playerHealth % 10;
            }
        } else {
            playerHealthArray[2] = playerHealth/ 100;
            playerHealthArray[1] = (playerHealth % 100) /10;
            playerHealthArray[0] = (playerHealth % 100) % 10;
        }
    }
    
    public void setPlayerPowerPoints(int playerPowerPoints) {
        
        if(playerPowerPoints < 100) {
            if(playerPowerPoints < 10) {
                playerPowerPointsArray[2] = 0;
                playerPowerPointsArray[1] = 0;
                playerPowerPointsArray[0] = playerPowerPoints;
            } else {
                playerPowerPointsArray[2] = 0;
                playerPowerPointsArray[1] = playerPowerPoints / 10;
                playerPowerPointsArray[0] = playerPowerPoints % 10;
            }
        } else {
            playerPowerPointsArray[2] = playerPowerPoints/ 100;
            playerPowerPointsArray[1] = (playerPowerPoints % 100) /10;
            playerPowerPointsArray[0] = (playerPowerPoints % 100) % 10;
        }
    }
    
    public void setGlobalXCoord(int globalXCoord) {
        this.globalXCoord = globalXCoord;
    }
    
    public void setGlobalYCoord(int globalYCoord) {
        this.globalYCoord = globalYCoord;
    }
    
    public void setMenuPointerXPosition(int pointerXPosition) {
        menuPointerXPosition = pointerXPosition;
    }
    
    public void setMenuPointerYPosition(int pointerYPosition) {
        menuPointerYPosition = pointerYPosition;
    }
    
    public void paintScreenTransition() {
        inScreenTransition = true;
        screenTransitionFinished = false;
        screenTransitionIndex = 0;
    }

    public void paintScreenTransitionToCombat() {
        inScreenTransitionToCombat = true;
        screenTransitionFinished = false;
        startingCombat = true;
        readingDialogs = true;
        finishedReadingDialogs = false;
        screenTransitionIndex = 0;
    }
    
    public boolean isInScreenTransition() {
        return inScreenTransition;
    }
    
    public boolean isInScreenTransitionToCombat() {
        return inScreenTransitionToCombat;
    }
    
    public boolean wasInScreenTransition() {
        return wasInScreenTransition;
    }
    
    public void displayMessageOnScreen(Graphics g, String messageToPrint) {
        message = new StringBuffer(messageToPrint);
        
        g.drawImage(displayMessageBoard, 76, 10, 360, 96, null);
        
        for(BufferedImage image : TextConverter.getLetterArray(message)) {

            switch(message.charAt(coordManager)) {
                case '_':   yTextPosition++;
                            sumaDeCharWidth = 0;
                            break;
                case ' ':   g.drawImage(image, 94 + sumaDeCharWidth, 28 + 32*yTextPosition,
                                        (TextConverter.CHAR_WIDTH[90])*2, 24, null);
                            sumaDeCharWidth += TextConverter.CHAR_WIDTH[90]*2;
                            break;
                default:    g.drawImage(image, 94 + sumaDeCharWidth, 28 + 32*yTextPosition,
                                            (TextConverter.CHAR_WIDTH[message.charAt(coordManager) - 33] - 1)*2,
                                            24, null);
                            sumaDeCharWidth += (TextConverter.CHAR_WIDTH[message.charAt(coordManager) - 33] )*2;
                            break;
            }
            coordManager++;
            
            if(sumaDeCharWidth > 320) {
                yTextPosition++;
                sumaDeCharWidth = 0;  
            }
        }
        coordManager = 0;
        sumaDeCharWidth = 0;
        yTextPosition = 0;
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
     
        
        
        //Dibujo general
        
        g.drawImage(mapSection, 0, 0, 512, 480, null);
        
        if(screenStatus == 0) {
            g.drawImage(playerSprite, 240, 192, 32, 48, null);

            if(!entityArray.isEmpty()) { //imprime las entitdades en el mapa 

                for(OverworldEntity entity : entityArray) {
                    g.drawImage(entity.getSprite(), entity.getXCoord()*2 - globalXCoord,
                                entity.getYCoord()*2 - globalYCoord, 32, 48, null); //El "*2" es constante por escala
                } 
            }
        } else if(screenStatus == 1) {  //Si esta peleando
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 512, 120);
            g.fillRect(0, 320, 512, 140);
            
            g.drawImage(playerBattleCardSprite, 200, 300, 112, 128, null);
            
            for(int i = 0; i < 3; i++) { //Vida y PP del jugador
                g.drawImage(playerBattleNumberDataDisplay[playerHealthArray[i]], 282 - i*16, 350, 14, 24, null);
                
                g.drawImage(playerBattleNumberDataDisplay[playerPowerPointsArray[i]], 282 - i*16, 381, 14, 24, null);
            }
            
            g.drawImage(enemyBattleSprite, 256 - enemyBattleSpriteWidth ,
                    280 - enemyBattleSpriteHeigth*2, enemyBattleSpriteWidth*2,
                    enemyBattleSpriteHeigth*2, null);
            
            if(readingDialogs) {
                
                if(startingCombat) {
                    textDialogsToRead = 1;

                    if(!finishedReadingDialogs) {
                        
                        displayMessageOnScreen(g, "@ You engage the Spiteful Crow!");

                    } else {
                        finishedReadingDialogs = true;
                        readingDialogs = false;
                        startingCombat = false;
                        textDialogsRead = 0;
                    }
                } else if(inCombatDialog) {

                    if(!finishedReadingDialogs) {
                        displayMessageOnScreen(g, messageToPrint[textDialogsRead]);
                    } else {
                        finishedReadingDialogs = true;
                        readingDialogs = false;
                        inCombatDialog = false;
                        textDialogsRead = 0;
                    }
                }
            } else if(!inScreenTransition){
                
                drawBattleMenus(g);
                
                switch(pointerOnMenu) {
                    case 0: g.drawImage(menuPointer, 50 + 86*menuPointerXPosition, 42 + 32*menuPointerYPosition,
                            8, 16, null);
                            break;

                    case 1: g.drawImage(menuPointer, 100 + 86*menuPointerXPosition, 48 + 32*menuPointerYPosition,
                            8, 16, null);
                            break;
                    case 2: 
                    case 3: 
                    case 4: g.drawImage(menuPointer, 375 + 35*menuPointerXPosition, 48 + 32*menuPointerYPosition,
                            8, 16, null);
                            break;
                }
            }
        }
        
        
        
        
        
        
        //Transiciones
        
        if(inScreenTransitionToCombat && !inScreenTransition) {
                
            g.drawImage(screenTransitionToCombat[screenTransitionIndex], 0, 0, 512, 480, null);

            if(screenTransitionIndex < 20) {
                durationCounter++;
            }

            if(durationCounter == 3) {
                if(screenTransitionIndex != 19) {
                   screenTransitionIndex++;
                    durationCounter = 0; 
                } else {
                    inScreenTransition = true;
                    screenTransitionIndex = 0;
                    durationCounter = 0;
                }  
            }
        }
        
        if(inScreenTransition || wasInScreenTransition) {
            
            if(inScreenTransition && inScreenTransitionToCombat) {
                g.drawImage(screenTransitionToCombat[19], 0, 0, 512, 480, null);
            }
            
            g.drawImage(screenTransition[screenTransitionIndex], 0, 0, 512, 480, null);
            
            durationCounter++;
            
            if(durationCounter == 4) {
                if(inScreenTransition) {
                    if(screenTransitionIndex != 9) {
                        screenTransitionIndex++;
                    } else {
                        inScreenTransition = false;
                        if(inScreenTransitionToCombat) {
                            startingCombat = true;
                            inScreenTransitionToCombat = false;
                        }
                        wasInScreenTransition = true;
                    }
                } else if(wasInScreenTransition) {
                    if(screenTransitionIndex != 0) {
                        screenTransitionIndex--;
                    } else {
                        wasInScreenTransition = false;
                        screenTransitionFinished = true;
                    }
                }
                durationCounter = 0;
            }
        }
        
        g.dispose();
    }

    public void setScreenStatus(int gameStatus) {
        screenStatus = gameStatus;
    }
    
    public void setLocationInMapHierarchy(int position) {
        
        for(int j = 0; j < battleMenuLocation.length; j++) {
            battleMenuLocation[j] = false;
        }
        
        battleMenuLocation[0] = true;
        pointerOnMenu = position;
        
        switch(position) {
            case 1: 
            case 2: 
            case 3: 
            case 4: battleMenuLocation[1] = true;
                    break;
        }
        
    }
    
    public void drawBattleMenus(Graphics g) {
        if(battleMenuLocation[0]) {    
            g.drawImage(battleMenu[0], 30, 20, 320, 96, this);

            for(BufferedImage image : TextConverter.getLetterArray(BATTLE_MENU_TEXT[0])) {

                switch(BATTLE_MENU_TEXT[0].charAt(coordManager)) {
                    case '_':   yTextPosition++;
                                sumaDeCharWidth = 0;
                                break;
                    case ' ':   g.drawImage(image, 64 + sumaDeCharWidth, 40 + 32*yTextPosition,
                                            (TextConverter.CHAR_WIDTH[90])*2, 24, null);
                                sumaDeCharWidth += TextConverter.CHAR_WIDTH[90]*2;
                                break;
                    default:    g.drawImage(image, 64 + sumaDeCharWidth, 40 + 32*yTextPosition,
                                                (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[0].charAt(coordManager) - 33] - 1)*2,
                                                24, null);
                                sumaDeCharWidth += (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[0].charAt(coordManager) - 33] )*2;
                                break;
                }
                coordManager++;
            }
            coordManager = 0;
            sumaDeCharWidth = 0;
            yTextPosition = 0;
        }

        if(battleMenuLocation[1]) {
            g.drawImage(battleMenu[1], 80, 20, 140, 140, this);

            for(BufferedImage image : TextConverter.getLetterArray(BATTLE_MENU_TEXT[1])) {

                switch(BATTLE_MENU_TEXT[1].charAt(coordManager)) {
                    case '_':   yTextPosition++;
                                sumaDeCharWidth = 0;
                                break;
                    case ' ':   g.drawImage(image, 115 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                            (TextConverter.CHAR_WIDTH[90])*2, 24, null);
                                sumaDeCharWidth += TextConverter.CHAR_WIDTH[90]*2;
                                break;
                    default:    g.drawImage(image, 115 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                                (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[1].charAt(coordManager) - 33] - 1)*2,
                                                24, null);
                                sumaDeCharWidth += (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[1].charAt(coordManager) - 33] )*2;
                                break;
                }
                coordManager++;
            }
            coordManager = 0;
            sumaDeCharWidth = 0;
            yTextPosition = 0;
            
            g.drawImage(battleMenu[2], 222, 20, 280, 140, this);

            
            if(pointerOnMenu == 1) {
                for(BufferedImage image : TextConverter.getLetterArray(BATTLE_MENU_TEXT[menuPointerYPosition+2])) {

                    switch(BATTLE_MENU_TEXT[menuPointerYPosition+2].charAt(coordManager)) {
                        case '_':   yTextPosition++;
                                    sumaDeCharWidth = 0;
                                    break;
                        case ' ':   g.drawImage(image, 245 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                                (TextConverter.CHAR_WIDTH[90])*2, 24, null);
                                    sumaDeCharWidth += TextConverter.CHAR_WIDTH[90]*2;
                                    break;
                        default:    g.drawImage(image, 245 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                                    (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[menuPointerYPosition+2].charAt(coordManager) - 33] - 1)*2,
                                                    24, null);
                                    sumaDeCharWidth += (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[menuPointerYPosition+2].charAt(coordManager) - 33] )*2;
                                    break;
                    }
                    coordManager++;
                }
            } else {
                for(BufferedImage image : TextConverter.getLetterArray(BATTLE_MENU_TEXT[pointerOnMenu])) {

                    switch(BATTLE_MENU_TEXT[pointerOnMenu].charAt(coordManager)) {
                        case '_':   yTextPosition++;
                                    sumaDeCharWidth = 0;
                                    break;
                        case ' ':   g.drawImage(image, 245 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                                (TextConverter.CHAR_WIDTH[90])*2, 24, null);
                                    sumaDeCharWidth += TextConverter.CHAR_WIDTH[90]*2;
                                    break;
                        default:    g.drawImage(image, 245 + sumaDeCharWidth, 47 + 32*yTextPosition,
                                                    (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[pointerOnMenu].charAt(coordManager) - 33] - 1)*2,
                                                    24, null);
                                    sumaDeCharWidth += (TextConverter.CHAR_WIDTH[BATTLE_MENU_TEXT[pointerOnMenu].charAt(coordManager) - 33] )*2;
                                    break;
                    }
                    coordManager++;
                }   
            }
            
            coordManager = 0;
            sumaDeCharWidth = 0;
            yTextPosition = 0;
               
        }

        switch(pointerOnMenu) {
            case 0: g.drawImage(menuPointer, 50 + 86*menuPointerXPosition, 42 + 32*menuPointerYPosition,
                    8, 16, null);
                    break;

            case 1: g.drawImage(menuPointer, 100 + 86*menuPointerXPosition, 48 + 32*menuPointerYPosition,
                    8, 16, null);
                    break;
            case 2: 
            case 3: 
            case 4: g.drawImage(menuPointer, 375 + 35*menuPointerXPosition, 48 + 32*menuPointerYPosition,
                    8, 16, null);
                    break;
        }
    }
}


