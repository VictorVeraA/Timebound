
package timebound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class OverworldEntity { //Algo por encima de OE para puertas y cosas sin sprite
   
    private int xCoord; 
    private int yCoord;
    private int hitboxWidth;
    private int hitboxHeigth;
    private int hitboxXCoord; // En relacion al tamanio del 
    private int hitboxYCoord; // sprite
    private String name;
    
    private BufferedImage spriteSheet;
    private int rowsOfSprites;
    private int columnsOfSprites;
    private BufferedImage[][] spriteMatrix;
    private int currentSpriteRow;
    private int currentSpriteColumn;
    
    private boolean inCollisionWithPlayer;
    private boolean nextToPlayerCharacter;
    
    public OverworldEntity(int xCoord, int yCoord, String name,
            String spriteSheetName, int rowsOfSprites, int columnsOfSprites, 
            int hitboxWidth, int hitboxHeigth, int hitboxXCoord, int hitboxYCoord) {
        
        this.xCoord = xCoord; //Posicion en el map
        this.yCoord = yCoord;
        
        this.hitboxWidth = hitboxWidth;     //tama�o y posicion de hitboxes
        this.hitboxHeigth = hitboxHeigth;   //Tiene que haber un metodo abstracto para implementar esto
        this.hitboxXCoord = hitboxXCoord;
        this.hitboxYCoord = hitboxYCoord;
        
        this.name = name;
        
        this.rowsOfSprites = rowsOfSprites;         //Indicativo del sistema para recuperar
        this.columnsOfSprites = columnsOfSprites;   //los sprites dentro de un spritesheet correctamente;
        
        this.currentSpriteRow = 0;                  //Indices utilizados en la matriz de sprites
        this.currentSpriteColumn = 0;               //para la facil invocacion del sprite actual de la entidad
        
        try{
            this.spriteSheet = ImageIO.read(new File("./src/resources/spritesheets/" + spriteSheetName + ".png"));
            this.spriteMatrix = new BufferedImage[rowsOfSprites][columnsOfSprites];
            getSpritesFromSpriteSheet();
        }catch(IOException e) {
            System.out.println("No se encontro el spritesheet de " + this.name);
        }
    }
    
    public int getXCoord() {
        return xCoord;
    }
    
    public int getYCoord() {
        return yCoord;
    }
    
    public int getHitboxWidth() {
        return hitboxWidth;
    }
    
    public int getHitboxHeigth() {
        return hitboxHeigth;
    }
    
    public int getHitboxXCoord() {
        return hitboxXCoord;
    }
    
    public int getHitboxYCoord() {
        return hitboxYCoord;
    }
    
    public String getName() {
        return name;
    }
    
    public BufferedImage getSpriteSheet() {
        return spriteSheet;
    }
    
    public int getRowsOfSprites() {
        return rowsOfSprites;
    }
    
    public int getColumnsOfSprites() {
        return columnsOfSprites;
    }
    
    public BufferedImage getSprite() { //sobrecarga
        return spriteMatrix[currentSpriteRow][currentSpriteColumn];
    }
    
    public int getCurrentSpriteRow() {
        return currentSpriteRow;
    }
    
    public int getCurrentSpriteColumn() {
        return currentSpriteColumn;
    }
    
    public BufferedImage getSprite(int rowIndex, int columnIndex) { //para cinematicas?
        return spriteMatrix[rowIndex][columnIndex];
    }
    
    public boolean isInCollisionWithPlayer() {
        return inCollisionWithPlayer;
    }
    
    public boolean isNextToPlayerCharacter() {
        return nextToPlayerCharacter;
    }
    
    public void setXCoord(int newXCoord) {
        xCoord = newXCoord;
    }
    
    public void setYCoord(int newYCoord) {
        yCoord = newYCoord;
    }
    
    public void setHitboxWidth(int hitboxWidth) {
        this.hitboxWidth = hitboxWidth;
    }
    
    public void setHitboxHeigth(int hitboxHeigth) {
        this.hitboxHeigth = hitboxHeigth;
    }
    
    public void setHitboxXCoord(int hitboxXCoord) {
        this.hitboxXCoord = hitboxXCoord;
    }
    
    public void setHitboxYCoord(int hitboxYCoord) {
        this.hitboxYCoord = hitboxYCoord;
    }
    
    public void setName(String newName) { //Should not use
        name = newName;
    }
    
    public void setSpriteSheet(BufferedImage newSpriteSheet) { //should not use
        spriteSheet = newSpriteSheet;
    }
    
    public void setRowsOfSprites(int newRowsOfSprites) {
        rowsOfSprites = newRowsOfSprites;
    }
    
    public void setColumnsOfSprites(int newColumnsOfSprites) {
        columnsOfSprites = newColumnsOfSprites;
    }
    
    public void setSpriteInMatrix(BufferedImage newSprite, int rowIndex, int columnIndex) { //should not use
        spriteMatrix[rowIndex][columnIndex] = newSprite;
    }
    
    public void setCurrentSpriteRow(int newSpriteRow) {
        currentSpriteRow = newSpriteRow;
    }
    
    public void setCurrentSpriteColumn(int newSpriteColumn) {
        currentSpriteColumn = newSpriteColumn;
    }
    
    public void setCurrentSpriteRowAndColumn(int newSpriteRow, int newSpriteColumn) {
        currentSpriteRow = newSpriteRow;
        currentSpriteColumn = newSpriteColumn;
    }
    
    public void setInCollisionWithPlayer(boolean collision) {
        inCollisionWithPlayer = collision;
    }
    
    public void setNextToPlayerCharacter(boolean nextToPlayerCharacter) {
        this.nextToPlayerCharacter = nextToPlayerCharacter;
    }
    
    private void getSpritesFromSpriteSheet() {
        int i, j;
        
        for(i = 0; i < rowsOfSprites; i++) {
           
            for(j = 0; j < columnsOfSprites; j++) {
                
                spriteMatrix[i][j] = spriteSheet.getSubimage(j*16, i*24, 16, 24);
            }
        }
    }
    
    public boolean isPlayerInEntityHitbox(int playerXCoord, int playerYCoord) { //dentro del espacio del sprite
        
       if( ( playerXCoord >= getXCoord() - getHitboxWidth() + getHitboxXCoord() && //Izquierda
             playerXCoord <= getXCoord() + getHitboxWidth() + getHitboxXCoord() ) && //Derecha
           ( playerYCoord + 12 >= getYCoord() - getHitboxHeigth() + getHitboxYCoord() && // Arriba
             playerYCoord + 18 <= getYCoord() + getHitboxHeigth() + getHitboxYCoord()) ) /*Abajo*/ { 
           
            inCollisionWithPlayer = true;
        } else {
            inCollisionWithPlayer = false;
        }
        
        return inCollisionWithPlayer;
    }
    
    public boolean isPlayerInEntitySoftbox(int playerXCoord, int playerYCoord) { //justo al lado
        
       if( ( playerXCoord >= getXCoord() - getHitboxWidth() + getHitboxXCoord() - 1 && //Izquierda
             playerXCoord <= getXCoord() + getHitboxWidth() + getHitboxXCoord() + 1 ) && //Derecha
           ( playerYCoord + 12 >= getYCoord() - getHitboxHeigth() + getHitboxYCoord() - 1 && // Arriba
             playerYCoord + 18 <= getYCoord() + getHitboxHeigth() + getHitboxYCoord() + 1) ) /*Abajo*/ { 
           
            inCollisionWithPlayer = true;
        } else {
            inCollisionWithPlayer = false;
        }
        
        return inCollisionWithPlayer;
    }
    
}
