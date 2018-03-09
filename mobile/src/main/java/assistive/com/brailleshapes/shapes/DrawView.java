package assistive.com.brailleshapes.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import assistive.com.brailleshapes.Logging;
import assistive.com.brailleshapes.Touch;

/**
 * Created by andre on 28-May-15.
 */
public class DrawView extends View {
    private final String TAG = "Brailler";
    private Paint paint = new Paint();
    private Path path = new Path();
    private ArrayList<Touch> downs;
    private boolean moved;
    private float x = 0;
    private float y = 0;
    private Logging log;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        downs = new ArrayList<Touch>();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (moved)
            canvas.drawPath(path, paint);
        for (Touch t : downs)
            canvas.drawCircle(t.getX(), t.getY(), 3, paint);


    }

    public boolean onTouchEvent(Touch t) {
        x = t.getX();
        y = t.getY();
        int type = t.getType();
        switch (type) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                downs.add(t);
                break;
            case MotionEvent.ACTION_MOVE:
                moved = true;
                path.lineTo(x, y);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void clear() {
        path = new Path();
        downs = new ArrayList<Touch>();
        invalidate();
        moved = false;
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        String touch = motionEvent.getX() + "," + motionEvent.getY() + "," + motionEvent.getEventTime() + "," + motionEvent.getPressure() + "," + motionEvent.getSize() + "," + motionEvent.getAction();
        final Touch t = new Touch(touch);
        if(log!=null && t!=null)
            log.addTouch(t);
        onTouchEvent(t);

        return true;
    }*/

    public void setLog(Logging log) {
        this.log = log;
    }
}
