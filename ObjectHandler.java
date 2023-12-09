import java.awt.Graphics;
import java.util.LinkedList;

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

    public void addObject(GameObject object) {
        objectList.add(object);
    }

    public void removeObject(GameObject object) {
        objectList.remove(object);
    }
}
