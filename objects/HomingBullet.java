package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class HomingBullet extends GameObject {

    private ObjectHandler handler;
    
    public HomingBullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
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
