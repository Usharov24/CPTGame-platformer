package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class Explosion extends GameObject {
     /**
    The float which will store the value for dmg

     **/ 
    private float fltDmg;
    
    /**
     * Constructor for the Sniper
     * @param fltWorldX - float value for the X position of the Sniper
     * @param fltWorldY - float value for the Y position of the Sniper
     * @param fltWidth - float value for the width of the Sniper
     * @param fltHeight - float value for the height of the Sniper
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param fltDmg - the dmg of players
     **/
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
    // World Y Coordinate Getter
    /**
     * @return float 
     * returns the dmg
     */
    public float getDmg(){
        return this.fltDmg;
    }
    //damage used to hurt enemies
}
