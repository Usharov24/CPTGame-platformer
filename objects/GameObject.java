package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.ObjectId;
import framework.SuperSocketMaster;

public abstract class GameObject {
    
    protected float fltWorldX, fltWorldY;
    protected float fltCameraX, fltCameraY;
    protected float fltVelX, fltVelY;
    protected float fltWidth, fltHeight;
    protected ObjectId id;
    protected SuperSocketMaster ssm;

    public GameObject(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, SuperSocketMaster ssm) {
        this.fltWorldX = fltWorldX;
        this.fltWorldY = fltWorldY;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.id = id;
        this.ssm = ssm;
    }

    public abstract void update(LinkedList<GameObject> objectList);
    public abstract void draw(Graphics g);
    public abstract Rectangle getBounds();

    public float getWorldX() {
        return fltWorldX;
    }

    public void setWorldX(float fltWorldX) {
        this.fltWorldX = fltWorldX;
    }

    public float getWorldY() {
        return fltWorldY;
    }

    public void setWorldY(float fltWorldY) {
        this.fltWorldY = fltWorldY;
    }

    public float getCameraX() {
        return fltCameraX;
    }

    public void setCameraX(float fltCameraX) {
        this.fltCameraX = fltCameraX;
    }

    public float getCameraY() {
        return fltCameraY;
    }

    public void setCameraY(float fltCameraY) {
        this.fltCameraY = fltCameraY;
    }

    public float getWidth() {
        return fltWidth;
    }

    public float getHeight() {
        return fltHeight;
    }

    public ObjectId getId() {
        return id;
    }
}
