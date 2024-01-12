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

public class Brute extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDispX, fltDispY;
    private int intPosition;
    private int intJumpCount;
    private float fltAngle = 270;
    private long[] lngTimer = {0,0,0,0};
    private boolean blnFalling = true;
    private boolean blnUlt = false;
    private boolean blnSlamming = false;
    private float fltUltSpeed = 0;
    private int intJumpStart = 0;
    private BufferedImage biVacTexture;

    public Brute(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.intPosition = intPosition;

        biVacTexture = resLoader.loadImage("/res\\VacGren.png");
    }

    public void update(LinkedList<GameObject> objectList) {
        if(intPosition == Main.intSessionId - 1) {
            if(blnUlt == false) {
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
                if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 3000) {
                    //Moving variables
                    lngTimer[0] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.SHIFT);
                    blnSlamming = true;
                    fltVelY = -35;
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 1600) {
                    lngTimer[1] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.F);
                    blnUlt = true;
                    fltWorldY -= 100;
                    //The Ultimate abilty
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 50) {
                    lngTimer[2] = System.currentTimeMillis();
                    if(fltWorldX + fltWidth/2 > input.fltMouseX){
                        handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20, System.currentTimeMillis() - 75, 50, 50, 135, id, handler, ssm));
                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    }
                    else{
                        handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20, System.currentTimeMillis() - 75, 50, 50, 270, id, handler, ssm));
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
                    handler.addObject(new VacGrenade(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX, fltDiffY, 40, 40, System.currentTimeMillis(), ObjectId.BULLET, handler, ssm, biVacTexture, 0));
                    if(intPosition == 0) ssm.sendText("h>a>aVAC~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aVAC~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                }

                if(blnFalling) fltVelY += 3;

                if(fltVelX > 10) fltVelX = 10;
                else if(fltVelX < -10) fltVelX = -10;

                if(fltVelY > 30) fltVelY = 30;

                if(blnSlamming == false){
                    fltWorldX += fltVelX;
                }
                else{
                    fltWorldX += fltVelX*2 ;
                }
                
                fltWorldY += fltVelY;

                if(intPosition == 0) ssm.sendText("h>a>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
                else ssm.sendText("c" + (intPosition + 1) + ">h>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            } else {
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
                fltUltSpeed += 0.3;
                fltVelY += (float)(fltUltSpeed*Math.sin(Math.toRadians(fltAngle)));
                fltVelX += (float)(fltUltSpeed*Math.cos(Math.toRadians(fltAngle)));

                fltWorldY += fltVelY;
                fltWorldX += fltVelX;

                if(intPosition == 0) ssm.sendText("h>a>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
                else ssm.sendText("c" + (intPosition + 1) + ">h>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            }
        } else {
            camObject = handler.getObject(Main.intSessionId - 1);
        }

        collisions();
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
    
            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                        fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;

                    if(blnSlamming){
                        blnSlamming = false;
                        handler.addObject(new Explosion(fltWorldX, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, handler, ssm));
                        handler.addObject(new Explosion(fltWorldX+ fltWidth, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, handler, ssm));
                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX) + "," + (fltWorldY + fltHeight) + "," + (300) + "," + (300));
                    }
                    if(blnUlt){
                        blnUlt = false;
                        handler.addObject(new Explosion(fltWorldX, fltWorldY, 300, 300, ObjectId.BOOM, handler, ssm));
                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX) + "," + (fltWorldY) + "," + (300) + "," + (300));
                         else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX) + "," + (fltWorldY) + "," + (300) + "," + (300));
                    }
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
    }

    public Rectangle getBounds() {
        float fltBoundsX = fltDispX + fltVelX - fltWidth/2;

        if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2;
        else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

        return new Rectangle((int)fltBoundsX, (int)(fltDispX - fltHeight/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltDispY + fltVelY - fltHeight/2;

        if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
        else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

        return new Rectangle((int)(fltDispX - fltWidth/2) + 4, (int)(fltDispY + fltVelY - fltHeight/2), (int)fltWidth - 8, (int)fltHeight);
    }
}
