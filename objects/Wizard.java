package Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Framework.InputHandler;
import Framework.Main;
import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.ResourceLoader;
import Framework.SuperSocketMaster;
import Framework.InputHandler.InputButtons;

public class Wizard extends GameObject {

    private InputHandler input;
    private ResourceLoader resLoader = new ResourceLoader();

    private BufferedImage[] biSprite;
    private BufferedImage[] biBulletTextures;
    private BufferedImage[] biCountDown;

    private float fltAcc = 1f, fltDec = 0.5f;

    private float fltDmgMult = 1;
    private float fltRegen = 4;
    private float fltBSpeedMult = 1;
    private float fltPSpeedMult = 1;
    private float fltReflectDmg = 0;
    private float fltMaxHP = 1000;
    private float fltPastDmgMult = 1;
    private float fltAirDmgMult = 1;
    private float fltLifeSteal = 0; 
    private float fltDef = 1;
    private float fltFireRateMult = 1;
    private float fltBurnDmg = 0;  
    private float fltHP = 1000;

    private long[] lngTimer = {0, 0, 0, 0, 0, 0};

    private int intPosition;
    private int intJumpCount;
    private int intWungoosCount = 0;
    private int intPeirceCount = 0;
    private int intExplodeRad = 0;
    private int intShurikenCount = 0;
    private int intBleedCount = 0;
    private int intCelebShot = 0;
    private int intJumpCap = 2;

    private boolean blnHoming = false;
    private boolean blnLeft = false;
    private boolean blnFalling = true;
    private boolean blnTeleporting = false;
    //all essential variables organized
    
    public Wizard(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.intPosition = intPosition;

        //tells the other folders how to handle this object and when
        biSprite = resLoader.loadImages("/res\\Wizard.png"); 
        biBulletTextures = resLoader.loadImages("/res\\FireBall.png", "/res\\ElectricBall.png" , "/res\\Shrapnel.png");
        biCountDown = resLoader.loadImages("/res\\M2.png","/res\\Shift.png","/res\\FKey.png");;
        //setting the texture and height and width for the player
    }
    
    public void update() {
        if(intPosition != Main.intSessionId - 1 && camObject == null) camObject = handler.getObject(Main.intSessionId - 1);

        if(Main.state == Main.State.GAME && (fltWorldX < -40 || fltWorldX > 1960 || fltWorldY < -40 || fltWorldY > 1480)) {
            fltWorldX = 200;
            fltWorldY = 1400;
        } else if(Main.state == Main.State.DEMO && (fltWorldX < -40 || fltWorldX > 1240 || fltWorldY < -40 || fltWorldY > 840)){
            fltWorldX = 200;
            fltWorldY = 800;
        }

        if(intPosition == Main.intSessionId - 1) {
            //checks if the player is the host
            if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                input.buttonSet.remove(InputButtons.W);
                input.buttonSet.remove(InputButtons.SPACE);
                fltVelY = -45;
                intJumpCount++;
                blnFalling = true;
            }
            //jumping for the character

            if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                fltVelX -= fltAcc;
                blnLeft = true;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                fltVelX += fltAcc;
                blnLeft = false;
            } else if(input.buttonSet.contains(InputHandler.InputButtons.A) && input.buttonSet.contains(InputHandler.InputButtons.D)) {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            } else {
                if(fltVelX > 0) fltVelX -= fltDec;
                else if(fltVelX < 0) fltVelX += fltDec;
            }
            //general movement for the character

            if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 5000 * fltFireRateMult) {
                //Moving variables
                lngTimer[0] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.SHIFT);
                blnTeleporting = true;
            }
            //movement ability for the character which teleports the wizard where he clicks, the timer is used to determine the cooldown 
            //it is multipled by fire rate in order to make abilities come back faster
            if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 8000 * fltFireRateMult) {
                lngTimer[1] = System.currentTimeMillis();
                input.buttonSet.remove(InputButtons.F);
                if(blnFalling){
                    fltPastDmgMult = fltDmgMult;
                    fltDmgMult *= fltAirDmgMult;
                }

                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 0 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 0 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + -20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 0 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 0 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + -20 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 0 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + 20 + "," + 20 + "," + 100 + "," + 100 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 100 * fltDmgMult +","+ 3 +","+ blnHoming +","+ intExplodeRad);

                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 0, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, -20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 0, -20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, -20, -20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, -20, 0, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, -20, 20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 0, 20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, 20, 20, 30, 30, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100 * fltDmgMult, ObjectId.HOMING_BULLET, handler, ssm, biBulletTextures[1], true, intExplodeRad, 3));
                //ultimate ability
                
                fltDmgMult = fltPastDmgMult;
            }   
            //sends the velocites and characteristics of each bullet while also adding them to the personal game

            if(blnTeleporting && input.buttonSet.contains(InputHandler.InputButtons.BUTTON1)){
                if(fltWorldY + input.fltMouseY - 360 > 0 && fltWorldY + input.fltMouseY - 360 < 1376 && fltWorldX + input.fltMouseX - 640 > 0 && fltWorldX + input.fltMouseX - 640 < 1886){
                    input.buttonSet.remove(InputButtons.BUTTON1);
                    blnFalling = true;
                    blnTeleporting = false;
                    fltWorldX += input.fltMouseX - 640;
                    fltWorldY += input.fltMouseY - 360;
                }
            }
            //used to detmerine if the player needs to be teleported

            if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 500 * fltFireRateMult) {
                lngTimer[2] = System.currentTimeMillis();
                if(blnFalling){
                    fltPastDmgMult = fltDmgMult;
                    fltDmgMult *= fltAirDmgMult;
                }
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
                float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                fltDiffX /= fltLength;
                fltDiffY /= fltLength;
                
                for(int intcount = 0; intcount < intShurikenCount; intcount++){
                    float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                    float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                    if(intPosition == 0 && ssm != null) {
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30 * fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("h>a>aBULLET~" +(fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30 * fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    } else if(ssm != null) {
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand3) * fltBSpeedMult  + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30 * fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                        ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 + intRand2) * fltBSpeedMult + "," + (fltDiffY * 20 + intRand4) * fltBSpeedMult + "," + 6 + "," + 6 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 30 * fltDmgMult +","+ 4 +","+ blnHoming +","+ intExplodeRad);
                    }
                    
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand1) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30 * fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 3));
                    handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, (fltDiffX * 20 - intRand2) * fltBSpeedMult, (fltDiffY * 20 + intRand3) * fltBSpeedMult, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 30 * fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[2], blnHoming, intExplodeRad, 3));
                    //used for a specific item which adds projectiles to every primary attack
                }

                handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20 * fltBSpeedMult, fltDiffY * 20 * fltBSpeedMult, 40, 40, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 150*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTextures[0], blnHoming, intExplodeRad,0));
                fltDmgMult = fltPastDmgMult;
                //this is used for the air damagemultiplier
                //primary attack which shoots one big bullet

                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20 * fltBSpeedMult) + "," + (fltDiffY * 20) + "," + 40 + "," + 40 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 2 +","+ blnHoming +","+ intExplodeRad);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aBULLET~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20 * fltBSpeedMult) + "," + (fltDiffY * 20) + "," + 40 + "," + 40 + "," + intPeirceCount +"," + intBleedCount +","+ fltBurnDmg +","+ 150*fltDmgMult +","+ 2 +","+ blnHoming +","+ intExplodeRad);
                //sends the bullet parameters to other players
            } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 4000) {
                lngTimer[3] = System.currentTimeMillis();
                if(blnFalling){
                    fltPastDmgMult = fltDmgMult;
                    fltDmgMult *= fltAirDmgMult;
                }
                float fltDiffX = input.fltMouseX - 640;
                float fltDiffY = input.fltMouseY - 360;
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
                //used to determine the necessary angle

                handler.addObject(new WaveAttacks(fltWorldX + fltWidth/2 - 5, fltWorldY + fltHeight/2 - 5, fltDiffX * 20, fltDiffY * 20 * -1, 10, 10, fltStartAngle, 200*fltDmgMult, intExplodeRad, fltBurnDmg, intBleedCount, fltLifeSteal, intCelebShot, ObjectId.BULLET, handler, ssm));
                fltDmgMult = fltPastDmgMult;
                //created a wave attack that spreads as it goes, makes sure to aim where the player is aiming

                if(intPosition == 0 && ssm != null) ssm.sendText("h>a>aWAVE~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * -20) + "," + 10 + "," + 10 + "," + fltStartAngle+ "," + 200*fltDmgMult + "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount);
                else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>aWAVE~" + (fltWorldX + fltWidth/2 - 5) + "," + (fltWorldY + fltHeight/2 - 5) + "," + (fltDiffX * 20) + "," + (fltDiffY * -20) + "," + 10 + "," + 10 + "," + fltStartAngle+ "," + 200*fltDmgMult + "," + intExplodeRad+ "," + fltBurnDmg+ "," + intBleedCount);
            }  

            if(System.currentTimeMillis() - lngTimer[4] > 200){
                lngTimer[4] = System.currentTimeMillis();
                if(fltVelX == 0 && fltVelY == 0 && intWungoosCount > 0){
                    fltHP += (fltRegen + (fltRegen*intWungoosCount*0.3));
                }
                else{
                    fltHP += fltRegen;
                }
                //regenerates the player health -
            }

            if(fltHP > fltMaxHP){
                fltHP = fltMaxHP;
            }

            fltVelY += 3;

            if(fltVelX > 10 * fltPSpeedMult) fltVelX = 10 * fltPSpeedMult;
            else if(fltVelX < -10 * fltPSpeedMult) fltVelX = -10 * fltPSpeedMult;

            if(fltVelY > 35) fltVelY = 35;
            else if(fltVelY < -35) fltVelY = -35;
            
            collisions();

            fltWorldX += fltVelX;
            fltWorldY += fltVelY;
            
            if(intPosition == 0 && ssm != null) ssm.sendText("h>a>oWIZARD~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + fltHP + ","+ fltMaxHP + "," + intPosition);
            else if(ssm != null) ssm.sendText("c" + (intPosition + 1) + ">h>oWIZARD~" + fltWorldX + "," + fltWorldY + "," + blnLeft + "," + fltHP + ","+ fltMaxHP + "," + intPosition);
        }
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
            //for statement that checks every object and checks if it reacts to the player upon collision
            if(object.getId() == ObjectId.BARRIER || object.getId() == ObjectId.PERM_BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;
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
                //if the player collides with a barrier, stop the player
            } else if( (object.getId() == ObjectId.ENEMY && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                Enemy enemy = (Enemy) object;
                fltHP -= enemy.getDmg() / fltDef;
                if(fltReflectDmg > 0){
                    enemy.setHP(enemy.getHP() - (float)(enemy.getDmg()*fltReflectDmg*0.1));
                }
                //damage system where damage might be traded
                lngTimer[5] = System.currentTimeMillis();
                //invincibilty frames to ensure the player doesnt die instantly upon collision
            } else if((object.getId() == ObjectId.ENEMY_BULLET && getBounds().intersects(object.getBounds())) || (object.getId() == ObjectId.ENEMY_BULLET && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyBullet enemy = (EnemyBullet) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
                //if the player collides with an enemy bullet, remove the bullet from existence
            } else if((object.getId() == ObjectId.ENEMY_BOOM) && getBounds().intersects(object.getBounds()) || (object.getId() == ObjectId.ENEMY_BOOM && getBounds2().intersects(object.getBounds())) && System.currentTimeMillis() - lngTimer[5] > 500){
                EnemyExplosion enemy = (EnemyExplosion) object;
                fltHP -= enemy.getDmg() / fltDef;
                handler.removeObject(object);
                lngTimer[5] = System.currentTimeMillis();
            } else if(object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds())) {  
                handler.removeObject(handler.getObject(intCount));
                Item item = (Item) object;
                if(item.getRarity() == 1){ 
                    if(item.getPlacement() == 1) {
                        fltDmgMult += 0.4;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP += 100;
                        fltHP += 100;
                    } else if(item.getPlacement() == 3) {
                        //add statement later using bln movement
                        intWungoosCount += 2;                       
                    } else if(item.getPlacement() == 4) {
                        fltBSpeedMult *= 1.2;
                    } else if(item.getPlacement() == 5) {
                        fltPSpeedMult *= 1.2;
                    } else if(item.getPlacement() == 6) {
                        fltReflectDmg += 1;
                        //reflect 10% of the dmg and then mult by this
                    } else if(item.getPlacement() == 7) {
                        intPeirceCount += 1;
                    } else if(item.getPlacement() == 8) {
                        fltDef += 0.3;
                    } else if(item.getPlacement() == 9) {
                        fltFireRateMult *= 0.9;
                    }
                } else if(item.getRarity() == 2) { 
                    if(item.getPlacement() == 1) {
                        fltAirDmgMult += 0.2;
                    } else if(item.getPlacement() == 2) {
                        fltMaxHP *= 0.3;
                        fltHP *= 0.3;
                    } else if(item.getPlacement() == 3) {
                        intExplodeRad += 25;
                    } else if(item.getPlacement() == 4) {
                        intJumpCap++;
                    } else if(item.getPlacement() == 5) {
                        intBleedCount += 1;
                    } else if(item.getPlacement() == 6) {
                        intShurikenCount += 1;
                    } else if(item.getPlacement() == 7) {
                        fltBurnDmg += 10;
                    }
                } else if(item.getRarity() == 3) { 
                    if(item.getPlacement() == 1) {
                        fltLifeSteal += 0.2;
                    } else if(item.getPlacement() == 2) {
                        //wont do anything for brute
                        blnHoming = true;
                    } else if(item.getPlacement() == 3) {
                        fltRegen *= 2;
                    } else if(item.getPlacement() == 4) {
                        fltFireRateMult *= 0.6;
                    } else if(item.getPlacement() == 5) {
                        intCelebShot += 1;
                    }
                }
                //checks the id of the item upon collision and then adds a certain attribute to the player
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        if(intPosition == Main.intSessionId - 1) {
            if(blnLeft) {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2 + 32, (int)-fltHeight/2, -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)-fltWidth/2, (int)-fltHeight/2, null);
            }
            //flips the sprite around determined off of the direction of the player
            if(System.currentTimeMillis() - lngTimer[0] > 5000){
                g2d.drawImage(biCountDown[1], 450, -325, null);

            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(450, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((5000-(System.currentTimeMillis()-lngTimer[0]))/1000))), 467, -302);
            }
            if(System.currentTimeMillis() - lngTimer[1] > 8000){
                g2d.drawImage(biCountDown[2], 500, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(500, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((8000-(System.currentTimeMillis()-lngTimer[1]))/1000))), 517, -302);
            }
            if(System.currentTimeMillis() - lngTimer[3] > 4000){
                g2d.drawImage(biCountDown[0], 400, -325, null);
            }else{
                g2d.setColor(Color.gray);
                g2d.fillRect(400, -325, 40, 40);
                g2d.setColor(Color.white);
                g2d.drawString(Integer.toString(Math.round(((4000-(System.currentTimeMillis()-lngTimer[3]))/1000))), 417, -302);
            }
             
        } else {
            if(blnLeft){
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2) + 32, (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), -32, 64, null);
            } else {
                g2d.drawImage(biSprite[0], (int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), null);
            }
            //flips the sprite over a network
        }

        if(blnTeleporting) {
            g2d.setColor(Color.gray);
            g2d.fillRect((int)(input.fltMouseX - fltWidth/2 - 640), (int)(input.fltMouseY - fltHeight/2 - 360), (int)fltWidth, (int)fltHeight);
            //draws where the player wil be teleporting if they click
        }
    }

    public Rectangle getBounds() {
        if(intPosition == Main.intSessionId - 1) {
            float fltBoundsX = fltVelX - fltWidth/2;

            if(fltBoundsX > fltWidth/2) fltBoundsX = fltWidth/2;
            else if(fltBoundsX < -fltWidth * 1.5f) fltBoundsX = -fltWidth * 1.5f;

            return new Rectangle((int)fltBoundsX, (int)-fltHeight/2 + 4, (int)fltWidth, (int)fltHeight - 8);
        } else {
            return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        }
    }
    //bounds for the player to use upon collisions

    public Rectangle getBounds2() {
        if(intPosition == Main.intSessionId - 1) {
            float fltBoundsY = fltVelY - fltHeight/2;

            if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
            else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

            return new Rectangle((int)-fltWidth/2 + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
        } else {
            return new Rectangle((int)(fltWorldX - camObject.getWorldX() - camObject.getWidth()/2), (int)(fltWorldY - camObject.getWorldY() - camObject.getHeight()/2), (int)fltWidth, (int)fltHeight);
        }
    }

    public float getHP(){
        return fltHP;
    }

    public float getMaxHP(){
        return fltMaxHP;
    }

    public void setHP(float fltHP){
        this.fltHP = fltHP;
    }

    public void setMaxHP(float fltMaxHP){
        this.fltMaxHP = fltMaxHP;
    }

    public float getDef(){
        return fltDef;
    }

    public int getChar(){
        return 3;
    }

    public void setLeft(boolean blnLeft){
        this.blnLeft = blnLeft;
    }

    public long[] getTimer(){
        return this.lngTimer;
    }
    //all these methods are used in either the enemy class or the main class to determine dmg health etc.
}