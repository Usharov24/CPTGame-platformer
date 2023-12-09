import java.awt.Graphics;
import java.util.LinkedList;

public abstract class GameObject {
    
    protected float fltX, fltY;
    protected float fltVelX, fltVelY;
    protected float fltWidth, fltHeight;

    public GameObject(float fltX, float fltY, float fltWidth, float fltHeight) {
        this.fltX = fltX;
        this.fltY = fltY;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
    }

    public abstract void update(LinkedList<GameObject> objectList);
    public abstract void draw(Graphics g);
    
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
}
