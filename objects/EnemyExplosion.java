package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class EnemyExplosion extends GameObject {

    public EnemyExplosion(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        fltWidth -= 15;
        fltHeight -= 15;
        if(fltWidth < 0){
            handler.removeObject(this);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY -fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
}