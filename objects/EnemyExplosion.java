package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class EnemyExplosion extends GameObject {
    private float fltDmg;
    public EnemyExplosion(float fltWorldX, float fltWorldY, float fltDmg, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltDmg = fltDmg;
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        fltWidth -= 15;
        fltHeight -= 15;
        //makes the explosions smaller
        if(fltWidth < 0){
            handler.removeObject(this);
            //when explosions disapear, remove them
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //draws explosion
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY -fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //hitbox
    }

    public float getDmg(){
        return this.fltDmg;
        //ensures players take the correct amount of dmg
    }
}
