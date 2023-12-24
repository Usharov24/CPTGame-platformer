package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class HomingBullet extends GameObject {

    private ObjectHandler handler;
    
    public HomingBullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, int intSender) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        
        if(Main.intSessionId == 1) ssm.sendText("h>aHOMING_BULLET~" + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + intSender);
        else ssm.sendText("c" + Main.intSessionId + ">aHOMING_BULLET~" + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + intSender);
    }
        
    public void update(LinkedList<GameObject> objectList) {
        if(fltX > 540){
            fltVelX -= 2; 
        }
         if(fltX < 540){
            fltVelX += 2; 
        }
         if(fltY > 360){
            fltVelY -= 2; 
        }
         if(fltY < 360){
            fltVelY += 2; 
        }
        fltX += fltVelX;
        fltY += fltVelY;

        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }
   

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return null;
    }
}
