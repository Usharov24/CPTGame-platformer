
package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

import java.awt.Color;

public class EnemyBullet extends GameObject {

    private BufferedImage biTexture;

    private float fltExplosionRadius;
    private boolean blnHoming;
    private float fltDmg;

    public EnemyBullet(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, float fltDmg, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biTexture, Boolean blnHoming, float fltExplosionRadius) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biTexture = biTexture;
        this.blnHoming = blnHoming;
        this.fltDmg = fltDmg;
        this.fltExplosionRadius = fltExplosionRadius;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        if(fltWorldX < 0 || fltWorldX > 1920 || fltWorldY < 0 || fltWorldY > 1440) handler.removeObject(this);

        if(blnHoming == false){
            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            collisions();
        }   
        else{
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
            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            collisions();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biTexture, (int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), null);
        g.setColor(Color.red);

        
        
        g.fillRect((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++) {
            if(handler.getObject(i).getId() == ObjectId.PLAYER) {
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(flttotaldist > fltpastTotal){
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

            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER){
                if(getBounds().intersects(object.getBounds())){
                    handler.removeObject(this);
                    if(fltExplosionRadius > 0){
                        handler.addObject(new EnemyExplosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2, fltDmg, fltExplosionRadius*2, fltExplosionRadius*2,ObjectId.ENEMY_BOOM, handler, ssm));
                        //arbitary value to make sure bomb doesnt explode multiple times
                    }        
                }
            }      
        }
    }

    public float getDmg(){
        return fltDmg;
    }
}
