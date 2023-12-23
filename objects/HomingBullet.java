package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.Rectangle;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;


public class HomingBullet extends GameObject {

    private ObjectHandler handler;
    
    

    
    public HomingBullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, int intsender) {
        super(fltX, fltY, fltWidth, fltHeight, id);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        if(Main.ssm!=null){
            Main.ssm.sendText("o,HOMINGBULLET," + fltX + "," + fltY + "," + fltVelX + "," + fltVelY + "," + fltWidth + "," + fltHeight + "," + id + "," + handler + "," + intsender);
            
        }

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
