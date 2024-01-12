package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class VacGrenade extends GameObject {

    private BufferedImage biImg;
    private boolean blnFalling = true;
    private boolean blnSucking = false;
    private float fltpastVelX;
    private long lngBirth;
    public VacGrenade(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, long lngBirth, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biImg, float fltexplosionradius) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.handler = handler;
        this.biImg = biImg;
        this.lngBirth = lngBirth;
        fltpastVelX = fltVelX;
    }
    
    public void update(LinkedList<GameObject> objectList) {
        if(blnSucking == false){
                collisions(); 
                if(blnFalling){
                    fltVelY+= 2;
                    
                }
                else{
                    if(fltVelX > 0)fltVelX -= fltpastVelX/8;
                    if(fltVelX < 0)fltVelX += fltpastVelX/-8;
                }
                fltWorldX += fltVelX;
                fltWorldY += fltVelY;
                System.out.println(fltVelX);
                
            if(fltWorldX > 1280 || fltWorldX < 0 || fltWorldY > 720 || fltWorldY < 0){
                handler.removeObject(this);
            }
            if(fltVelX == 0 && fltVelY == 0){
                blnSucking = true;
            }
        }
        else if(blnSucking){
            Suck(FindNear());
            if(System.currentTimeMillis() - lngBirth > 10000){
                handler.removeObject(this);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)(fltWorldX - fltWidth/2),(int)(fltWorldY - fltHeight/2), null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2), (int)(fltWorldY - fltHeight/2), (int)fltWidth, (int)fltHeight);
    }

    private ArrayList<Integer> FindNear(){
        float fltDistX = 0;
        float fltDistY = 0;
        float fltTotalDist = 0;
        ArrayList<Integer> arylist = new ArrayList<Integer>();
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                fltDistX = fltWorldX - handler.getObject(i).getWorldX();
                fltDistY = fltWorldY - handler.getObject(i).getWorldY();
                fltTotalDist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
                if(fltTotalDist < 400){
                    arylist.add(i);
                }
                
            }
        }
        return arylist;
    }

    private void Suck(ArrayList<Integer> arylist){
        float fltTargetX;
        float fltTargetY;
        for(int i = 0; arylist.size() > i; i++){
            fltTargetX = handler.getObject(i).getWorldX();
            fltTargetY = handler.getObject(i).getWorldY();

            if(fltWorldX > fltTargetX){
                handler.getObject(i).setWorldX(fltTargetX + 5);
            }
            if(fltWorldX < fltTargetX){
                handler.getObject(i).setWorldX(fltTargetX - 5);
            }
            if(fltWorldY > fltTargetY){
                handler.getObject(i).setWorldY(fltTargetY + 5);
            }
            if(fltWorldY < fltTargetY){
                handler.getObject(i).setWorldY(fltTargetY - 5);
            }
        }


    }
    
    private void collisions() {
        if(getBounds().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            fltWorldY = (float)new Rectangle(0, 660, 1280, 10).getY();
        }
        else{
            blnFalling  = true;
        } 
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO){
                if(getBounds().intersects(handler.getObject(i).getBounds())){
                    //handler.getObject(i) -- player dmg
                    //handler.removeObject(this);
                    /*if(fltexplosionradius > 0){
                        handler.removeObject(this);
                        handler.addObject(new Explosion(fltWorldX - fltexplosionradius/2, fltWorldY - fltexplosionradius/2,fltexplosionradius*2,fltexplosionradius*2,ObjectId.BOOM,ssm,handler));
                        //arbitary value to make sure bomb doesnt explode multiple times
                        fltWorldX = 10000;
                    }*/       
                }
            }        
        }
    }
}
