package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;


public class WaveAttacks extends GameObject {

    private float fltStartAngle;
    private float fltSpread = 1;
    private int intBoomRad;
    private float fltDmg;
    private int intBleedCount;
    private float fltBurnDmg;
    private float fltLifeSteal;
    private int intSender;
    private int intCelebShot;

    public WaveAttacks(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltStartAngle, float fltDmg, int intBoomRad, float fltBurnDmg, int intBleedCount, float fltLifeSteal, int intCelebShot, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.handler = handler;
        this.intBoomRad = intBoomRad;
        this.id = ObjectId.WAVE;
        this.fltDmg = fltDmg;
        this.intBleedCount = intBleedCount;
        this.fltBurnDmg = fltBurnDmg;
        this.intCelebShot = intCelebShot;
        //all necessary parameters
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        fltSpread += 15;
        //widens the area of the wave with each update
        collisions();

        fltWorldX+= fltVelX;
        fltWorldY += fltVelY;
        //moves the wave in the direction
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2), (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2), (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-1, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-1, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-2, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-2, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-3, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-3, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-4, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-4, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        //draws an arc
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        //returns the hitboxes of the arc
    }

    private void collisions() {
        
        if(fltWorldX < 0 || fltWorldX > 1920){
            handler.removeObject(this);
        }
        //if the arc leaves the room, remove it
    }

    public int getBoom(){
        return intBoomRad;
    }
    public float getDmg(){
        return fltDmg;
    }

    public float getBurn(){
        return fltBurnDmg;
    }

    public float getBleed(){
        return intBleedCount;
    }

    public int getChar(){
        return this.intSender;
    }

    public float getLifeSteal(){
        return this.fltLifeSteal;
    }

    public int getCelebShot(){
        return this.intCelebShot;
    }

    public float getVelX(){
        return this.fltVelX;
    }

    public float getVelY(){
        return this.fltVelY;
    }
    //all used in the enemy class to ensure enemies take the correct amount of dmg
}
