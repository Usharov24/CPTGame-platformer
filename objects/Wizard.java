package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;

import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Wizard extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDispX, fltDispY;
    private float fltDashVel;

    private int intPosition;
    private int intJumpCount;
    private long[] lngtimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnteleporting = false;
    private BufferedImage[] biBulletTextures;
    
    public Wizard(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.handler = handler;
        this.input = input;
        this.intPosition = intPosition;

        biBulletTextures = resLoader.loadImages("/res\\FireBall.png", "/res\\ElectricBall.png");
    }
    
    public void update() {
        if(intPosition == Main.intSessionId - 1) {
            if(input.buttonSet.contains(InputHandler.InputButtons.W) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.W);
                fltVelY = -45;
                blnFalling = true;
                intJumpCount++;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
                blnFalling = true;
                intJumpCount++;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                fltVelX -= fltAcc;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                fltVelX += fltAcc;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            } else {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngtimer[0] > 5000) {
                //Moving variables
                lngtimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                blnteleporting = true;
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngtimer[1] > 8000) {
                lngtimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 0 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 0 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + -20 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 0 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 0 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 20 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + 20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + 20 + "," + 100 + "," + 100 + "," + 3);
                if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 20 + "," + 100 + "," + 100 + "," + 3);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 20 + "," + 100 + "," + 100 + "," + 3);

                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, 0));
            }

            if(blnFalling) fltVelY += 3;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 35) fltVelY = 35;
            else if(fltVelY < -35) fltVelY = -35;

            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            fltWorldX += fltVelX + fltDashVel;
            fltWorldY += fltVelY;

            collisions();
            
            if(intPosition == 0) ssm.sendText("h>a>oWIZARD~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oWIZARD~" + fltWorldX + "," + fltWorldY + "," + intPosition);

            if(blnteleporting && input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)){
                fltWorldX = input.fltMouseX;
                fltWorldY = input.fltMouseY;
                
                blnFalling = true;
                blnteleporting = false;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 500) {
                lngtimer[2] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                
                blnteleporting = false;
            
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                if(intPosition == 0) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 100 + "," + 100 + "," + 2);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 100 + "," + 100 + "," + 2);
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 100, 100, ObjectId.BULLET, handler, ssm, biBulletTextures[0], false, 0));
                
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                lngtimer[3] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                float fltStartAngle = 0;
                //CAST graph to determine angle
                //A
                fltDiffY*= -1;
                if(fltDiffX > 0 && fltDiffY > 0){
                    fltStartAngle = (float) Math.atan(fltDiffY/fltDiffX);
                    
                }
                //S
                else if(fltDiffX < 0 && fltDiffY > 0){
                    fltStartAngle = (float) ((Math.atan(fltDiffY/fltDiffX)) + Math.PI); 
                }
                //T
                else if(fltDiffX < 0 && fltDiffY < 0){
                    fltStartAngle = (float) ((fltDiffY/fltDiffX) + Math.PI); 
                }
                //C
                else if(fltDiffX > 0 && fltDiffY < 0){
                    fltStartAngle = (float) Math.atan(fltDiffY/fltDiffX); 
                }
                
                if(intPosition == 0) ssm.sendText("h>a>aWAVE~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * -20) + "," + 10 + "," + 10 + "," + fltStartAngle);
                else ssm.sendText("c" + (intPosition + 1) + ">h>aWAVE~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * -20) + "," + 10 + "," + 10 + "," + fltStartAngle);
                handler.addObject(new WaveAttacks(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20 * -1, 10, 10, fltStartAngle, ObjectId.BULLET, handler, ssm));
            }   
        } else {
            camObject = handler.getObject(Main.intSessionId - 1);
        }
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);

            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    blnFalling = false;
                    intJumpCount = 0;

                    fltWorldY = object.getWorldY() - fltHeight;
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();
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
        g2d.setColor(Color.white);

        if(intPosition == Main.intSessionId - 1) {
            g2d.fillRect((int)(fltDispX - fltWidth/2), (int)(fltDispY- fltHeight/2), (int)fltWidth, (int)fltHeight);
        } else {
            g2d.fillRect((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        }

        if (blnteleporting){
            g2d.setColor(Color.gray);
            g2d.fillRect((int)(input.fltMouseX - fltWidth/2 - 640), (int)(input.fltMouseY - fltHeight/2 - 360), (int)fltWidth, (int)fltHeight);
        }
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltDispX + fltVelX - fltWidth/2;

        if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2;
        else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

        return new Rectangle((int)fltBoundsX, (int)(fltDispY - fltHeight/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltDispY + fltVelY - fltHeight/2;

        if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
        else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

        return new Rectangle((int)(fltDispX - fltWidth/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
    }
}
