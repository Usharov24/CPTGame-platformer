package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Player extends GameObject {

    private InputHandler input;

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDashVel;

    private int intSessionId;
    private int intJumpCount;
    private int intDirection = 1;

    private boolean blnFalling = true;
    private boolean blnteleporting = false;

    public Player(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intSessionId) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.intSessionId = intSessionId;
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

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT)) {
                input.buttonSet.remove(InputButtons.SHIFT);
                blnteleporting = true;
                if(intDirection > 0){
                    fltDashVel = 50;
                } else if(intDirection < 0){
                    fltDashVel = -50;
                }
            }

            if(blnFalling) fltVelY += 5;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 30) fltVelY = 30;

            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            fltWorldX += fltVelX + fltDashVel;
            fltWorldY += fltVelY;

            collisions();
            if(blnteleporting == true){
                drawTeleport(null);
            }
            if(intSessionId == 1) ssm.sendText("h>a>oPLAYER~" + fltWorldX + "," + fltWorldY + "," + intSessionId);
            else ssm.sendText("c" + intSessionId + ">h>oPLAYER~" + fltWorldX + "," + fltWorldY + "," + intSessionId);

            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)) {
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                
                blnteleporting = false;
            
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                if(intSessionId == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);
                else ssm.sendText("c" + intSessionId + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler, ssm, null, false, 0));
                
            } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON2)) {
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                if(intSessionId == 1) ssm.sendText("h>a>aHOMING_BULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);
                else ssm.sendText("c" + intSessionId + ">h>aHOMING_BULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * 20) + "," + 10 + "," + 10);

                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler, ssm, null, true, 0));
            } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3)) {
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                for(int intCount = 0; intCount < 2; intCount++) {
                    // Might slightly change how this works in the future
                    float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                    float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                    if(intSessionId == 1) {
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6);
                        ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 - intRand3) + "," + 6 + "," + 6);
                    } else {
                        ssm.sendText("c" + intSessionId + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6);
                        ssm.sendText("c" + intSessionId + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand2) + "," + (fltDiffY * 20 - intRand4) + "," + 6 + "," + 6);
                    }

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, ObjectId.BULLET, handler, ssm, null, false, 0));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, ObjectId.BULLET, handler, ssm, null, false, 0));
                }
            }
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltWorldY = (float)new Rectangle(0, 660, 1280, 10).getY() - fltHeight;
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
        return new Rectangle((int)(fltWorldX + fltVelX), (int)fltWorldY + 2, (int)fltWidth, (int)fltHeight - 4);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)fltWorldX + 2, (int)(fltWorldY + fltVelY), (int)fltWidth - 4, (int)fltHeight);
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
