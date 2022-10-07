
package timebound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class BackgroundMap {
    
    private static BufferedImage[] mapArray;
    private static BufferedImage mapSection;
    private static final String[] mapMusic = {"Oneth_theme", "cancion"};
    private static final boolean[] mapMusicLoopable = {true, false};
    private static boolean changingMap;
    private static int currentMapIndex;
    
    public BackgroundMap() {
        
        mapArray = new BufferedImage[2];
        changingMap = false;
        currentMapIndex = 0;
        
        try{
            mapArray[0] = ImageIO.read(new File("./src/resources/maps/Onett.png"));
            mapArray[1] = ImageIO.read(new File("./src/resources/maps/Onett_G.png"));
        }catch(NullPointerException | IOException e) {
            System.out.println("No se encontraron los mapas");
            e.printStackTrace();
        }
    }
     
    public final static BufferedImage getMapSection(int x, int y) {
        
        try {
            mapSection = mapArray[currentMapIndex].getSubimage(x, y, 256, 240);
        } catch(Exception e) {
            
        }
        
        return mapSection;   
    }
    
    public final static void setMap(int index) {
        currentMapIndex = index;
        changingMap = true;
    }
    
    public final static void setChangingMap(boolean status) { //finished changing maps = false
        changingMap = status;
    }
    
    public final static boolean isChangingMap() {
        return changingMap;
    }
    
    public final static int getMap() {
        return currentMapIndex;
    }
    
}
