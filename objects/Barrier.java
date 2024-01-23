package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public class Barrier extends GameObject {

    BufferedImage biTexture;

    public Barrier(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, BufferedImage biTexture, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.biTexture = biTexture;

        camObject = handler.getObject(Main.intSessionId - 1);
    }

    public void update() {
        
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        for(int intCount1 = 0; intCount1 < fltWidth/biTexture.getWidth(); intCount1++) {
            for(int intCount2 = 0; intCount2 < fltHeight/biTexture.getHeight(); intCount2++) {
                g2d.drawImage(biTexture, (int)(fltWorldX + biTexture.getWidth() * intCount1 - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY + biTexture.getHeight() * intCount2 - camObject.getWorldY() - camObject.getHeight()/2), null);
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
    }
}
