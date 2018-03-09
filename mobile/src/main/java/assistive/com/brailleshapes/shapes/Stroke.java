package assistive.com.brailleshapes.shapes;

import java.util.ArrayList;

import assistive.com.brailleshapes.Touch;

/**
 * Created by andre on 15-Jun-15.
 */
public class Stroke {
    private ArrayList<Touch> touches;

    public Stroke(){
        touches = new ArrayList<Touch>();
    }

    public void addTouch(Touch t){
        touches.add(t);
    }

    public ArrayList<Touch> getPoints() {
        return touches;
    }
}
