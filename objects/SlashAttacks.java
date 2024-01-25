package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;


public class SlashAttacks extends GameObject {

    private float fltStartAngle;
    /**
    The float fltangle
     **/ 
    private long lngbirth;
    /**
    The lng birth day
     **/ 
    private int intBoomRad;
     /**
    The int which will store the value for the explosion radius
     **/ 
    private float fltDmg;
     /**
    The float which will store the value for damage
     **/ 
    private int intBleedCount;
     /**
    The int which will store the value for the bleed count
     **/ 
    private float fltBurnDmg;
     /**
    The float which will store the value for the burn damage
     **/ 
    private float fltLifeSteal;
    /**
    The float which will store the value for the life steal
     **/ 
    private int intSender;
     /**
    The int which will store the value for the intsender
     **/ 
    private int intCelebShot;
     /**
    The int which will store the value celebratory shot count
     **/ 
    /**
     * Constructor for the Knight
     * @param fltWorldX - float value for the X position of the knight
     * @param fltWorldY - float value for the Y position of the knight
     * @param fltVelX - float value for the velocity X
     * @param fltVelY - float value for the velocity Y
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param fltWidth - the width
     * @param fltHeight - the height
     */
    public SlashAttacks(float fltWorldX, float fltWorldY, float fltVelX, long lngbirth, float fltWidth, float fltHeight, float fltStartAngle, float fltDmg, int intBoomRad, float fltBurnDmg, int intBleedCount, float fltLifeSteal, int intCelebShot, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.lngbirth = lngbirth;
        this.intBoomRad = intBoomRad;
        this.id = id;
        this.fltDmg = fltDmg;
        this.intBleedCount = intBleedCount;
        this.fltBurnDmg = fltBurnDmg;
        this.intCelebShot = intCelebShot;
        //all the characteristics for the slashes
        camObject = handler.getObject(Main.intSessionId - 1);
    }

    
    public void update() {
        
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
        //moves the slash in a direction
        if(System.currentTimeMillis() - lngbirth > 100){
            if(intBoomRad > 0){
                handler.addObject(new Explosion(fltWorldX - intBoomRad/2, fltWorldY - intBoomRad/2, fltDmg, intBoomRad*2, intBoomRad*2,ObjectId.BOOM, handler, ssm));
                //timer to remove the object which also checks to make sure that the slash explodes if needed
            }  
            handler.removeObject(this);
        }

        collisions();
        //checks if the slash is out of bounds
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        
        for(int intCount = -2; intCount < 3; intCount++) {
            g.drawArc((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) + intCount, (int) (fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45, -90);
        }
        //drawing of the arc
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //the hitbox of the slash
    }
    /**
     * @return void 
     * this checks for collisions
     */
    private void collisions() {
        
        if(fltWorldX < 0 || fltWorldX > 1920){
            handler.removeObject(this);
        }
        //checks if the slash collided with the outside barriers
    }

    /**
     * @return int 
     * this method reutnrs int
     */
    public int getBoom(){
        return intBoomRad;
    }

    /**
     * @return float 
     * this method returns dmg
     */
    public float getDmg(){
        return fltDmg;
    }
    /**
     * @return float 
     * this method is burn
     */
    public float getBurn(){
        return fltBurnDmg;
    }
    /**
     * @return float 
     * this method is bleed
     */
    public float getBleed(){
        return intBleedCount;
    }
    /**
     * @return int 
     * this method is char
     */
    public int getChar(){
        return this.intSender;
    }
    /**
     * @return float 
     * this method is lifesteal
     */
    public float getLifeSteal(){
        return this.fltLifeSteal;
    }
    /**
     * @return int 
     * this method is celeb shot
     */
    public int getCelebShot(){
        return this.intCelebShot;
    }
    /**
     * @return float 
     * this method is velx
     */
    public float getVelX(){
        return this.fltVelX;
    }
    /**
     * @return float 
     * this method is vely
     */
    public float getVelY(){
        return this.fltVelY;
    }
}
