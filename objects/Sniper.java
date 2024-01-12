package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.ResourceLoader;
import framework.SuperSocketMaster;
import framework.InputHandler.InputButtons;

public class Sniper extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDispX, fltDispY;
    private float fltDashVel;

    private int intPosition;
    private int intJumpCount;
    private int intDirection = 1;
    private long[] lngtimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnBazooka = false;
    private boolean blnRapidFire = false;
    private int intRecoilX = 0;
    private int intRecoilY = 0;
    private BufferedImage[] biBulletTextures;

    public Sniper(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.intPosition = intPosition;

        biBulletTextures = resLoader.loadImages("/res\\SniperBullet.png", "/res\\Rocket.png");
    }

    public void update(LinkedList<GameObject> objectList) {System.out.println(fltWorldX + " " + fltWorldY);
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
                float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                fltDiffX /= fltLength;
                fltDiffY /= fltLength;

                
                if(blnBazooka == false){
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, handler, ssm, biBulletTextures[0], false, 0));
                    if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 0);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 0);
                    intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-15);
                    intRecoilY = intRecoilY +(int)(fltVelY + fltDiffY*-15);
                }

                if(blnBazooka == true){
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, handler, ssm, biBulletTextures[1], false, 100));
                    if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 1);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 1);
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
                    float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                    float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    if(blnBazooka == false){
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, handler, ssm, biBulletTextures[0], false, 0));
                        if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 0);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 0);
                        intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-15);
                        intRecoilY = intRecoilY + (int)(fltVelY + fltDiffY*-15);
                    }

                    if(blnBazooka == true){
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 60, fltDiffY * 60, 10, 10, ObjectId.BULLET, handler, ssm, biBulletTextures[1], false, 100));
                        if(intPosition == 1) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 1);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 60) + "," + (fltDiffY * 60) + "," + 10 + "," + 10 + "," + 1);
                        intRecoilX = intRecoilX + (int)(fltVelX + fltDiffX*-24);
                        intRecoilY = intRecoilY + (int)(fltVelY + fltDiffY*-24);
                    }
                }
            }
            
            if(blnFalling) fltVelY += 3;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;


            if(fltDashVel > 0) fltDashVel -= 5;
            else if(fltDashVel < 0) fltDashVel += 5;

            if(intRecoilX > 0) intRecoilX -= 1;
            else if(intRecoilX < 0) intRecoilX += 1;

            if(intRecoilY > 0) intRecoilY -= 1;
            else if(intRecoilY < 0) intRecoilY += 1;

            fltWorldX += fltVelX + fltDashVel + intRecoilX;
            fltWorldY += fltVelY + intRecoilY;
            
            collisions();

            if(intPosition == 0) ssm.sendText("h>a>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oSNIPER~" + fltWorldX + "," + fltWorldY + "," + intPosition); 
        } else {
            camObject = handler.getObject(Main.intSessionId - 1);
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(-100 - (int)fltWorldX, 720 - (int)fltWorldY, 1280, 30))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltWorldY = (float)new Rectangle(-100 - (int)fltWorldX, 720 - (int)fltWorldY, 1280, 30).getY() - fltHeight/2;
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
        // TEMP
        if(intPosition == Main.intSessionId - 1) {
            g2d.setColor(Color.red);
            g2d.draw(new Rectangle((int)-100 - (int)fltWorldX, 720 - (int)fltWorldY, 1280, 30));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(fltDispX + fltVelX - fltWidth/2), (int)(fltDispX - fltHeight/2) + 4, (int)(fltWidth + fltVelX/2), (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)(fltDispX - fltWidth/2) + 4, (int)(fltDispY + fltVelY - fltHeight/2), (int)fltWidth - 8, (int)(fltHeight + fltVelY/2));
    }

    public void drawTeleport(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.gray);
        g2d.fillRect((int)input.fltMouseX, (int)input.fltMouseY, (int)fltWidth, (int)fltHeight);
    }
}
