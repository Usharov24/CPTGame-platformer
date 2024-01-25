package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class Explosion extends GameObject {
    private float fltDmg;
    public Explosion(float fltWorldX, float fltWorldY, float fltDmg, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltDmg = fltDmg;
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        fltWidth -= 15;
        fltHeight -= 15;
        //slowly decreases in size
        if(fltWidth < 0){
            handler.removeObject(this);
            //when explosion is gone, remove it
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //draw the explosion
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY -fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //hitbox of the explosion
    }

    public float getDmg(){
        return this.fltDmg;
    }
    //damage used to hurt enemies
}
