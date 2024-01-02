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
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Sniper extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;

    private int intSessionId;
    private int intJumpCount;
    private int intDirection = 1;
    private long[] lngtimer = {0,0,0,0};
    private BufferedImage BiBullet = null;
    private BufferedImage BiBomb = null;
    private boolean blnFalling = true;
    private boolean blnBazooka = false;
    private boolean blnRapidFire = false;
    private int intRecoilX = 0;
    private int intRecoilY = 0;

    public Sniper(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intSessionId) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intSessionId = intSessionId;
        try{
            BiBullet = ImageIO.read(new File("res/SniperBullet.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
        try{
            BiBomb = ImageIO.read(new File("res/Rocket.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
    }

    public void update(LinkedList<GameObject> objectList) {
        if(intSessionId == Main.intSessionId) {
            if(input.buttonSet.contains(InputHandler.InputButtons.W) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.W);
                fltVelY -= 50;
                blnFalling = true;
                intJumpCount++;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intJumpCount < 2) {
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY -= 50;
                blnFalling = true;
                intJumpCount++;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                fltVelX -= fltAcc;
                intDirection = -1;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                fltVelX += fltAcc;
                intDirection = 1;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            } else {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngtimer[0] > 1000) {
                //Moving variables
                lngtimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                if(intDirection > 0){
                    fltDashVel = 50;
                } else if(intDirection < 0){
                    fltDashVel = -50;
                }
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngtimer[1] > 8000) {
                lngtimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                blnBazooka = true;
                //The Ultimate abilty
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 1000) {
                lngtimer[2] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                
                if(blnBazooka == false){
                    handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, ssm, handler, false, BiBullet, 0));
                    if(intSessionId == 1) ssm.sendText("h>a>aSBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                    else ssm.sendText("c" + intSessionId + ">h>aSBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                    intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-15);
                    intRecoilY = intRecoilY +(int)(fltVelY + fltDiffY*-15);
                }

                if(blnBazooka == true){
                    handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, ssm, handler, false, BiBomb, 100));
                    if(intSessionId == 1) ssm.sendText("h>a>aBOMB~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                    else ssm.sendText("c" + intSessionId + ">h>aBOMB~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                    intRecoilX = intRecoilX +(int)(fltVelX + fltDiffX*-24);
                    intRecoilY = intRecoilY +(int)(fltVelY + fltDiffY*-24);
                }
                
                
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 7000) {
                lngtimer[3] = System.currentTimeMillis();
                blnRapidFire = true;

            }

            if(System.currentTimeMillis() - lngtimer[1] > 10000 && blnBazooka == true){
                blnBazooka = false;
            }

            if(blnRapidFire == true){
                if(System.currentTimeMillis() - lngtimer[3] < 500 && (System.currentTimeMillis() - lngtimer[3]) % 2 == 0){
                    float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                    float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    if(blnBazooka == false){
                        handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, ssm, handler, false, BiBullet, 0));
                        if(intSessionId == 1) ssm.sendText("h>a>aSBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                        else ssm.sendText("c" + intSessionId + ">h>aSBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                        intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-15);
                        intRecoilY = intRecoilY + (int)(fltVelY + fltDiffY*-15);
                    }

                    if(blnBazooka == true){
                        handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, ssm, handler, false, BiBomb, 100));
                        if(intSessionId == 1) ssm.sendText("h>a>aBOMB~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                        else ssm.sendText("c" + intSessionId + ">h>aBOMB~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10);
                        intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-24);
                        intRecoilY = intRecoilY + (int)(fltVelY + fltDiffY*-24);
                    }
                }
            }
            
            if(blnFalling) fltVelY += 5;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 30) fltVelY = 30;

            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            if(intRecoilX > 0) intRecoilX -= 1;
            else if(intRecoilX < 0) intRecoilX += 1;

            if(intRecoilY > 0) intRecoilY -= 1;
            else if(intRecoilY < 0) intRecoilY += 1;

            fltX += fltVelX + fltDashVel + intRecoilX;
            fltY += fltVelY + intRecoilY;
            
            collisions();
            if(intSessionId == 1) ssm.sendText("h>a>oSNIPER~" + fltX + "," + fltY + "," + intSessionId);
            else ssm.sendText("c" + intSessionId + ">h>oSNIPER~" + fltX + "," + fltY + "," + intSessionId);

            
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltY = (float)new Rectangle(0, 660, 1280, 10).getY() - fltHeight;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.blue);
        g2d.fill(getBounds());
        g2d.setColor(Color.red);
        g2d.fill(getBounds2());
        g2d.setColor(Color.white);
        g2d.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltX + fltVelX), (int)fltY + 2, (int)fltWidth, (int)fltHeight - 4);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)fltX + 2, (int)(fltY + fltVelY), (int)fltWidth - 4, (int)fltHeight);
    }

    // Will likely remove later
    public int getSessionId() {
        return intSessionId;
    }

    public void drawTeleport(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.gray);
        g2d.fillRect((int)input.fltMouseX, (int)input.fltMouseY, (int)fltWidth, (int)fltHeight);
        
    }
}
