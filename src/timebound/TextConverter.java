
package timebound;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Victor
 */
public final class TextConverter{
    
    private static BufferedImage sourceFile;
    private static BufferedImage[] letters;
    public static final int[] CHAR_WIDTH = new int[] {2, 4, 3, 6, 10, 8, 3, 4, 4, 4,
                                                      6, 3, 3, 2, 5 , 5, 3, 5, 5, 6,
                                                      5, 5, 5, 5, 5 , 2, 3, 5, 6, 5,
                                                      5, 4, 7, 6, 6 , 6, 5, 5, 6, 6,
                                                      2, 5, 6, 5, 8 , 6, 6, 6, 6, 6, //fixed I
                                                      6, 6, 6, 7, 8 , 6, 6, 5, 3, 6, //this one is pending
                                                      3, 4, 5, 7, 6 , 5, 5, 5, 5, 4,
                                                      5, 5, 2, 3, 5 , 2, 8, 5, 5, 5, //fixed i, j & l 
                                                      5, 4, 5, 4, 5 , 6, 8, 5, 5, 5,
                                                      3};
    
    public TextConverter() {
        try {
            sourceFile = ImageIO.read(new File("./src/resources/spritesheets/letter_sprites/THE_letter_fix.png"));
        } catch (IOException ex) {
            Logger.getLogger(TextConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        letters = new BufferedImage[91];
        
        getLettersFromSourceImage();
    }
    
    public static BufferedImage getSourceFile() {
        return null;
    }
    
    private void getLettersFromSourceImage() {
        int i = 0;
        int j = 0;
        int k = 0;
        boolean finished = false;
        
        while(!finished) {
            
            letters[k] = sourceFile.getSubimage(1 + (j*12) + j*2 ,
                                                1 + (i*12) + i*2, CHAR_WIDTH[k] - 1, 12);
            k++;
            
            j++;
            if(j == 10) {
                i++;
                j = 0;
            }
            
            if(k == 91 ) {
                finished = true;
            }
        }
            
    }
    
    public static BufferedImage getLetter(int asciiCode) {
        return letters[asciiCode - 33];
    }
    
    public static BufferedImage[] getLetterArray(StringBuffer string) {
        BufferedImage[] letterArray;
        letterArray = new BufferedImage[string.length()];
        
        for(int i = 0; i < string.length(); i++) {
            
            if(string.charAt(i) == ' ') {
                letterArray[i] = letters[90]; //espacio
            } else {
                letterArray[i] = letters[string.charAt(i) - 33];
            }
        }
        
        return letterArray; 
    }
    
    
    
}
