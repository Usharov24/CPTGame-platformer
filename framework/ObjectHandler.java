package framework;
import java.awt.Graphics;
import java.util.LinkedList;

import objects.GameObject;

public class ObjectHandler {

    public LinkedList<GameObject> objectList = new LinkedList<>();

    public void update() {
        for(int intCount = 0; intCount < objectList.size(); intCount++) {
            GameObject object = objectList.get(intCount);
            
            object.update(objectList);
            
        }
    }
    public GameObject findnear(float fltX, float fltY){
        float fltDistX = 0;     
        float fltDistY = 0;
        float flttotaldist = 0;
        float fltPastX = 0;
        float fltPastY = 0;
        float fltpastTotal = 0;
        int intreturn = 0;
        for(int i = 0; i < objectList.size(); i++){
            
            fltDistX = fltX - objectList.get(i).getX();
            fltDistY = fltY - objectList.get(i).getY();
            flttotaldist = (float) Math.sqrt(fltDistX*fltDistX + fltDistY*fltDistY);
            if(flttotaldist > fltpastTotal){
                fltpastTotal = flttotaldist;
                fltPastX = fltDistX;
                fltPastY = fltDistY;
                intreturn = i;
            }

           
        }
        return objectList.get(intreturn);

        
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

    public GameObject getObject(int intIndex) {
        return objectList.get(intIndex);
    }

    public boolean containsObject(GameObject object) {
        return objectList.contains(object);
    }
}
