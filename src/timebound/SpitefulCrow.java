
package timebound;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Victor
 */
public class SpitefulCrow extends EnemyEntity{

    public SpitefulCrow(String name, int xCoord, int yCoord) {
        
        super(xCoord, yCoord, name, "spitefulCrow", 1, 3, 16, 8, 0, 16,
              24, 0, 5, 3, 77, 0,"spiteful_crow_bg", "spiteful_crow_battle",
              "spiteful_crow_battle_sprite", 30, 34);
        
        super.setCurrentSpriteRowAndColumn(0,1);
        
        super.initializeSelectedMoveDescriptionArray(2);
        super.setSelectedMoveDescription(0, "has_  a wide grin on its face."); 
        super.setSelectedMoveDescription(1, "pecked_  at your eyes!");
    }
    
    @Override
    public void selectMove() {
        setSelectedMove(ThreadLocalRandom.current().nextInt(0, 2)); //0-1
        
        switch(getSelectedMove()) { //Esto cambia para otros enemigos
            case 0: setCategoryOfSelectedMove(0);
                    break;
            case 1: setCategoryOfSelectedMove(1);
                    break;
        }
        /*
            Este enemigo no tiene ataques complicados, por lo cual solamente
            puede atacar (1) o no hacer nada (0)
        */
    }
    
    @Override
    public int getDamageFromSelectedMove() {
        return getOffense() + ThreadLocalRandom.current().nextInt(-2, 3);
    }

    @Override
    public void moveTowardsPlayer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void routine() {
       //Nothing for the moment
    }
}
