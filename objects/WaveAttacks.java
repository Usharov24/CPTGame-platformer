package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


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

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        
        fltSpread += 15;
        fltWorldX+= fltVelX;
        fltWorldY += fltVelY;
        collisions();

    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        //g.drawArc((int)(fltWorldX - Math.abs(Math.cos(fltStartAngle))) , (int) (fltWorldY + Math.abs(Math.sin(fltStartAngle))),intSpread, intSpread, (int)fltStartAngle + 45,  -90);
        //g.drawArc((int)fltWorldX, (int)fltWorldY, (int) (10 + Math.abs(intSpread*Math.cos(fltStartAngle))), (int) (10 + Math.abs(intSpread*Math.sin(fltStartAngle))), (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2), (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2), (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-1, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-1, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-2, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-2, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-3, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-3, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        g.drawArc((int)(fltWorldX-fltSpread/2 - camObject.getWorldX() - camObject.getWidth()/2)-4, (int) (fltWorldY - fltSpread/2 - camObject.getWorldY() - camObject.getHeight()/2)-4, (int) fltSpread, (int) fltSpread, (int)fltStartAngle + 45,  -90);
        
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    private void collisions() {
        
        if(fltWorldX < 0 || fltWorldX > 1920){
            handler.removeObject(this);
        }
        
    }

    public int getBoom(){
        return intBoomRad;
    }
    public float getDMG(){
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
}
