package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    
    public void update() {
        if(!blnSucking){ 
            if(blnFalling) fltVelY += 3;
            else {
                if(fltVelX > 0) fltVelX -= fltpastVelX/8;
                else if(fltVelX < 0) fltVelX += fltpastVelX/-8;
            }

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
                
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
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.blue);
        g2d.fill(getBounds());
        g2d.setColor(Color.red);
        g2d.fill(getBounds2());

        g.drawImage(biImg, (int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltWorldX - fltWidth/2 + fltVelX - camObject.getWorldX() - camObject.getWidth()/2;

        if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2; 
        else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

        return new Rectangle((int)fltBoundsX, (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)(fltWorldX - fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }

    private ArrayList<Integer> FindNear(){
        float fltDistX = 0;
        float fltDistY = 0;
        float fltTotalDist = 0;
        ArrayList<Integer> arylist = new ArrayList<Integer>();
        for(int i = 0; i < handler.objectList.size(); i++){
            if(handler.getObject(i).getId() == ObjectId.ENEMY_APPLE || handler.getObject(i).getId() == ObjectId.ENEMY_MANGO) {
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
