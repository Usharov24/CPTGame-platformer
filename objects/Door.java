package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class Door extends GameObject {
    /**
    The int value for number of playered enetered
    **/
    private static int[] intPlayersEntered = new int[4];
    /**
    The boolean value for room cleared
    **/
    public static boolean blnRoomCleared;
    /**
    The gameobject which follows the player
    **/
    private GameObject camObject;
    /**
    The bufferedimage which is the door sprite
    **/
    private BufferedImage[] biTextures;
     /**
     * Constructor for the Door
     * @param fltWorldX - float value for the X position of the door
     * @param fltWorldY - float value for the Y position of the door
     * @param fltWidth - float value for the width of the door
     * @param fltHeight - float value for the height of the door
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param biTextures - the sprite of the
     * @param intPosition - float value for the width of the door
     * 
     **/
    public Door(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, BufferedImage[] biTextures, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.biTextures = biTextures;

        for(int intCount = 0; intCount < intPlayersEntered.length; intCount++) {
            intPlayersEntered[intCount] = 0;
        }
        blnRoomCleared = false;
        //used to open/close the door

        camObject = handler.getObject(Main.intSessionId - 1);
    }

    public void update() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
            
            // Check to see if the room is clear of enemies
            if(!blnRoomCleared && object.getId() == ObjectId.ENEMY) {
                Enemy enemy = (Enemy)object;
                
                if(enemy.getHP() > 0) {
                    return;
                } else {
                    continue;
                }
                //when all enemies are dead, continue the code
            }
            
            if(object.getId() == ObjectId.PLAYER && getBounds().intersects(object.getBounds())) {
                intPlayersEntered[intCount] = 1;
            } else if(object.getId() == ObjectId.PLAYER) {
                intPlayersEntered[intCount] = 0;
            }
            //used to ensure all players enter the door
        }

        blnRoomCleared = true;
        //when all enemies are dead, set the room to be cleared
        if(Main.intSessionId == 1 && intPlayersEntered[0] + intPlayersEntered[1] + intPlayersEntered[2] + intPlayersEntered[3] == Main.intServerSize - (Main.intAlivePlayers[0] + Main.intAlivePlayers[1] + Main.intAlivePlayers[2] + Main.intAlivePlayers[3])) {
            // Remove any remaining object from the list
            // Ex. Enemies, barriers, items, bullets, doors, etc.
            handler.clearEntities();

            GameObject object = handler.getObject(Main.intSessionId - 1);

            if(object instanceof Sniper) {
                Sniper door = (Sniper)object;
                door.setWorldX(150 + 40 * (Main.intSessionId - 1));
                door.setWorldY(1400);
                if(door.getHP() <= 0) door.setHP(door.getMaxHP()/2);
            } else if(object instanceof Brute) {
                Brute brute = (Brute)object;
                brute.setWorldX(150 + 40 * (Main.intSessionId - 1));
                brute.setWorldY(1400);
                if(brute.getHP() <= 0) brute.setHP(brute.getMaxHP()/2);
            } else if(object instanceof Knight) {
                Knight knight = (Knight)object;
                knight.setWorldX(150 + 40 * (Main.intSessionId - 1));
                knight.setWorldY(1400);
                if(knight.getHP() <= 0) knight.setHP(knight.getMaxHP()/2);
            } else if(object instanceof Wizard) {
                Wizard wizard = (Wizard)object;
                wizard.setWorldX(150 + 40 * (Main.intSessionId - 1));
                wizard.setWorldY(1400);
                if(wizard.getHP() <= 0) wizard.setHP(wizard.getMaxHP()/2);
            }
            //when transferring to another level, move all the characters to a space

            for(int intCount = 0; intCount < intPlayersEntered.length; intCount++) {
                intPlayersEntered[intCount] = 0;
            }

            Main.intRoomCount++;
            if(ssm != null) ssm.sendText("h>a>mNEXT_ROOM");
            //sends a msg to clients to load in the next room
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        if(!blnRoomCleared) g2d.drawImage(biTextures[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        else g2d.drawImage(biTextures[1], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        //draws a different sprite depending on whether or not the room has been cleared
    }

    public Rectangle getBounds() {
        if(blnRoomCleared) return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        else return new Rectangle();
        //hit box for the door
    }
}
