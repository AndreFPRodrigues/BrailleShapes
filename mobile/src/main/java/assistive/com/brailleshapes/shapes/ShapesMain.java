package assistive.com.brailleshapes.shapes;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Locale;

import assistive.com.brailleshapes.Logging;
import assistive.com.brailleshapes.R;
import assistive.com.brailleshapes.Touch;


public class ShapesMain extends ActionBarActivity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, NodeApi.NodeListener, GoogleApiClient.OnConnectionFailedListener{
    /**
     * Request code for launching the Intent to resolve Google Play services errors.
     */
    private static final int REQUEST_RESOLVE_ERROR = 1000;
    private final String TOUCH = "touch";
    private final String TAG = "Brailler";
    private Handler mHandler;
    private GoogleApiClient mGoogleApiClient;
    private boolean recording;
    private boolean mResolvingError = false;
    private DrawView dv;
    private TextView td;
    private Logging log;
    private StudyController stdc;
    private String username;
    private TextToSpeech ttobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getAction();

        mHandler = new Handler();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        startTraining();
        stdc= new StudyController();

        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            ttobj.setLanguage(Locale.getDefault());
                        }
                    }
                });
    }

    private void startTraining() {
        setContentView(R.layout.activity_main);
        dv = (DrawView) findViewById(R.id.single_touch_view);
        td = (TextView) findViewById(R.id.toDraw);
        log = new Logging(username + "_training");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (!mResolvingError) {
            // Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override //ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        Log.d("teste", "CONnECT  MESSAGE");

        mResolvingError = false;
        //   Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override //OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            mResolvingError = false;
            //Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        final String message = new String(messageEvent.getData());
        if (messageEvent.getPath().contains(TOUCH)) {
            final Touch t = new Touch(message);
            log.addTouch(t);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    dv.onTouchEvent(t);
                }
            });
        }
    }

    //Start recording Process
    public void start(View v){
        stdc.setRepetitions(Integer.parseInt(((TextView) findViewById(R.id.numberOfTrials)).getText().toString()));
        setContentView(R.layout.recording);
        td = (TextView) findViewById(R.id.toDraw);
        clear(null);
        next(null);
        log.setUsername(username);
        dv = (DrawView) findViewById(R.id.single_touch_view);
        dv.setLog(log);

    }

    //Next letter Process
    public void next(View v){
        log.writeToFile();
        final String letter= stdc.getNextLetter();
        if(letter!=null) {
            String toRead=letter;
            if(toRead.equals("Q"))
                toRead="Que";
            ttobj.speak(toRead, TextToSpeech.QUEUE_FLUSH, null);
            log.setLetter(letter);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    td.setText(letter);
                }
            });
            clear(null);
        }else{
            log.closeFile();
            startTraining();
        }
    }

    //Handles the toogle buttons recording
    public void write(View v) {
        final String key = (String) v.getTag();
        if (recording) {
            log.writeToFile();
        }
        recording = true;
        log.setLetter(key);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                td.setText(key);
            }
        });
        clear(null);

    }

    public void clear(View v) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                dv.clear();
            }
        });
        log.clear();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPeerConnected(Node node) {

    }

    @Override
    public void onPeerDisconnected(Node node) {

    }


}