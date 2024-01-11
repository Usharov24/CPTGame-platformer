package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;
import framework.Main;

public class Bullet extends GameObject {

    private ObjectHandler handler;
    private BufferedImage biTexture;

    private float fltExplosionRadius;
    private boolean blnHoming;
    
    public Bullet(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, BufferedImage biTexture, Boolean blnHoming, float fltExplosionRadius) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        this.biTexture = biTexture;
        this.blnHoming = blnHoming;
        this.fltExplosionRadius = fltExplosionRadius;
        System.out.println("bullet made");
    }
    
    public void update(LinkedList<GameObject> objectList) {
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

        if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0){
            handler.removeObject(this);
        }

        
    }

    public void draw(Graphics g) {
        g.drawImage(biTexture, (int)(fltWorldX - fltWidth/2),(int)(fltWorldY - fltHeight/2), null);
        g.fillRect((int)fltWorldX, (int)fltWorldX, 10, 10);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2), (int)(fltWorldY - fltHeight/2), (int)fltWidth, (int)fltHeight);
    }

    public GameObject findNearestObject(float fltWorldX, float fltWorldY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
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
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(fltExplosionRadius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltExplosionRadius/2, fltWorldY - fltExplosionRadius/2,fltExplosionRadius*2,fltExplosionRadius*2,ObjectId.BOOM,ssm,handler));
                        //arbitary value to make sure bomb doesnt explode multiple times
                        fltWorldX = 10000;
                    }        
                }
            }        
        }
    }
}
