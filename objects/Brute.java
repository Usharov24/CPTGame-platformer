package objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

    private long[] lngTimer = {0, 0, 0, 0, 0};

    private float fltAcc = 1f, fltDec = 0.5f;
    private float fltDispX, fltDispY;
    
    private float fltAngle = 270;
    private float fltUltSpeed = 0;

    private int intPosition;
    private int intJumpCount;

    private boolean blnSlamming = false;
    private boolean blnUlt = false;

    private BufferedImage[] biVacTextures;

    private float fltHP = 1000;
    private int intWungoosCount = 0;
    private boolean blnMoving = false;
    private float fltDmgMult = 1;
    private float fltRegen = 4;
    private float fltBSpeedMult = 1;
    private float fltPSpeedMult = 1;
    private float fltReflectDmg = 0;
    private int intPeirceCount = 0;
    private float fltDef = 1;
    private float fltFireRateMult = 1;
    private int intExplodeRad = 0;
    private int intShurikanCount = 0;
    private int intBleedCount = 0;
    private float fltBurnDmg = 0;  
    private float fltAirDmgMult = 1;
    private float fltLifeSteal = 0; 
    private int intCelebShot = 0;
    private int intJumpCap = 2;
    private float fltMaxHP = 1000;
    private BufferedImage biBulletTexture;
    private float fltPastDmgMult = 1;
    private boolean blnHoming = false;
    private boolean blnFalling = false;

    public Brute(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm, InputHandler input, int intPosition) {
        super(fltWorldX, fltWorldY, fltWidth, fltHeight, id, handler, ssm);
        this.input = input;
        this.intPosition = intPosition;

        biVacTextures = resLoader.loadSpriteSheet("/res\\VacGrenade.png", 20, 20);
    }

    public void update() {
        if(intPosition != Main.intSessionId - 1 && camObject == null) camObject = handler.getObject(Main.intSessionId - 1);

        if(intPosition == Main.intSessionId - 1) {
            if(blnFalling){
                fltPastDmgMult = fltDmgMult;
                fltDmgMult *= fltAirDmgMult;
            }
            if(!blnUlt) {
                if(intJumpCount < intJumpCap && (input.buttonSet.contains(InputHandler.InputButtons.W) || input.buttonSet.contains(InputHandler.InputButtons.SPACE))) {
                    input.buttonSet.remove(InputButtons.W);
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

                if(input.buttonSet.contains(InputHandler.InputButtons.SHIFT) && System.currentTimeMillis() - lngTimer[0] > 3000 * fltFireRateMult) {
                    lngTimer[0] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.SHIFT);
                    blnSlamming = true;
                    fltVelY = -35;
                }

                if(input.buttonSet.contains(InputHandler.InputButtons.F) && System.currentTimeMillis() - lngTimer[1] > 1600 * fltFireRateMult) {
                    lngTimer[1] = System.currentTimeMillis();
                    input.buttonSet.remove(InputButtons.F);
                    blnUlt = true;
                    blnSlamming = false;
                    fltWorldY -= 100;
                }

                if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON1) && System.currentTimeMillis() - lngTimer[2] > 50 * fltFireRateMult) {
                    lngTimer[2] = System.currentTimeMillis();
                    if(blnFalling){
                        fltPastDmgMult = fltDmgMult;
                        fltDmgMult *= fltAirDmgMult;
                    }
                    lngTimer[2] = System.currentTimeMillis();
                    for(int intcount = 0; intcount < intShurikanCount; intcount++){
                        float fltDiffX = input.fltMouseX - 640;
                        float fltDiffY = input.fltMouseY - 360;
                        float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));
                        float intRand1 = (float)Math.random() * 3, intRand2 = (float)Math.random() * 3;
                        float intRand3 = (float)Math.random() * 3, intRand4 = (float)Math.random() * 3;

                        fltDiffX /= fltLength;
                        fltDiffY /= fltLength;

                    
                        if(intPosition == 1) {
                            ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6 + "," + 4);
                            ssm.sendText("h>a>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 - intRand3) + "," + 6 + "," + 6 + "," + 4);
                        } else {
                            ssm.sendText("c" + (intPosition + 1) + ">h>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand1) + "," + (fltDiffY * 20 + intRand3) + "," + 6 + "," + 6 + "," + 4);
                            ssm.sendText("c" + (intPosition + 1) + ">h>aSHRAPNEL~" + (fltWorldX + fltWidth/2 - 3) + "," + (fltWorldY + fltHeight/2 - 3) + "," + (fltDiffX * 20 - intRand2) + "," + (fltDiffY * 20 - intRand4) + "," + 6 + "," + 6 + "," + 4);
                        }

                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand1, fltDiffY * 20 + intRand3, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad, 0));
                        handler.addObject(new Bullet(fltWorldX + fltWidth/2 - 3, fltWorldY + fltHeight/2 - 3, fltDiffX * 20 - intRand2, fltDiffY * 20 - intRand4, 6, 6, intPeirceCount, intBleedCount, fltBurnDmg, fltLifeSteal, intCelebShot, 100*fltDmgMult, ObjectId.BULLET, handler, ssm, biBulletTexture, blnHoming, intExplodeRad, 0));
                    }
                    if(input.fltMouseX - 640 < 0) {
                        handler.addObject(new KnightSlashes(fltWorldX + 25, fltWorldY+15, -20 * fltBSpeedMult, System.currentTimeMillis() - 75, 50, 50, 135, 0, 0, 0, 0, 0, 0, id, handler, ssm));
                        
                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 * fltBSpeedMult +"," + (50) + "," + (50) + "," + 135);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + -20 * fltBSpeedMult+"," + (50) + "," + (50) + "," + 135);
                    } else {
                        handler.addObject(new KnightSlashes(fltWorldX, fltWorldY+15 , 20 * fltBSpeedMult, System.currentTimeMillis() - 75, 50, 50, 270, 0, 0, 0, 0, 0, 0, id, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 * fltBSpeedMult +"," + (50) + "," + (50) + "," + 270);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aSLASH~" + (fltWorldX + 25) + "," + (fltWorldY + 15) + "," + 20 * fltBSpeedMult +"," + (50) + "," + (50) + "," + 270);
                    }
                } else if(input.buttonSet.contains(InputHandler.InputButtons.BUTTON3) && System.currentTimeMillis() - lngTimer[3] > 3000) {
                    lngTimer[3] = System.currentTimeMillis();
                    
                    float fltDiffX = input.fltMouseX - 640;
                    float fltDiffY = input.fltMouseY - 360;
                    float fltLength = (float)Math.sqrt(Math.pow(fltDiffX, 2) + Math.pow(fltDiffY, 2));

                    fltDiffX /= fltLength;
                    fltDiffY /= fltLength;

                    handler.addObject(new VacGrenade(fltWorldX + fltWidth/2 - 10, fltWorldY + fltHeight/2 - 10, fltDiffX * 40, fltDiffY * 40, 20, 20, ObjectId.VAC_GREN, handler, ssm, biVacTextures));
                    
                    if(intPosition == 0) ssm.sendText("h>a>aVAC~" + (fltWorldX + fltWidth/2 - 10) + "," + (fltWorldY + fltHeight/2 - 10) + "," + (fltDiffX * 40) + "," + (fltDiffY * 40) + "," + 20 + "," + 20);
                    else ssm.sendText("c" + (intPosition + 1) + ">h>aVAC~" + (fltWorldX + fltWidth/2 - 10) + "," + (fltWorldY + fltHeight/2 - 10) + "," + (fltDiffX * 40) + "," + (fltDiffY * 40) + "," + 20 + "," + 20);
                }

                if(System.currentTimeMillis() - lngTimer[4] > 1000){
                    lngTimer[4] = System.currentTimeMillis();
                    if(blnMoving == false){
                        fltHP += fltRegen;
                    }
                    else{
                        fltHP += fltRegen + (fltRegen*intWungoosCount*0.3);
                    }
                }

                if(fltHP > fltMaxHP){
                    fltHP = fltMaxHP;
                }

                fltVelY += 3;

                if(fltVelX > 15) fltVelX = 15;
                else if(fltVelX < -15) fltVelX = -15;

                if(fltVelY > 35) fltVelY = 35;
                else if(fltVelY < -35) fltVelY = -35;
            } else {
                if(input.buttonSet.contains(InputHandler.InputButtons.A)) {
                    fltAngle -= 8;
                } else if(input.buttonSet.contains(InputHandler.InputButtons.D)) {
                    fltAngle += 8;
                }

                fltUltSpeed += 0.3;
                fltVelY += (float)(fltUltSpeed*Math.sin(Math.toRadians(fltAngle)));
                fltVelX += (float)(fltUltSpeed*Math.cos(Math.toRadians(fltAngle)));

                if(fltVelX > 35 * fltPSpeedMult) fltVelX = 35 * fltPSpeedMult;
                else if(fltVelX < -35 * fltPSpeedMult) fltVelX = -35 * fltPSpeedMult;

                if(fltVelY > 35) fltVelY = 35;
                else if(fltVelY < -35) fltVelY = -35;
            }

            collisions();

            if(!blnSlamming) fltWorldX += fltVelX;
            else fltWorldX += fltVelX * 2;

            fltWorldY += fltVelY;

            if(intPosition == 0) ssm.sendText("h>a>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            else ssm.sendText("c" + (intPosition + 1) + ">h>oBRUTE~" + fltWorldX + "," + fltWorldY + "," + intPosition);
            fltDmgMult = fltPastDmgMult;
        }
    }

    private void collisions() {
        for(int intCount = 0; intCount < handler.objectList.size(); intCount++) {
            GameObject object = handler.getObject(intCount);
    
            if(object.getId() == ObjectId.BARRIER) {
                if(getBounds().intersects(object.getBounds()) && fltVelX > 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() - fltWidth;

                    if(blnUlt) {
                        blnUlt = false;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth, fltWorldY + fltHeight/2, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight/2) + "," + 300 + "," + 300);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth) + "," + (fltWorldY + fltHeight/2) + "," + 300 + "," + 300);
                    }
                } else if(getBounds().intersects(object.getBounds()) && fltVelX < 0) {
                    fltVelX = 0;
                    fltWorldX = object.getWorldX() + object.getWidth();

                    if(blnUlt) {
                        blnUlt = false;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX, fltWorldY + fltHeight/2, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + 300 + "," + 300);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + fltWorldX + "," + (fltWorldY + fltHeight/2) + "," + 300 + "," + 300);
                    }
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY > 0) {
                    fltVelY = 0;
                    intJumpCount = 0;
                    fltWorldY = object.getWorldY() - fltHeight;

                    if(blnSlamming) {
                        blnSlamming = false;

                        for(int intCount2 = 0; intCount2 < 2; intCount2++) {
                            handler.addObject(new Explosion(fltWorldX + fltWidth * intCount2, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, handler, ssm));

                            if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth * intCount2) + "," + (fltWorldY + fltHeight) + "," + 300 + "," + 300);
                            else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth * intCount2) + "," + (fltWorldY + fltHeight) + "," + 300 + "," + 300);
                        }
                    } else if(blnUlt) {
                        blnUlt = false;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth/2, fltWorldY + fltHeight, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth/2) + "," + (fltWorldY + fltHeight) + "," + 300 + "," + 300);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth/2) + "," + (fltWorldY + fltHeight) + "," + 300 + "," + 300);
                    }
                } else if(getBounds2().intersects(object.getBounds()) && fltVelY < 0) {
                    fltVelY = 0;
                    fltWorldY = object.getWorldY() + object.getHeight();

                    if(blnUlt) {
                        blnUlt = false;
                        fltAngle = 270;

                        handler.addObject(new Explosion(fltWorldX + fltWidth/2, fltWorldY, 300, 300, ObjectId.BOOM, handler, ssm));

                        if(intPosition == 0) ssm.sendText("h>a>aBOOM~" + (fltWorldX + fltWidth/2) + "," + fltWorldY + "," + 300 + "," + 300);
                        else ssm.sendText("c" + (intPosition + 1) + ">h>aBOOM~" + (fltWorldX + fltWidth/2) + "," + fltWorldY + "," + 300 + "," + 300);
                    }
                }
            } else if(object.getId() == ObjectId.ENEMY){
                Enemy enemy = (Enemy) object;
                fltHP -= enemy.getDmg() / fltDef;
                if(fltReflectDmg > 0){
                    enemy.setHP(enemy.getHP() - enemy.getDmg()*fltReflectDmg);
                }
            } else if(object.getId() == ObjectId.ENEMY_BULLET && getBounds().intersects(object.getBounds())){
                EnemyBullet enemy = (EnemyBullet) object;
                fltHP -= enemy.getDMG() / fltDef;
                handler.removeObject(object);
            } else if(object.getId() == ObjectId.ITEM && getBounds().intersects(object.getBounds())) {  
                handler.removeObject(handler.getObject(intCount));
                ItemObject item = (ItemObject) object;
                if(item.getRarity() == 1){ 
                    if(item.getPlacement() == 1){
                        fltDmgMult += 0.2;
                    }
                    else if(item.getPlacement() == 2){
                        fltMaxHP += 20;
                        fltHP += 20;
                    }
                    else if(item.getPlacement() == 3){
                        //add statement later using bln movement
                        intWungoosCount += 1;                       
                    }
                    else if(item.getPlacement() == 4){
                        fltBSpeedMult *= 1.2;
                    }
                    else if(item.getPlacement() == 5){
                        fltPSpeedMult *= 1.2;
                    }
                    else if(item.getPlacement() == 6){
                        fltReflectDmg += 1;
                        //reflect 10% of the dmg and then mult by this
                    }
                    else if(item.getPlacement() == 7){
                        intPeirceCount += 1;
                    }
                    else if(item.getPlacement() == 8){
                        fltDef += 0.2;
                    }

                    else if(item.getPlacement() == 9){
                        fltFireRateMult *= 0.9;
                    }
                }
                else if(item.getRarity() == 2){ 
                    if(item.getPlacement() == 1){
                        fltAirDmgMult += 0.2;
                    }
                    else if(item.getPlacement() == 2){
                        fltMaxHP *= 0.2;
                        fltHP *= 0.2;
                    }
                    else if(item.getPlacement() == 3){
                        intExplodeRad += 25;
                    }
                    else if(item.getPlacement() == 4){
                        intJumpCap ++;
                    }
                    else if(item.getPlacement() == 5){
                        intBleedCount += 1;
                    }
                    else if(item.getPlacement() == 6){
                        intShurikanCount += 1;
                    }
                    else if(item.getPlacement() == 7){
                        fltBurnDmg += 10;
                    }
                }
                else if(item.getRarity() == 3){ 
                    if(item.getPlacement() == 1){
                        fltLifeSteal += 0.2;
                    }
                    else if(item.getPlacement() == 2){
                        //wont do anything for brute
                        blnHoming = true;
                    }
                    else if(item.getPlacement() == 3){
                        fltRegen *= 2;
                    }
                    else if(item.getPlacement() == 4){
                        fltFireRateMult *= 0.75;
                    }
                    else if(item.getPlacement() == 5){
                        intCelebShot += 1;
                    }
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

        return new Rectangle((int)fltBoundsX, (int)(fltDispY - fltHeight/2) + 4, (int)fltWidth, (int)fltHeight - 8);
    }

    public Rectangle getBounds2() {
        float fltBoundsY = fltDispY + fltVelY - fltHeight/2;

        if(fltBoundsY > fltHeight/2) fltBoundsY = fltHeight/2;
        else if(fltBoundsY < -fltHeight * 1.5f) fltBoundsY = -fltHeight * 1.5f;

        return new Rectangle((int)(fltDispX - fltWidth/2) + 4, (int)fltBoundsY, (int)fltWidth - 8, (int)fltHeight);
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

    public float getDef(){
        return fltDef;
    }

    public float getReflecDmg(){
        return fltReflectDmg;
    }

    public int getChar(){
        return 1;
    }
}
