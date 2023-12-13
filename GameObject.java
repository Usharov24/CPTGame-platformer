import java.awt.Graphics;
import java.util.LinkedList;

public abstract class GameObject {
    
    protected float fltX, fltY;
    protected float fltVelX, fltVelY;
    protected float fltWidth, fltHeight;
    protected ObjectId id;
    SuperSocketMaster ssm = null;
    private ObjectHandler handler;

    public GameObject(float fltX, float fltY, float fltWidth, float fltHeight, ObjectId id) {
        this.fltX = fltX;
        this.fltY = fltY;
        this.fltWidth = fltWidth;
        this.fltHeight = fltHeight;
        this.id = id;
    }

    public abstract void update(LinkedList<GameObject> objectList);
    public abstract void draw(Graphics g);
    
    public void networkrecieve(){
        String strmsg[] = ssm.readText().split(",");
        if(strmsg[3] == "BULLET"){
            handler.addObject(new Bullet(Float.parseFloat(strmsg[0]), Float.parseFloat(strmsg[1]), Float.parseFloat(strmsg[2]),Float.parseFloat(strmsg[3]), Float.parseFloat(strmsg[5]), Float.parseFloat(strmsg[6]), ObjectId.BULLET, handler));
        } 
        
    }

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
