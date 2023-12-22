package objects;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.ObjectId;

public abstract class GameObject {
    
    protected float fltX, fltY;
    protected float fltVelX, fltVelY;
    protected float fltWidth, fltHeight;
    protected ObjectId id;

    public GameObject(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id) {
        this.fltX = fltX;
        this.fltY = fltY;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.id = id;
    }

    public abstract void update(LinkedList<GameObject> objectList);
    public abstract void draw(Graphics g);
    public abstract Rectangle getBounds();

    public float getX() {
        return fltX;
    }

    public void setX(float fltX) {
        this.fltX = fltX;
    }

    public float getY() {
        return fltY;
    }

    public void setY(float fltY) {
        this.fltY = fltY;
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
