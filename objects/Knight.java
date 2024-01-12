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

public class Knight extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();
    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDispX, fltDispY;
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

    public Knight(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
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
                    handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis(), 50, 50, 135, id, handler, ssm));
                    if(intPosition == 1) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                }
                else{
                    handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis(), 50, 50, 270, id, handler, ssm));
                    if(intPosition == 1) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 100 && blnBoost) {
                lngtimer[2] = System.currentTimeMillis();
                if(fltWorldX + fltWidth/2 > input.fltMouseX){
                    handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis() + 300, 50, 50, 135, id, handler, ssm));
                    if(intPosition == 1) ssm.sendText("h>a>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -35 +"," + (50) + "," + (50) + "," + 135);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -35 +"," + (50) + "," + (50) + "," + 135);
                }
                else{
                    handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis() + 300, 50, 50, 270, id, handler, ssm));
                    if(intPosition == 1) ssm.sendText("h>a>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 35 +"," + (50) + "," + (50) + "," + 270);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aBIGSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 35 +"," + (50) + "," + (50) + "," + 270);
                }
            }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                lngtimer[3] = System.currentTimeMillis();
                System.out.println("shot");
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
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

                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, ObjectId.BULLET, handler, ssm, biBulletTexture, false, 0));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, ObjectId.BULLET, handler, ssm, biBulletTexture, false, 0));
                }
            }

            if(System.currentTimeMillis() - lngtimer[1] > 8000 && blnBoost == true){
                blnBoost = false;
            }
            

            

            
            
            if(blnFalling) fltVelY += 3;

            if(fltVelX > 10) fltVelX = 10;
            else if(fltVelX < -10) fltVelX = -10;

            if(fltVelY > 35) fltVelY = 35;
            else if(fltVelY < -35) fltVelY = -35;

            if(fltDashVelY > 0) fltDashVelY -= 1;
            else if(fltDashVelY < 0) fltDashVelY += 1;

            if(fltDashVelX > 0) fltDashVelX -= 1;
            else if(fltDashVelX < 0) fltDashVelX += 1;

            if(intRecoilX > 0) intRecoilX -= 1;
            else if(intRecoilX < 0) intRecoilX += 1;

            if(intRecoilY > 0) intRecoilY -= 1;
            else if(intRecoilY < 0) intRecoilY += 1;

            fltWorldX += fltVelX + fltDashVelX + intRecoilX;
            fltWorldY += fltVelY + fltDashVelY + intRecoilY;
            
            
            if(intPosition == 0) ssm.sendText("h>a>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oKNIGHT~" + fltWorldX + "," + fltWorldY + "," + intPosition);
        } else {
            camObject = handler.getObject(Main.intSessionId - 1);
        }

        collisions();
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(-100 - (int)fltWorldX, 720 - (int)fltWorldY, 1280, 30))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;

            fltWorldY = (float)new Rectangle(-100, 720, 1280, 30).getY() - fltHeight/2;
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
