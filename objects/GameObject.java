package Objects;
import java.awt.Graphics;
import java.awt.Rectangle;

import Framework.ObjectHandler;
import Framework.ObjectId;
import Framework.SuperSocketMaster;

public abstract class GameObject {

    // Properties
    protected float fltWorldX, fltWorldY;
    protected float fltVelX, fltVelY;
    protected float fltWidth, fltHeight;
    protected ObjectId id;
    protected GameObject camObject;
    protected ObjectHandler handler;
    protected SuperSocketMaster ssm;

    // Constructor
    public GameObject(float fltWorldX, float fltWorldY, float fltWidth, float fltHeight, ObjectId id, ObjectHandler handler, SuperSocketMaster ssm) {
        this.fltWorldX = fltWorldX;
        this.fltWorldY = fltWorldY;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.id = id;
        this.handler = handler;
        this.ssm = ssm;
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract Rectangle getBounds();

    // World X Coordinate Getter
    public float getWorldX() {
        return fltWorldX;
    }

    // World X Coordinate Setter
    public void setWorldX(float fltWorldX) {
        this.fltWorldX = fltWorldX;
    }

    // World Y Coordinate Getter
    public float getWorldY() {
        return fltWorldY;
    }

    // World Y Coordinate Setter
    public void setWorldY(float fltWorldY) {
        this.fltWorldY = fltWorldY;
    }

    // Width Getter
    public float getWidth() {
        return fltWidth;
    }

    // Height Getter
    public float getHeight() {
        return fltHeight;
    }

    // Id Getter
    public ObjectId getId() {
        return id;
    }
}
