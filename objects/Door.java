package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class Door extends GameObject {

    private static int[] intPlayersEntered = new int[4];
    public static boolean blnRoomCleared;

    private GameObject camObject;
    private BufferedImage[] biTextures;

    public Door(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, BufferedImage[] biTextures, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.biTextures = biTextures;

        for(int intCount = 0; intCount < intPlayersEntered.length; intCount++) {
            intPlayersEntered[intCount] = 0;
        }
        blnRoomCleared = false;

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
                    if(intCount == handler.objectList.size() - 1) blnRoomCleared = true;
                    continue;
                }
            }
            
            if(object.getId() == ObjectId.PLAYER && getBounds().intersects(object.getBounds())) {
                intPlayersEntered[intCount] = 1;
            } else if(object.getId() == ObjectId.PLAYER) {
                intPlayersEntered[intCount] = 0;
            }
        }
              
        if(intPlayersEntered[0] + intPlayersEntered[1] + intPlayersEntered[2] + intPlayersEntered[3] == Main.intServerSize) {
            // Remove any remaining object from the list
            // Ex. Enemies, barriers, items, bullets, doors, etc.
            handler.clearEntities();

            for(int intCount = 0; intCount < intPlayersEntered.length; intCount++) {
                intPlayersEntered[intCount] = 0;
            }

            // This is incremented multiple times when a player moves into the door's hitbox
            // Need to find a solution where it is only incremented one time
            Main.intRoomCount++;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        if(!blnRoomCleared) g2d.drawImage(biTextures[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        else g2d.drawImage(biTextures[1], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        if(blnRoomCleared) return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        else return new Rectangle();
    }
}
