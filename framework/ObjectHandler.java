package framework;
import java.awt.Graphics;
import java.util.LinkedList;
import objects.EnemyObject;
import objects.GameObject;

public class ObjectHandler {

    public LinkedList<GameObject> objectList = new LinkedList<>();

    public void update() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);
            
            object.update(objectList);
            
        }
    }

    public void draw(Graphics g) {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);

            object.draw(g);
        }
    }

    public int sizeHandler() {
        return objectList.size();
    }

    public void addObject(GameObject object) {
        objectList.add(object);
    }

    public void removeObject(GameObject object) {
        objectList.remove(object);
    }

    public GameObject getObject(int intIndex) {
        return objectList.get(intIndex);
    }

    //public ObjectId ObjectIdParse(){
    //    return 2;
    //}

    public boolean containsObject(GameObject object) {
        return objectList.contains(object);
    }
}
