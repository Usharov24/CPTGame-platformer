package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class VacGrenade extends GameObject {

    private BufferedImage biImg;
    private int intLifetime = 600;
    private boolean blnActive = false;long previous = 0;

    public VacGrenade(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage biImg) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biImg = biImg;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        long current = System.currentTimeMillis() - previous;
        System.out.println(current);
        previous = current;
        if(blnActive) {
            Suck(FindNear());
            if((intLifetime -= 1) == 0) {
                handler.removeObject(this);
            }
        }

        fltVelY += 3;

        if(fltVelX > 35) fltVelX = 35;
        else if(fltVelX < -35) fltVelX = -35;

        if(fltVelY > 35) fltVelY = 35;
        else if(fltVelY < -35) fltVelY = -35;

        fltWorldX += fltVelX;
        fltWorldY += fltVelY;

        collisions();
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object;
            
            if((object = handler.getObject(intCount)).getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    blnActive = true;
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(biImg, (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltWorldX + fltVelX - camObject.getWorldX() - camObject.getWidth()/2;

        if(fltBoundsX > fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX + fltWidth/2 - camObject.getWorldX() - camObject.getWidth()/2;
        else if(fltBoundsX < fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX - fltWidth * 1.5f - camObject.getWorldX() - camObject.getWidth()/2;

        return new Rectangle((int)fltBoundsX, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltWorldY + fltVelY - camObject.getWorldY() - camObject.getHeight()/2;

        if(fltBoundsY > fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY + fltHeight/2 - camObject.getWorldY() - camObject.getHeight()/2;
        else if(fltBoundsY < fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY - fltHeight * 1.5f - camObject.getWorldY() - camObject.getHeight()/2;

        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
    }

    private ArrayList<Integer> FindNear() {
        float fltDistX = 0;
        float fltDistY = 0;
        float fltTotalDist = 0;
        ArrayList<Integer> arylist = new ArrayList<Integer>();
        for(int i = 0; i < handler.objectList.size(); i++) {
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

    private void Suck(ArrayList<Integer> arylist) {
        float fltTargetX;
        float fltTargetY;
        for(int i = 0; arylist.size() > i; i++){
            fltTargetX = handler.getObject(i).getWorldX();
            fltTargetY = handler.getObject(i).getWorldY();

            if(fltWorldX > fltTargetX) {
                handler.getObject(i).setWorldX(fltTargetX + 5);
            }
            if(fltWorldX < fltTargetX) {
                handler.getObject(i).setWorldX(fltTargetX - 5);
            }
            if(fltWorldY > fltTargetY) {
                handler.getObject(i).setWorldY(fltTargetY + 5);
            }
            if(fltWorldY < fltTargetY) {
                handler.getObject(i).setWorldY(fltTargetY - 5);
            }
        }


    }
}
