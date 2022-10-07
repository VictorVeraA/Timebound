
package timebound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.util.Timer;
import java.util.TimerTask;

public class KeyboardFunctions extends JComponent { //Abstract y heredarlo para movimiento y menus
        
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    
    public static final int ACT = 4;
    public static final int CANCEL = 5;
    public static final int MENU = 6;
    
    public static final int RELEASE_LEFT = 7;
    public static final int RELEASE_UP = 8;
    public static final int RELEASE_RIGHT = 9;
    public static final int RELEASE_DOWN = 10;
    
    
    private static final String[] actionNames = new String[] {"Move left",
        "Move up", "Move right", "Move down", "Inteact action", "Cancel action",
        "Toggle menu", "Release left", "Release up", "Release right", "Release down"};
    
    private final AbstractAction[] absArray;
    private boolean[] movingDirectionArray;
    private boolean pressingKeys;
    
    private Timer keyboardEventUpdateSchedule;
    private TimerTask keyboardUpdate;
    
    private int xCoordIncrement;
    private int yCoordIncrement;
    
    private int direction;
    private int alternateSprite;
    private int millisForAltSprite;
    
    private int actionCounter;
    private boolean mixUp;
    
    public KeyboardFunctions() {  
        this.pressingKeys = false;
        this.xCoordIncrement = 0;
        this.yCoordIncrement = 0;
        this.actionCounter = 0;
        this.direction = 3;
        this.mixUp = false;
        
        this.millisForAltSprite = 0;
        this.alternateSprite = 0;
                                            //LEFT    UP    RIGHT  DOWN   ACT   CANCEL  MENU
        movingDirectionArray = new boolean[] {false, false, false, false, false, false, false};
        
        keyboardUpdate = new TimerTask() {
            @Override
            public void run() {
                
                if (pressingKeys) {
                    //Needs work

                    if( (movingDirectionArray[UP] && movingDirectionArray[DOWN]) ||
                        (movingDirectionArray[LEFT] && movingDirectionArray[RIGHT]) ) {
                        
                        millisForAltSprite = 10;
                        xCoordIncrement = 0;
                        yCoordIncrement = 0;
                        
                    } else if( (movingDirectionArray[UP] && movingDirectionArray[LEFT]) ||
                        (movingDirectionArray[UP] && movingDirectionArray[RIGHT]) || 
                        (movingDirectionArray[DOWN] && movingDirectionArray[LEFT]) ||
                        (movingDirectionArray[DOWN] && movingDirectionArray[RIGHT]) ) {

                        if (movingDirectionArray[UP] && movingDirectionArray[LEFT]) {
                            direction = 4;
                            xCoordIncrement = -1;
                            yCoordIncrement = -1;
                        }

                        if (movingDirectionArray[UP] && movingDirectionArray[RIGHT]) {
                            direction = 5;
                            xCoordIncrement = 1;
                            yCoordIncrement = -1;
                        }

                        if (movingDirectionArray[DOWN] && movingDirectionArray[LEFT]) {
                            direction = 6;
                            xCoordIncrement = -1;
                            yCoordIncrement = 1;
                        } 

                        if (movingDirectionArray[DOWN] && movingDirectionArray[RIGHT]) {
                            direction = 7;
                            xCoordIncrement = 1;
                            yCoordIncrement = 1;
                        }

                    } else {

                        if (movingDirectionArray[LEFT]) {
                            direction = LEFT;
                            yCoordIncrement = 0;
                            if (mixUp) {
                                xCoordIncrement = -1;
                            } else {
                                xCoordIncrement = -2;
                            }
                        }

                        if (movingDirectionArray[UP]) {
                            direction = UP;
                            xCoordIncrement = 0;
                            if (mixUp) {
                                yCoordIncrement = -1;
                            } else {
                                yCoordIncrement = -2;
                            }
                        }

                        if (movingDirectionArray[RIGHT]) {
                            direction = RIGHT;
                            yCoordIncrement = 0;
                            if (mixUp) {
                                xCoordIncrement = 1;
                            } else {
                                xCoordIncrement = 2;
                            }
                        }

                        if (movingDirectionArray[DOWN]) {
                            direction = DOWN;
                            xCoordIncrement = 0;
                            if (mixUp) {
                                yCoordIncrement = 1;
                            } else {
                                yCoordIncrement = 2;
                            }
                        }
                        
                    }

                    mixUp = !mixUp;
                    
                    millisForAltSprite++;
                    if(millisForAltSprite < 9) {
                        alternateSprite = 1;
                    } else if(millisForAltSprite < 18){
                        alternateSprite = 0; 
                    } else {
                        millisForAltSprite = 0;
                    }                       

                } else {
                    alternateSprite = 0;
                    millisForAltSprite = 0;
                    xCoordIncrement = 0;
                    yCoordIncrement = 0;
                }

            }
            
        }; 
        
        //Abstraer la clase
        
        this.absArray = new AbstractAction[] { 
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { moveLeft(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { moveUp(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { moveRight(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { moveDown(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { toggleAction(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { toggleCancel(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { toggleMenu(); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { releaseKey(LEFT); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { releaseKey(UP); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { releaseKey(RIGHT); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { releaseKey(DOWN); } },
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { releaseKey(ACT); } }
        };
                
        //Default bindings
        //On Press

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false),actionNames[UP]);
        getActionMap().put(actionNames[UP], absArray[UP]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false),actionNames[DOWN]);
        getActionMap().put(actionNames[DOWN], absArray[DOWN]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false),actionNames[RIGHT]);
        getActionMap().put(actionNames[RIGHT], absArray[RIGHT]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false),actionNames[LEFT]);
        getActionMap().put(actionNames[LEFT], absArray[LEFT]);
        
        //Toggle
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false),actionNames[ACT]);
        getActionMap().put(actionNames[ACT], absArray[ACT]);
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false),actionNames[CANCEL]);
        getActionMap().put(actionNames[CANCEL], absArray[CANCEL]);
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false),actionNames[MENU]);
        getActionMap().put(actionNames[MENU], absArray[MENU]);
       
        //Release
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true),actionNames[RELEASE_UP]);
        getActionMap().put(actionNames[RELEASE_UP], absArray[RELEASE_UP]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true),actionNames[RELEASE_DOWN]);
        getActionMap().put(actionNames[RELEASE_DOWN], absArray[RELEASE_DOWN]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true),actionNames[RELEASE_RIGHT]);
        getActionMap().put(actionNames[RELEASE_RIGHT], absArray[RELEASE_RIGHT]);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true),actionNames[RELEASE_LEFT]);
        getActionMap().put(actionNames[RELEASE_LEFT], absArray[RELEASE_LEFT]);
        
        keyboardEventUpdateSchedule =  new Timer();
        keyboardEventUpdateSchedule.schedule(keyboardUpdate, 16, 16);
    }
    
    public void setKeyBinding(String bindedKey, String newKey) {
        
        getInputMap().remove(KeyStroke.getKeyStroke(bindedKey));
        
        getInputMap().put(KeyStroke.getKeyStroke(newKey),
            getInputMap().get(KeyStroke.getKeyStroke(bindedKey)));
        
    }
    
    public void moveRight() {
        this.movingDirectionArray[RIGHT] = true;
        this.pressingKeys = true;
    }

    public void moveLeft() {
        this.movingDirectionArray[LEFT] = true;
        this.pressingKeys = true;
    }

    public void moveUp() {
        this.movingDirectionArray[UP] = true;
        this.pressingKeys = true;
    }

    public void moveDown() {
        this.movingDirectionArray[DOWN] = true;
        this.pressingKeys = true;
    }
    
    public void toggleAction() {
        this.movingDirectionArray[ACT] = !movingDirectionArray[ACT];
        //Y si no pasa nada al final, pues le vuelves a hacer toggle
        //Ponle tu, solo actica, y en otro lado, afuera, desactiva
    }
    
    public void toggleCancel() {
        this.movingDirectionArray[CANCEL] = !movingDirectionArray[CANCEL];
    }
    
    public void toggleMenu() {
        this.movingDirectionArray[MENU] = !movingDirectionArray[MENU];
    }
    
    public boolean getActionStatus() {
        return movingDirectionArray[ACT];
    }
    
    public boolean getCancelStatus() {
        return movingDirectionArray[CANCEL];
    }
    
    public boolean getMenuStatus() {
        return movingDirectionArray[MENU];
    }
    
    public int getDirection() {
        return this.direction;
    }
    
    public boolean isMoving(int direction) {
        return movingDirectionArray[direction];
    }
    
    public int getAlternateSprite() {
        return alternateSprite;
    }
    
    public int getXCoordIncrement() {
        return xCoordIncrement;
    }
    
    public int getYCoordIncrement() {
        return yCoordIncrement;
    }
    
    public boolean isPressingKeys() {
        return pressingKeys;
    }
    
    public void releaseKey(int key) {
        movingDirectionArray[key] = false;
        
        if(!movingDirectionArray[LEFT] && !movingDirectionArray[UP] &&
           !movingDirectionArray[RIGHT] && !movingDirectionArray[DOWN]) {
            this.pressingKeys = false;
        }
    }
    
    //disable all cuando se llame al menu keybindingmanagermovekeys
}
