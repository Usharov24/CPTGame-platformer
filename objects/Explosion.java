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
    public Explosion(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        fltWidth -= 15;
        fltX += 7.5;
        fltY += 7.5;
        fltHeight -= 15;
        if(fltWidth < 0){
            handler.removeObject(this);
        }
        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
