package assistive.com.brailleshapes.storage;

/**
 * Created by kyle montague on 10/05/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by kyle montague, and Hugo Nicolau (he sat behind me shouting that I was wrong the whole time.) on 10/11/2014.
 */
public class Logger {



    private static final String SUBTAG = "Logger: ";

    protected String mFolderName = Environment.getExternalStorageDirectory().getPath()+"/Traces";
    protected String mFilename = "";
    protected String mName;
    protected String mDeviceName ="WEAR";
    protected ArrayList<String> mData = null;


    private BroadcastReceiver mStorageReceiver = null;

    protected int mFlushThreshold = 1000;
    protected final int MIN_FLUSH_THRESHOLD = 50;


    public enum FileFormat{
        csv,
        txt,
        xml
    };

    public Logger(String name, int flushThreshold, long timestamp, FileFormat format) {
        // initialize variables
        mName = name;
        mFlushThreshold = Math.max(MIN_FLUSH_THRESHOLD, flushThreshold);
        mData = new ArrayList<String>();
        setFileInfo(timestamp,format);
        mDeviceName = BluetoothAdapter.getDefaultAdapter().getAddress();
    }


    public String getFilename(){
        return mFilename;
    }


    public void setFileInfo(long timestamp, FileFormat format){
        mFolderName = mFolderName+"/"+timestamp+"/";
        mFilename = mFolderName+mDeviceName+"_"+mName+"_"+timestamp+"."+format;
    }

    public void flush(){
        //Log.v(BaseLogger.TAG, SUBTAG + "Flush - "+mData.size()+" file: "+mFilename);
        DataWriter w = new DataWriter(mData, mFolderName, mFilename, false);
        w.execute();
        mData = new ArrayList<String>();
    }

    public void writeAsync(String data){
        mData.add(data);
        if(mData.size() >= mFlushThreshold)
            flush();
    }

    public void writeSync(ArrayList<String> data){
        mData.addAll(data);
        flush();
    }

    public void writeFile(File file){
        String name = file.getName();
        file.renameTo(new File(mFolderName + "/" + name));
    }

    public String parentDirectory(){
        return mFolderName;
    }
}
