package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;


public class VacGrenade extends GameObject {

    private BufferedImage[] biTextures;
    private int intStartTime;
    private boolean blnActive = false;

    public VacGrenade(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, BufferedImage[] biTextures) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.biTextures = biTextures;

        camObject = handler.getObject(Main.intSessionId - 1);
    }
    
    public void update() {
        if(blnActive) {
            for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
                GameObject object = handler.getObject(intCount);

                if(object.getId() == ObjectId.ENEMY) {
                    float fltDiffX = fltWorldX + fltWidth/2 - object.getWorldX() - object.getWidth()/2;
                    float fltDiffY = fltWorldY + fltHeight/2 - object.getWorldY() - object.getHeight()/2;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                    if(fltLength <= 400) {
                        fltDiffX /= fltLength;
                        fltDiffY /= fltLength;

                        object.setWorldX(object.getWorldX() + fltDiffX * 12);
                        object.setWorldY(object.getWorldY() + fltDiffY * 12);
                    }
                }
            }
            
            if(((int)System.currentTimeMillis() - intStartTime)/1000 == 10) {
                handler.removeObject(this);
            }
        }

        fltVelY += 3;

        if(fltVelX > 30) fltVelX = 30;
        else if(fltVelX < -30) fltVelX = -30;

        if(fltVelY > 30) fltVelY = 30;
        else if(fltVelY < -30) fltVelY = -30;

        collisions();

        fltWorldX += fltVelX;
        fltWorldY += fltVelY;
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
                    if(intStartTime == 0) intStartTime = (int)System.currentTimeMillis();
                    blnActive = true;
                    fltVelY = 0;
                    fltVelX = 0;
                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
                }
            }
        }
    }

    public void draw(Graphics g) {
        if(blnActive) g.drawImage(biTextures[1], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
        else g.drawImage(biTextures[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
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
}
