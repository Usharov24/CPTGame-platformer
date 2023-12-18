package objects;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public abstract class EnemyObject extends GameObject {

    protected float fltRangeX;
    protected float fltRangeY;
    protected float fltHealth;
    protected float fltDamage;
    //protected boolean blnMovement;
    //protected int intMovementType;
    //protected int intAttackType;

    //protected float fltVelX; ??
    //protected float fltVelY; ??

    public EnemyObject(float fltX, float fltY, float fltHeight, float fltWidth, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id){
        super(fltX, fltY, fltHeight, fltWidth, id);
        this.fltRangeX = fltRangeX;
        this.fltRangeY = fltRangeY;
        this.fltHealth = fltHealth;
        this.fltDamage = fltDamage;
    }

    public abstract void update(LinkedList<GameObject> objectList);

    public abstract void draw(Graphics g);
    
    public void networkreceive(){

    }

    public float getRangeX() {
        return fltRangeX;
    }

    public float getRangeY() {
        return fltRangeY;
    }

    public void setRangeX() {
        this.fltRangeX = fltRangeX;
    }

    public void setRangeY() {
        this.fltRangeY = fltRangeY;
    }

    public float getHealth() {
        return fltHealth;
    }

    public float getDamage() {
        return fltDamage;
    }

    public void setHealth() {
        this.fltHealth = fltHealth;
    }

    public void setDamage() {
        this.fltDamage = fltDamage;
    }

    // public void setMovement(){
    //  this.blnMovement = blnMovement;
    //}
}