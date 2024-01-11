package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Knight extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVelY;
    private float fltDashVelX;
    private int intPosition;
    private int intJumpCount;
    private long[] lngtimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnBoost = false;
    private int intRecoilX = 0;
    private int intRecoilY = 0;
    private BufferedImage biBulletTexture;

    public Knight(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intPosition = intPosition;

        biBulletTexture = resLoader.loadImage("/res\\Shrapnel.png");
    }

    public void update(LinkedList<GameObject> objectList) {
        if(intPosition == Main.intSessionId - 1) {
            if(input.buttonSet.contains(InputHandler.InputButtons.W) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.W);
                fltVelY = -45;
                intJumpCount++;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
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

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngtimer[0] > 800 && blnBoost == false) {
                //Moving variables
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                
            
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                fltDashVelX = Math.round(fltDiffX * 50);
                fltDashVelY = Math.round(fltDiffY * 50);
                lngtimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
            }
            else if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngtimer[0] > 400 && blnBoost) {
                //Moving variables
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                
            
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                fltDashVelX = Math.round(fltDiffX * 55);
                fltDashVelY = Math.round(fltDiffY * 55);
                lngtimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngtimer[1] > 1600) {
                lngtimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                blnBoost = true;
                //The Ultimate abilty
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 200 && blnBoost == false) {
                lngtimer[2] = System.currentTimeMillis();
                if(fltWorldX + fltWidth/2 > input.fltMouseX){
                    handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis(), 50, 50, 135, id, ssm, handler));
                    if(intPosition == 1) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                }
                else{
                    handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis(), 50, 50, 270, id, ssm, handler));
                    if(intPosition == 1) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 100 && blnBoost) {
                lngtimer[2] = System.currentTimeMillis();
                if(fltWorldX + fltWidth/2 > input.fltMouseX){
                    handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis() + 300, 50, 50, 135, id, ssm, handler));
                    if(intPosition == 1) ssm.sendText("h>a>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -35 +"," + (50) + "," + (50) + "," + 135);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -35 +"," + (50) + "," + (50) + "," + 135);
                }
                else{
                    handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis() + 300, 50, 50, 270, id, ssm, handler));
                    if(intPosition == 1) ssm.sendText("h>a>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 35 +"," + (50) + "," + (50) + "," + 270);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 35 +"," + (50) + "," + (50) + "," + 270);
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                lngtimer[3] = System.currentTimeMillis();
                System.out.println("shot");
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                for(int intCount = 0; intCount < 4; intCount++) {
                    // Might slightly change how this works in the future
                    float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                    float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                    if(intPosition == 1) {
                        ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6 + "," + 4);
                        ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 - intRand3) + "," + 6 + "," + 6 + "," + 4);
                    } else {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6 + "," + 4);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand2) + "," + (fltDiffY * 20 - intRand4) + "," + 6 + "," + 6 + "," + 4);
                    }

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, ObjectId.BULLET, ssm, handler, biBulletTexture, false, 0));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, ObjectId.BULLET, ssm, handler, biBulletTexture, false, 0));
                }
            }

            if(System.currentTimeMillis() - lngtimer[1] > 8000 && blnBoost == true){
                blnBoost = false;
            }
        
            if(blnFalling) fltVelY += 3;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 30) fltVelY = 30;

            if(fltDashVelY > 0) fltDashVelY -= 1;
            else if(fltDashVelY < 0) fltDashVelY += 1;

            if(fltDashVelX > 0) fltDashVelX -= 1;
            else if(fltDashVelX < 0) fltDashVelX += 1;

            if(intRecoilX > 0) intRecoilX -= 1;
            else if(intRecoilX < 0) intRecoilX += 1;

            if(intRecoilY > 0) intRecoilY -= 1;
            else if(intRecoilY < 0) intRecoilY += 1;
            collisions();
            fltWorldX += fltVelX + fltDashVelX + intRecoilX;
            fltWorldY += fltVelY + intRecoilY + fltDashVelY;
            
            
            if(intPosition == 0) ssm.sendText("h>a>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + intPosition);
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 10000))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltWorldY = (float)new Rectangle(0, 660, 1280, 10).getY() - fltHeight;
        }
        else{
            blnFalling  = true;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.blue);
        g2d.fill(getBounds());
        g2d.setColor(Color.red);
        g2d.fill(getBounds2());
        g2d.setColor(Color.white);
        g2d.fillRect((int)fltWorldX, (int)fltWorldY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltWorldX + fltVelX + fltDashVelX), (int)fltWorldY + 2, (int)fltWidth, (int)fltHeight - 4);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)fltWorldX + 2, (int)(fltWorldY + fltVelY + fltDashVelY), (int)fltWidth - 4, (int)fltHeight);
    }
}
