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

public class Brute extends GameObject {
    private ObjectHandler handler;
    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private int intPosition;
    private int intJumpCount;
    private float fltAngle = 270;
    private long[] lngTimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnRocket = false;
    private boolean blnSlamming = false;
    private float fltRocketSpeed = 0;
    private int intJumpStart = 0;
    private BufferedImage biVacTexture;

    public Brute(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intPosition = intPosition;

        biVacTexture = resLoader.loadImage("/res\\VacGren.png");
    }

    public void update(LinkedList<GameObject> objectList) {
        if(intPosition == Main.intSessionId - 1) {
            if(blnRocket == false){
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
                if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 3000) {
                    //Moving variables
                    lngTimer[0] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.SHIFT);
                    blnSlamming = true;
                    fltVelY = -65;
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 1600) {
                    lngTimer[1] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.F);
                    blnRocket = true;
                    fltWorldY -= 100;
                    //The Ultimate abilty
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 50) {
                    lngTimer[2] = System.currentTimeMillis();
                    if(fltWorldX + fltWidth/2 > input.fltMouseX){
                        handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis() - 75, 50, 50, 135, id, ssm, handler));
                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    }
                    else{
                        handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis() - 75, 50, 50, 270, id, ssm, handler));
                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                    }
                
                }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 3000) {
                    lngTimer[3] = System.currentTimeMillis();
                    
                    float fltDiffX = input.fltMouseX - (fltWorldX + fltWidth/2);
                    float fltDiffY = input.fltMouseY - (fltWorldY + fltHeight/2);
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    fltDiffX = Math.round(fltDiffX*40);
                    fltDiffY = Math.round(fltDiffY*40);
                    handler.addObject(new VacGrenade(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX, fltDiffY, 40, 40, System.currentTimeMillis(), ObjectId.BULLET, ssm, handler, biVacTexture, 0));
                    if(intPosition == 0) ssm.sendText("h>a>aVAC~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aVAC~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                }

                if(blnFalling) fltVelY += 5;

                if(fltVelX > 10) fltVelX = 10;
                else if(fltVelX < -10) fltVelX = -10;

                if(fltVelY > 30) fltVelY = 30;
                
                collisions();

                if(blnSlamming == false){
                    fltWorldX += fltVelX;
                }
                else{
                    fltWorldX += fltVelX*2 ;
                }
                
                fltWorldY += fltVelY;
                
                if(intPosition == 0) ssm.sendText("h>a>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
                else ssm.sendText("c" + (intPosition + 1) + ">h>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            }
            else{
                if(intJumpStart == 0){
                    fltWorldY -= 50;
                    intJumpStart = 1;
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                    fltAngle -= 8;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                    fltAngle += 8;
                }
                blnSlamming = false;
                fltRocketSpeed += 0.3;
                fltVelY =+ (float)(fltRocketSpeed*Math.sin(Math.toRadians(fltAngle)));
                fltVelX =+ (float)(fltRocketSpeed*Math.cos(Math.toRadians(fltAngle)));
                collisions();

                fltWorldY += fltVelY;
                fltWorldX += fltVelX;
            }
        }
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 300000))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;
            if(blnSlamming){
                blnSlamming = false;
                handler.addObject(new Explosion(fltWorldX, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, ssm, handler));
                handler.addObject(new Explosion(fltWorldX+ fltWidth, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, ssm, handler));
                if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
            }
            if(blnRocket){
                
                blnRocket = false;
                handler.addObject(new Explosion(fltWorldX, fltWorldY, 300, 300, ObjectId.BOOM, ssm, handler));
                if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX) + "," + (fltWorldY ) + "," + (300) + "," + (300));
                else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX) + "," + (fltWorldY) + "," + (300) + "," + (300));
            }
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
        return new Rectangle((int)(fltWorldX + fltVelX), (int)fltWorldY + 2, (int)fltWidth, (int)fltHeight - 4);
    }

    public Rectangle getBounds2() {
        return new Rectangle((int)fltWorldX + 2, (int)(fltWorldY + fltVelY), (int)fltWidth - 4, (int)fltHeight);
    }
}
