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

public class Brute extends GameObject {

    private ObjectHandler handler;
    private InputHandler input;
    private float fltAcc = 1f, fltDec = 0.5f;
    private int intSessionId;
    private int intJumpCount;
    private float fltAngle = 270;
    private long[] lngtimer = {0,0,0,0};
    private BufferedImage BiVacGrenade = null;
    private boolean blnFalling = true;
    private boolean blnRocket = false;
    private boolean blnSlamming = false;
    private float fltRocketSpeed = 0;
    private int intJumpStart = 0;

    public Brute(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm, ObjectHandler handler, InputHandler input, int intSessionId) {
        super(fltX, fltY, fltWidth, fltHeight, id, ssm);
        this.handler = handler;
        this.input = input;
        this.intSessionId = intSessionId;
        try{
            BiVacGrenade = ImageIO.read(new File("res/VacGren.png"));
        }catch(IOException e){
            System.out.println("no image");
        }
    }

    public void update(LinkedList<GameObject> objectList) {
        if(intSessionId == Main.intSessionId) {
            if(blnRocket == false){
                if(input.buttonSet.contains(InputHandler.InputButtons.W) && intJumpCount < 2) {
                    input.buttonSet.remove(InputButtons.W);
                    fltVelY -= 50;
                    intJumpCount++;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intJumpCount < 2) {
                    input.buttonSet.remove(InputButtons.SPACE);
                    fltVelY -= 50;
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

                if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngtimer[0] > 3000) {
                    //Moving variables
                    lngtimer[0] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.SHIFT);
                    blnSlamming = true;
                    System.out.println(blnSlamming);
                    fltVelY = -65;
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngtimer[1] > 1600) {
                    lngtimer[1] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.F);
                    blnRocket = true;
                    //The Ultimate abilty
                }
                if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngtimer[2] > 50) {
                    lngtimer[2] = System.currentTimeMillis();
                    if(fltX + fltWidth/2 > input.fltMouseX){
                        handler.addObject(new KnightSlashes(fltX + 25, fltY+15, -20, System.currentTimeMillis() - 75, 50, 50, 135, id, ssm, handler));
                        if(intSessionId == 1) ssm.sendText("h>a>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                        else ssm.sendText("c" + intSessionId + ">h>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + -20 +"," + (50) + "," + (50) + "," + 135);
                    }
                    else{
                        handler.addObject(new KnightSlashes(fltX, fltY+15 , 20, System.currentTimeMillis() - 75, 50, 50, 270, id, ssm, handler));
                        if(intSessionId == 1) ssm.sendText("h>a>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                        else ssm.sendText("c" + intSessionId + ">h>aSLASH~" + (fltX + 25) + "," + (fltY + 15) + "," + 20 +"," + (50) + "," + (50) + "," + 270);
                    }
                
                }else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngtimer[3] > 3000) {
                    lngtimer[3] = System.currentTimeMillis();
                    
                    float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
                    float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;
                    fltDiffX = Math.round(fltDiffX*40);
                    fltDiffY = Math.round(fltDiffY*40);
                    handler.addObject(new VacGrenade(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX, fltDiffY, 40, 40, System.currentTimeMillis(), ObjectId.BULLET, ssm, handler, BiVacGrenade, 0));
                    if(intSessionId == 1) ssm.sendText("h>a>aVAC~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                    else ssm.sendText("c" + intSessionId + ">h>aVAC~" + (fltX + fltWidth/2 - 5) + "," + (fltY + fltHeight/2 - 5) + "," + (fltDiffX) + "," + (fltDiffY) + "," + 40 + "," + 40);
                }

                
                

                

                
                
                if(blnFalling) fltVelY += 5;

                if(fltVelX > 10) fltVelX = 10;
                else if(fltVelX < -10) fltVelX = -10;

                if(fltVelY > 30) fltVelY = 30;

                
                collisions();
                if(blnSlamming == false){
                    fltX += fltVelX;
                }
                else{
                    fltX += fltVelX*2 ;
                }
                
                fltY += fltVelY;
                
                
                if(intSessionId == 1) ssm.sendText("h>a>oBRUTE~" + fltX + "," + fltY + "," + intSessionId);
                else ssm.sendText("c" + intSessionId + ">h>oBRUTE~" + fltX + "," + fltY + "," + intSessionId);

                
            }
            else{
                if(intJumpStart == 0){
                    fltY -= 50;
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

                System.out.println("Y" + fltVelY);
                System.out.println("X" + fltVelX);
                System.out.println("Angle" + fltAngle);
                collisions();
                fltY += fltVelY;
                fltX += fltVelX;
                //on hit
                //fltVelY = 0;
                //fltVely = 0;
                //new Explosion size*fltRocketSpeed and dmg*speed
            }
        }
        
    }

    private void collisions() {
        if(getBounds2().intersects(new Rectangle(0, 660, 1280, 10))) {
            blnFalling = false;
            fltVelY = 0;
            intJumpCount = 0;
            if(blnSlamming){
                blnSlamming = false;
                handler.addObject(new Explosion(fltX, fltY + fltHeight, 300, 300, ObjectId.BOOM, ssm, handler));
                handler.addObject(new Explosion(fltX+ fltWidth, fltY + fltHeight, 300, 300, ObjectId.BOOM, ssm, handler));
                if(intSessionId == 1) ssm.sendText("h>a>aBOOM~" + (fltX + fltWidth) + "," + (fltY + fltHeight) + "," + (300) + "," + (300));
                else ssm.sendText("c" + intSessionId + ">h>aBOOM~" + (fltX + fltWidth) + "," + (fltY + fltHeight) + "," + (300) + "," + (300));
                if(intSessionId == 1) ssm.sendText("h>a>aBOOM~" + (fltX) + "," + (fltY + fltHeight) + "," + (300) + "," + (300));
                else ssm.sendText("c" + intSessionId + ">h>aBOOM~" + (fltX) + "," + (fltY + fltHeight) + "," + (300) + "," + (300));
            }
            if(blnRocket){
                intJumpStart = 0;
                blnRocket = false;
                handler.addObject(new Explosion(fltX+fltWidth/2, fltY + fltHeight/2, 300, 300, ObjectId.BOOM, ssm, handler));
                if(intSessionId == 1) ssm.sendText("h>a>aBOOM~" + (fltX + fltWidth/2) + "," + (fltY + fltHeight/2) + "," + (300) + "," + (300));
                else ssm.sendText("c" + intSessionId + ">h>aBOOM~" + (fltX + fltWidth/2) + "," + (fltY + fltHeight/2) + "," + (300) + "," + (300));
            }
            fltY = (float)new Rectangle(0, 660, 1280, 10).getY() - fltHeight;
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
}
