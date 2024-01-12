package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.ObjectHandler;
import framework.ObjectId;
import framework.SuperSocketMaster;

public abstract class EnemyObject extends GameObject {

    protected float fltRangeX;
    protected float fltRangeY;
    protected float fltVelX;
    protected float fltVelY;
    protected float fltHealth;
    protected float fltDamage;
    //protected boolean blnMovement;
    //protected int intMovementType;
    //protected int intAttackType;

    public EnemyObject(float fltWorldX, float fltWorldY, float fltVelX, float fltVelY, float fltHeight, float fltWidth, float fltRangeX, float fltRangeY, float fltHealth, float fltDamage, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm){
        super(fltWorldX, fltWorldY, fltHeight, fltWidth, id, handler, ssm);
        this.fltRangeX = fltRangeX;
        this.fltRangeY = fltRangeY;
        this.fltVelX = fltVelX;
        this.fltVelY = fltVelY;
        this.fltHealth = fltHealth;
        this.fltDamage = fltDamage;
    }
    
    public void networkreceive(){

    }

    public float getRangeX() {
        return fltRangeX;
    }

    public float getRangeY() {
        return fltRangeY;
    }

    public void setRangeX(float fltRangeX) {
        this.fltRangeX = fltRangeX;
    }

    public void setRangeY(float fltRangeY) {
        this.fltRangeY = fltRangeY;
    }

    public float getX(){
        return fltWorldX;
    }

    public float getY(){
        return fltWorldY;
    }

    public void setX(float fltWorldX){
        this.fltWorldX = fltWorldX;
    }
    
    public void setY(float fltWorldY){
        this.fltWorldY = fltWorldY;
    }

    public float getVelX(){
        return fltVelX;
    }

    public float getVelY(){
        return fltVelY;
    } 

    public void setVelX(float fltVelX){
        this.fltVelX = fltVelX;
    }

    public void setVelY(float fltVelY){
        this.fltVelY = fltVelY;
    }

    public float getHealth() {
        return fltHealth;
    }

    public float getDamage() {
        return fltDamage;
    }

    public void setHealth(float fltHealth) {
        this.fltHealth = fltHealth;
    }

    public void setDamage(float fltDamage) {
        this.fltDamage = fltDamage;
    }
    
    // public void setMovement(boolean blnMovement){
    //  this.blnMovement = blnMovement;
    //}
}
