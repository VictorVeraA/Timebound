
package timebound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Victor
 */
public abstract class EnemyEntity extends BattleAbleOverworldEntity implements HostileNPCInterface{

    private BufferedImage battleBackground;
    private String battleMusic;
    private BufferedImage battleSprite;
    private int battleSpriteWidth;
    private int battleSpriteHeigth;
    
    private String[] selectedMoveDescription;  //Lo inicializa cada uno
    
    public EnemyEntity(int xCoord, int yCoord, String name, String spriteSheetName,
            int rowsOfSprites, int columnsOfSprites, int hitboxWidth, int hitboxHeigth,
            int hitboxXCoord, int hitboxYCoord, int healthPoints, int powerPoints, int offense,
            int defense, int speed, int guts, String battleBackgroundName,
            String battleMusicName, String battleSpriteName, int battleSpriteWidth,
            int battleSpriteHeigth) {
        
        super(xCoord, yCoord, name, spriteSheetName, rowsOfSprites, columnsOfSprites,
                hitboxWidth, hitboxHeigth, hitboxXCoord, hitboxYCoord, healthPoints,
                powerPoints, offense, defense, speed, guts);
        
        this.battleMusic = battleMusicName;
        this.battleSpriteHeigth = battleSpriteHeigth;
        this.battleSpriteWidth = battleSpriteWidth;
        
        if(battleBackgroundName != null) {
            try {
                this.battleBackground = ImageIO.read(new File("./src/resources/maps/"+ battleBackgroundName +".png"));
                this.battleSprite = ImageIO.read(new File("./src/resources/spritesheets/battle_sprites/" + battleSpriteName +".png"));
            } catch (IOException ex) {
                Logger.getLogger(EnemyEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public String getBattleMusic() {
        return battleMusic;
    }
    
    public BufferedImage getBattleBackground() {
        return battleBackground;
    }
    
    public BufferedImage getBattleSprite() {
        return battleSprite;
    }
    
    public int getBattleSpriteWidth() {
        return battleSpriteWidth;
    }
    
    public int getBattleSpriteHeigth() {
        return battleSpriteHeigth;
    }
    
    public void initializeSelectedMoveDescriptionArray(int size) {
        if(size != 0) {
            selectedMoveDescription = new String[size];
        }
    }
    
    public void setSelectedMoveDescription(int index, String description) {
        selectedMoveDescription[index] = description;
    }
    
    public String getSelectedMoveDescription(int index) {
        return selectedMoveDescription[index];
    }
    
    public String getSelectedMoveDescription() {
        return selectedMoveDescription[getSelectedMove()];
    }
    
    public abstract void selectMove();
    
    public abstract int getDamageFromSelectedMove();
    
}
