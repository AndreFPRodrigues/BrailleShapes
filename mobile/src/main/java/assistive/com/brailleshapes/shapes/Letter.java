package assistive.com.brailleshapes.shapes;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

import assistive.com.brailleshapes.Touch;

/**
 * Created by andre on 15-Jun-15.
 */
public class Letter {
    private ArrayList <Touch> touches;
    String letter;
    private final String TAG = "Brailler";


    public Letter(String letter){
        this.letter=letter;
        touches= new ArrayList<Touch>();
    }

    public void addTouch(Touch t){
        touches.add(t);
    }

    public ArrayList <Stroke> getStrokes(){
        Collections.sort(touches);
        ArrayList <Stroke> strokes= new ArrayList<Stroke>();
        Stroke stroke= new Stroke();
        for(Touch t: touches){
            if(t.getType()!= MotionEvent.ACTION_UP){
                stroke.addTouch(t);
            }else{
                stroke.addTouch(t);
                strokes.add(stroke);
                stroke = new Stroke();
            }
        }
        return strokes;
    }

    public int getSize() {
        return touches.size();
    }

    public void clear() {
        touches.clear();
    }

    public String getLetter() {
        return letter;
    }
}
