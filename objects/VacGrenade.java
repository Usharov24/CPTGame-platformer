package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;


public class VacGrenade extends GameObject {
    /**
     buffered image which is the texture
     */
    private BufferedImage[] biTextures;
/**
     int which is the start time
     */
    private int intStartTime;
    /**
     boolean which deems activity
     */
    private boolean blnActive = false;

    public VacGrenade(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage[] biTextures) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biTextures = biTextures;
        this.id = null;

        camObject = handler.getObject(Main.intSessionId - 1);
    }

    /**
     * Constructor for the Knight
     * @param fltWorldX - float value for the X position of the knight
     * @param fltWorldY - float value for the Y position of the knight
     * @param fltVelX - float value for the velocity X
     * @param fltVelY - float value for the velocity Y
     * @param id - ObjectId value, the id of the object
     * @param handler - handler value, which list is it a part of
     * @param ssm - the ssm of the players
     * @param fltWidth - the width
     * @param fltHeight - the height
     */
    
    public void update() {
        if(blnActive) {
            //active determines if the grenade has stopped moving
            for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
                GameObject object = handler.getObject(intCount);

                if(object.getId() == ObjectId.ENEMY) {
                    float fltDiffX = fltWorldX + fltWidth/2 - object.getWorldX() - object.getWidth()/2;
                    float fltDiffY = fltWorldY + fltHeight/2 - object.getWorldY() - object.getHeight()/2;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                    if(fltLength <= 700) {
                        fltDiffX /= fltLength;
                        fltDiffY /= fltLength;
                        Enemy enemy = (Enemy) object;
                        enemy.setVelX(enemy.getVelX() + fltDiffX * 12);
                        enemy.setVelY(enemy.getVelY() + fltDiffY * 12);
                    }
                }
                //sucks the nearby enemies towards the grenade
            }
            
            if(((int)System.currentTimeMillis() - intStartTime)/1000 == 10) {
                handler.removeObject(this);
            }
            //after a set amount of time, remove it
        }
        
        fltVelY += 3;

        if(fltVelX > 25) fltVelX = 25;
        else if(fltVelX < -25) fltVelX = -25;

        if(fltVelY > 25) fltVelY = 25;
        else if(fltVelY < -25) fltVelY = -25;
        //reponsible for the movement of the grenade
        collisions();

        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
    }
    /**
     * @return void checks for collisions
     */
    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
            
            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    if(intStartTime == 0) intStartTime = (int)System.currentTimeMillis();
                    blnActive = true;
                    fltVelY = 0;
                    fltVelX = 0;
                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
                //mkaes sure the grenade doesnt clip through anything
            }
        }
    }

    public void draw(Graphics g) {
        if(blnActive) g.drawImage(biTextures[1], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        else g.drawImage(biTextures[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        //draws a certain sprite showing whether the grenade is active or not
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltWorldX + fltVelX - camObject.getWorldX() - camObject.getWidth()/2;

        if(fltBoundsX > fltWorldX + fltWidth - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX + fltWidth - camObject.getWorldX() - camObject.getWidth()/2;
        else if(fltBoundsX < fltWorldX - fltWidth * 2f - camObject.getWorldX() - camObject.getWidth()/2) fltBoundsX = fltWorldX - fltWidth * 2f - camObject.getWorldX() - camObject.getWidth()/2;

        return new Rectangle((int)fltBoundsX, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2) + 4, (int)fltWidth, (int)fltHeight - 8);
        //the hit box of the grenade
    }
    /**
     * @return rectangle bound for y
     */
    public Rectangle getBounds2() {
        float fltBoundsY = fltWorldY + fltVelY - camObject.getWorldY() - camObject.getHeight()/2;

        if(fltBoundsY > fltWorldY + fltHeight - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY + fltHeight - camObject.getWorldY() - camObject.getHeight()/2;
        else if(fltBoundsY < fltWorldY - fltHeight * 2f - camObject.getWorldY() - camObject.getHeight()/2) fltBoundsY = fltWorldY - fltHeight * 2f - camObject.getWorldY() - camObject.getHeight()/2;

        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
        //the hit box of the grenade
    }
    //used to ensure the grenade doesn't pass through barriers
}
