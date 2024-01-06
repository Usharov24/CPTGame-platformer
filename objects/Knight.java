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

public class Knight extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;

    private int intSessionId;
    private int intJumpCount;
    private int intDirection = 1;
    private long[] lngtimer = {0,0,0,0};
    private BufferedImage BiShrapnel = null;
    private boolean blnFalling = true;
    private boolean blnBoost = false;
    private int intRecoilX = 0;
    private int intRecoilY = 0;

    public Knight(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intSessionId) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intSessionId = intSessionId;
        try{
            BiShrapnel = ImageIO.read(new File("res/Shrapnel.png"));
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
                blnBoost = true;
                //The Ultimate abilty
            }
            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 200) {
                lngtimer[2] = System.currentTimeMillis();
                
                if(fltX + fltWidth/2 > input.fltMouseX){
                    handler.addObject(new KnightSlashes(fltX + 25, fltY+15, -20, System.currentTimeMillis(), 50, 50, 135, id, ssm, handler));
                    if(intSessionId == 1) ssm.sendText("h>a>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    else ssm.sendText("c" + intSessionId + ">h>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                }
                else{
                    handler.addObject(new KnightSlashes(fltX, fltY+15 , 20, System.currentTimeMillis(), 50, 50, 270, id, ssm, handler));
                    if(intSessionId == 1) ssm.sendText("h>a>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                    else ssm.sendText("c" + intSessionId + ">h>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                lngtimer[3] = System.currentTimeMillis();
                System.out.println("shot");
                float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                for(int intCount = 0; intCount < 2; intCount++) {
                    // Might slightly change how this works in the future
                    float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                    float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                    if(intSessionId == 1) {
                        ssm.sendText("h>a>aSHRAPNEL~" + (fltX + fltWidth/2 - 3) + "," + (fltY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6);
                        ssm.sendText("h>a>aSHRAPNEL~" + (fltX + fltWidth/2 - 3) + "," + (fltY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 - intRand3) + "," + 6 + "," + 6);
                    } else {
                        ssm.sendText("c" + intSessionId + ">h>aSHRAPNEL~" + (fltX + fltWidth/2 - 3) + "," + (fltY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6);
                        ssm.sendText("c" + intSessionId + ">h>aSHRAPNEL~" + (fltX + fltWidth/2 - 3) + "," + (fltY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand2) + "," + (fltDiffY * 20 - intRand4) + "," + 6 + "," + 6);
                    }

                    handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, ObjectId.BULLET, ssm, handler, false, BiShrapnel, 0));
                    handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, ObjectId.BULLET, ssm, handler, false, BiShrapnel, 0));
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
            if(intSessionId == 1) ssm.sendText("h>a>oKNIGHT~" + fltX + "," + fltY + "," + intSessionId);
            else ssm.sendText("c" + intSessionId + ">h>oKNIGHT~" + fltX + "," + fltY + "," + intSessionId);

            
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
