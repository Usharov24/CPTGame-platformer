package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import framework.Main;
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

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update(LinkedList<GameObject> objectList) {
        if(blnSucking == false){ 
            if(blnFalling) fltVelY += 2;
            else {
                if(fltVelX > 0)fltVelX -= fltpastVelX/8;
                else if(fltVelX < 0)fltVelX += fltpastVelX/-8;
            }

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            System.out.println(fltVelX);
                
            if(fltVelX == 0 && fltVelY == 0){
                blnSucking = true;
            }
        } else if(blnSucking) {
            Suck(FindNear());
            if(System.currentTimeMillis() - lngBirth > 10000){
                handler.removeObject(this);
            }
        }

        collisions();
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object;
            
            if((object = handler.getObject(intCount)).getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds())) {
                    blnFalling = false;
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() - fltHeight/2;
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2),(int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
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
}
