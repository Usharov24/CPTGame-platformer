package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import java.awt.Rectangle;


public class Bullet extends GameObject {

    private ObjectHandler handler;
    
    
    public Bullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, int intsender) {
        super(fltX, fltY, fltWidth, fltHeight, id);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        
        if(Main.ssm!=null && Main.intSessionId == intsender){
            Main.ssm.sendText("o,BULLET," + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + id + "," + handler + "," + intsender);
            
        }
        
    }
    
    public void update(LinkedList<GameObject> objectList) {
    
        fltX += fltVelX;
        fltY += fltVelY;

        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return null;
    }
}
