package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class KnightSlashes extends GameObject {

    private float fltStartAngle;
    private long lngbirth;
    private int intBoomRad;
    
    public KnightSlashes(float fltWorldX, float fltWorldY, float fltVelX, long lngbirth, float fltWidth, float fltHeight, float fltStartAngle, float fltDmg, int intBoomRad, float fltBurnDmg, int intBleedCount, float fltLifeSteal, int intCelebShot, int intPeirceCount, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltStartAngle = (float)Math.toDegrees(fltStartAngle);
        this.lngbirth = lngbirth;
        this.intBoomRad = intBoomRad;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        
        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
        
        if(System.currentTimeMillis() - lngbirth > 100){
            if(intBoomRad > 0){
                handler.addObject(new Explosion(fltWorldX - intBoomRad/2, fltWorldY - intBoomRad/2, intBoomRad*2, intBoomRad*2,ObjectId.BOOM, handler, ssm));
                //arbitary value to make sure bomb doesnt explode multiple times
            }  
            handler.removeObject(this);
        }
        collisions();
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);

        for(int intCount = -2; intCount < 3; intCount++) {
            g.drawArc((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) + intCount, (int) (fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int) fltWidth, (int) fltHeight, (int)fltStartAngle + 45, -90);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
    private void collisions() {
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(intBoomRad > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - intBoomRad/2, fltWorldY - intBoomRad/2, intBoomRad*2, intBoomRad*2,ObjectId.BOOM, handler, ssm));
                        //arbitary value to make sure bomb doesnt explode multiple times
                    }        
                }
            }
            if(handler.getObject(i).getId() == ObjectId.BARRIER){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(intBoomRad > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - intBoomRad/2, fltWorldY - intBoomRad/2, intBoomRad*2, intBoomRad*2,ObjectId.BOOM, handler, ssm));
                        //arbitary value to make sure bomb doesnt explode multiple times
                    }        
                }
            }        
        }
    }
}
