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
    private long lngbirth;
    private int intBoomRad;
    private float fltExplosionRadius;
    private float fltDmg;
    private int intBleedCount;
    private float fltBurnDmg;
    private float fltLifeSteal;
    private int intSender;
    private int intCelebShot;
    
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
    private void collisions() {
        
        if(fltWorldX < 0 || fltWorldX > 1920){
            handler.removeObject(this);
        }
        //checks if the slash collided with the outside barriers
    }

    public int getBoom(){
        return (int)fltExplosionRadius;
    }
    //used to make sure enemies explode when needed
    public float getDmg(){
        return fltDmg;
    }
    //afflicts dmg to enemies
    public float getBurn(){
        return fltBurnDmg;
    }
    //burn dmg which is a type of dmg that ticks every frame
    public float getBleed(){
        return intBleedCount;
    }
    //bleed dmg which does a burst of dmg after 5 strikes
    public int getChar(){
        return this.intSender;
    }
    //used for lifesteal
    public float getLifeSteal(){
        return this.fltLifeSteal;
    }
    //used for lifesteal as well
    public int getCelebShot(){
        return this.intCelebShot;
    }
    //used to spot in bullets from the item
    public float getVelX(){
        return this.fltVelX;
    }
    //returns x velocity
    public float getVelY(){
        return 0;
    }
    //returns y velocity
}
