package assistive.com.brailleshapes;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import assistive.com.brailleshapes.R;
import assistive.com.brailleshapes.StudyListener;


public class Shapes extends Activity {

    private final String TOUCH = "touch";
    private StudyListener sl;
    private LinearLayout container;
    private String touch;
    private final String TAG="Brailler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sl = new StudyListener();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                container = (LinearLayout) stub.findViewById(R.id.ll0);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //Log.d(TAG, motionEvent.getX() + " " + motionEvent.getY());
                            touch =  motionEvent.getX() +","+ motionEvent.getY() + ","+motionEvent.getEventTime()+","+motionEvent.getPressure()+","+motionEvent.getSize()+","+motionEvent.getAction() ;
                            sl.sendMessage(getApplicationContext(), TOUCH, touch);

                        return true;
                    }


                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
