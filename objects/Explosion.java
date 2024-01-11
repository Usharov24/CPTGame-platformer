package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class Explosion extends GameObject {

    private ObjectHandler handler;
    public Explosion(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        fltWidth -= 15;
        fltWorldX += 7.5;
        fltWorldY += 7.5;
        fltHeight -= 15;
        if(fltWidth < 0){
            handler.removeObject(this);
        }
        if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }
}
