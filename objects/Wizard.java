package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Wizard extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;

    private int intSessionId;
    private int intJumpCount;
    private long[] lngtimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnteleporting = false;
    private BufferedImage BiFireball = null;
    private BufferedImage BiEBall = null;
    


    public Wizard(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intSessionId) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intSessionId = intSessionId;
        try{
            BiFireball = ImageIO.read(new File("res/FireBall.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
        try{
            BiEBall = ImageIO.read(new File("res/ElectricBall.png"));
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
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, 20, 0, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, 20, -20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, 0, -20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, -20, -20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, -20, 0, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, -20, 20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, 0, 20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, 20, 20, 30, 30, ObjectId.HOMING_BULLET, ssm, handler, true, BiEBall, 0));
                //The Ultimate abilty
            }

            if(blnFalling) fltVelY += 5;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 30) fltVelY = 30;

            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            fltX += fltVelX + fltDashVel;
            fltY += fltVelY;

            collisions();
            
            if(intSessionId == 1) ssm.sendText("h>a>oPLAYER~" + fltX + "," + fltY + "," + intSessionId);
            else ssm.sendText("c" + intSessionId + ">h>oPLAYER~" + fltX + "," + fltY + "," + intSessionId);

            if(blnteleporting && input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)){
                fltX = input.fltMouseX;
                fltY = input.fltMouseY;
                
                blnFalling = true;
                blnteleporting = false;

                
            }

            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 500) {
                lngtimer[2] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                
                blnteleporting = false;
            
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                if(intSessionId == 1) ssm.sendText("h>a>aBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);
                else ssm.sendText("c" + intSessionId + ">h>aBULLET~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);
                handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 100, 100, ObjectId.BULLET, ssm, handler, false, BiFireball, 0));
                
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                lngtimer[3] = System.currentTimeMillis();
                float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
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
                
                handler.addObject(new WaveAttacks(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20 * -1, 10, 10, fltStartAngle, ObjectId.BULLET, ssm, handler));
            }
            
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
        if (blnteleporting){
            g2d.setColor(Color.gray);
            g2d.fillRect((int)(input.fltMouseX - fltWidth/2), (int)(input.fltMouseY - fltHeight/2), (int)fltWidth, (int)fltHeight);
        }
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
}
