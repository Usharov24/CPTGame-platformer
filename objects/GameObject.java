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
    /**
     * @return void 
     * updates the method
     */
    public abstract void update();
    /**
     * @return void 
     * draws stuff
     */
    public abstract void draw(Graphics g);
    /**
     * @return rectangle 
     * reutrns bounds
     */
    public abstract Rectangle getBounds();

    // World X Coordinate Getter
    /**
     * @return float 
     * getx
     */
    public float getWorldX() {
        return fltWorldX;
    }

    // World X Coordinate Setter
    /**
     * @return void 
     * set x
     */
    public void setWorldX(float fltWorldX) {
        this.fltWorldX = fltWorldX;
    }

    // World Y Coordinate Getter
    /**
     * @return float 
     * gety
     */
    public float getWorldY() {
        return fltWorldY;
    }

    // World Y Coordinate Setter
    /**
     * @return void 
     * set y 
     */
    public void setWorldY(float fltWorldY) {
        this.fltWorldY = fltWorldY;
    }

    // Width Getter
    // World Y Coordinate Getter
    /**
     * @return float 
     * get width
     */
    public float getWidth() {
        return fltWidth;
    }

    // Height Getter
    // World Y Coordinate Getter
    /**
     * @return float 
     * get hiehgt
     */
    public float getHeight() {
        return fltHeight;
    }

    // Id Getter
    // World Y Coordinate Getter
    /**
     * @return ObjectId 
     * get Id
     */
    public ObjectId getId() {
        return id;
    }
}
