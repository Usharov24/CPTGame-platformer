package objects;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.InputHandler;
import framework.Main;
import framework.ObjectHandler;
import framework.ObjectId;
import framework.InputHandler.InputButtons;

public class Player extends GameObject {

    public static ObjectHandler handler;
    private InputHandler input;
    private String strdirection;
    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltgravity;
    private float fltjumpvel;
    public int intjumpcount;
    private float fltdashvelx;


    public Player(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, InputHandler input) {
        super(fltX, fltY, fltWidth, fltHeight, id);
        this.handler = handler;
        this.input = input;
    }

    public void update(LinkedList<GameObject> objectList) {
        if(input.buttonSet.contains(InputHandler.InputButtons.W) && intjumpcount < 2){
            input.buttonSet.remove(InputButtons.W);
            input.buttonSet.remove(InputButtons.SPACE);
            fltjumpvel = -30;
            fltgravity = 0;
            intjumpcount++;
        } 
        if(input.buttonSet.contains(InputHandler.InputButtons.SPACE) && intjumpcount <2){
            input.buttonSet.remove(InputButtons.SPACE);
            input.buttonSet.remove(InputButtons.W);
            fltjumpvel = -30;
            fltgravity = 0;
            intjumpcount++;

        } 
        else if(input.buttonSet.contains(InputHandler.InputButtons.S) && fltY < 660) fltVelY += fltAcc;
        else if(input.buttonSet.contains(InputHandler.InputButtons.W) && input.buttonSet.contains(InputHandler.InputButtons.S)) {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        } else {
            if(fltVelY > 0) fltVelY -= fltDec;
            else if(fltVelY < 0) fltVelY += fltDec;
        }
        

        if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
            fltVelX -= fltAcc;
            strdirection = "left";
        }
        else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
            fltVelX += fltAcc;
            strdirection = "right";
        }
        else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        } else {
            if(fltVelX > 0) fltVelX -= fltDec;
            else if(fltVelX < 0) fltVelX += fltDec;
        }
        
        if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            handler.addObject(new Bullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler, framework.Main.intSessionId));
        }
        
        if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON2)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
            fltDiffX /= fltLength;
            fltDiffY /= fltLength;
            handler.addObject(new HomingBullet(fltX + fltWidth/2 - 5, fltY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20, 10, 10, ObjectId.BULLET, handler, framework.Main.intSessionId));
        }

        if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3)) {
            float fltDiffX = input.fltMouseX - (fltX + fltWidth/2);
            float fltDiffY = input.fltMouseY - (fltY + fltHeight/2);
            float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

            fltDiffX /= fltLength;
            fltDiffY /= fltLength;

            for(int intCount = 0; intCount < 2; intCount++) {
                handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 + (float) Math.random() * 3, 6, 6, ObjectId.BULLET, handler, framework.Main.intSessionId));
                handler.addObject(new Bullet(fltX + fltWidth/2 - 3, fltY + fltHeight/2 - 3, fltDiffX * 20 - (float) Math.random() * 3, fltDiffY * 20 - (float) Math.random() * 3, 6, 6, ObjectId.BULLET, handler, framework.Main.intSessionId));
            }
        }

        
        
        if(fltVelX > 10) fltVelX = 10;
        else if(fltVelX < -10) fltVelX = -10;
        if(fltVelY > 10) fltVelY = 10;
        else if(fltVelY < -10) fltVelY = -10;
        if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT)){
            input.buttonSet.remove(InputButtons.SHIFT);
            if(strdirection.equals("right")){
                fltdashvelx = 50;

            }
            if(strdirection.equals("left")){
                fltdashvelx = -50;
                
            }
           
            
        }
        //makes it so the object feels gravity when above ground
        if(fltY < 685){
                fltgravity += 2;
            }

        if(fltY > 685){
            intjumpcount = 0;
            fltgravity = 0;
            fltjumpvel = 0;
            fltY = 685;
        }
        //when hitting ground it takes away all momentum
                
        if(fltdashvelx > 0) fltdashvelx-=5;
        if(fltdashvelx < 0) fltdashvelx+=5;     
        //creates the deceleration for dashes

        fltX += fltVelX + fltdashvelx;
        fltY += fltVelY + fltgravity + fltjumpvel;
        if(ssm != null) ssm.sendText("o,PLAYER," + fltX + "," + fltY + "," + Main.intSessionId);
        
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)fltX, (int)fltY, (int)fltWidth, (int)fltHeight);
    }
}
