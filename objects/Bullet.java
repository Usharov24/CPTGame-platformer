package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class Bullet extends GameObject {

    private ObjectHandler handler;
    private boolean blnHoming;
    private BufferedImage biImg;
    private float fltexplosionradius;
    
    public Bullet(float fltX, float fltY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, Boolean blnHoming, BufferedImage biImg, float fltexplosionradius) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        this.blnHoming = blnHoming;
        this.biImg = biImg;
        this.fltexplosionradius = fltexplosionradius;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        if(blnHoming == false){
            fltX += fltVelX;
            fltY += fltVelY;
            collisions();
        }   
        else{
            float fltTargetX = findNearestObject(fltX, fltY).getX();
            float fltTargetY = findNearestObject(fltX, fltY).getY();
            if(fltX > fltTargetX){
                fltVelX -= 5; 
            }
            if(fltX < fltTargetX){
                fltVelX += 5; 
            }
            if(fltY > fltTargetY){
                fltVelY -= 5; 
            }
            if(fltY < fltTargetY){
                fltVelY += 5; 
            }
            fltX += fltVelX;
            fltY += fltVelY;
            collisions();
        }

        if(fltX > 1280 || fltX < 0 || fltY > 720 || fltY < 0){
            handler.removeObject(this);
        }

        
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
        g.drawImage(biImg, (int)fltX, (int)fltY, null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public GameObject findNearestObject(float fltX, float fltY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < handler.sizeHandler(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                fltDistX = fltX - handler.getObject(i).getX();
                fltDistY = fltY - handler.getObject(i).getY();
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
        for(int i = 0; i < handler.sizeHandler(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    handler.removeObject(this);
                    if(fltexplosionradius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltX - fltexplosionradius/2, fltY - fltexplosionradius/2,fltexplosionradius*2,fltexplosionradius*2,ObjectId.BOOM,ssm,handler));
                        //arbitary value to make sure bomb doesnt explode multiple times
                        fltX = 10000;

                    }        
                }
            }        
        }
    }
}
