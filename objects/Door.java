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
                    continue;
                }
            }
            
            if(object.getId() == ObjectId.PLAYER && getBounds().intersects(object.getBounds())) {
                intPlayersEntered[intCount] = 1;
            } else if(object.getId() == ObjectId.PLAYER) {
                intPlayersEntered[intCount] = 0;
            }
        }

        blnRoomCleared = true;
              
        if(Main.intSessionId == 1 && intPlayersEntered[0] + intPlayersEntered[1] + intPlayersEntered[2] + intPlayersEntered[3] == Main.intServerSize) {
            // Remove any remaining object from the list
            // Ex. Enemies, barriers, items, bullets, doors, etc.
            handler.clearEntities();

            GameObject object = handler.getObject(Main.intSessionId - 1);

            if(object instanceof Sniper) {
                Sniper sniper = (Sniper)object;
                sniper.setWorldX(150 + 40 * (Main.intSessionId - 1));
                sniper.setWorldY(1400);
            } else if(object instanceof Brute) {
                Brute brute = (Brute)object;
                brute.setWorldX(150 + 40 * (Main.intSessionId - 1));
                brute.setWorldY(1400);
            } else if(object instanceof Knight) {
                Knight knight = (Knight)object;
                knight.setWorldX(150 + 40 * (Main.intSessionId - 1));
                knight.setWorldY(1400);
            } else if(object instanceof Wizard) {
                Wizard wizard = (Wizard)object;
                wizard.setWorldX(150 + 40 * (Main.intSessionId - 1));
                wizard.setWorldY(1400);
            }

            for(int intCount = 0; intCount < intPlayersEntered.length; intCount++) {
                intPlayersEntered[intCount] = 0;
            }

            Main.intRoomCount++;
            if(ssm != null) ssm.sendText("h>a>mNEXT_ROOM");
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
