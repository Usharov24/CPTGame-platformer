package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public class Bullet extends GameObject {

    private BufferedImage biTexture;

    private float fltExplosionRadius;
    private boolean blnHoming;
    private float fltDmg;
    private int intBleedCount;
    private float fltBurnDmg;
    private int intPeirceCount;
    private float fltLifeSteal;
    private int intSender;
    private int intCelebShot;

    public Bullet(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, int intPeirceCount, int intBleedCount, float fltBurnDmg, float fltLifeSteal, int intCelebShot, float fltDmg, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biTexture, Boolean blnHoming, float fltExplosionRadius, int intSender) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biTexture = biTexture;
        this.blnHoming = blnHoming;
        this.fltExplosionRadius = fltExplosionRadius;
        this.fltDmg = fltDmg;
        this.intBleedCount = intBleedCount;
        this.fltBurnDmg = fltBurnDmg;
        this.intPeirceCount = intPeirceCount;
        this.intSender = intSender;
        this.intCelebShot = intCelebShot;
        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        if(fltWorldX < 0 || fltWorldX > 1920 || fltWorldY < 0 || fltWorldY > 1440) handler.removeObject(this);

        if(blnHoming == false) {
            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
        } else {
            float fltTargetX = findNearestObject(fltWorldX, fltWorldY).getWorldX();
            float fltTargetY = findNearestObject(fltWorldX, fltWorldY).getWorldY();
            if(fltWorldX > fltTargetX){
                fltVelX -= 5; 
            }
            if(fltWorldX < fltTargetX){
                fltVelX += 5; 
            }
            if(fltWorldY > fltTargetY){
                fltVelY -= 5; 
            }
            if(fltWorldY < fltTargetY){
                fltVelY += 5; 
            }

            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biTexture, (int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 9000000;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++) {
            if(handler.getObject(i).getId() == ObjectId.ENEMY) {
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist < fltpastTotal){
                    fltpastTotal = flttotaldist;
                    intreturn = i;
                }
            }
        }
        return handler.getObject(intreturn);
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++){
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(fltExplosionRadius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, fltDmg, fltExplosionRadius*2, fltExplosionRadius*2,ObjectId.BOOM, handler, ssm));
                    }        
                }
            }        
        }
    }

    public int getPeirce(){
        return intPeirceCount;
    }

    public void setPeirce(int intPeirceCount){
        this.intPeirceCount = intPeirceCount;
    }
    //the two functions are used to determine when a bullet should despawn

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
