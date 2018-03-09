package assistive.com.brailleshapes.storage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by hugonicolau on 13/11/2014.
 *
 * Writes data to log file
 */

public class DataWriter extends AsyncTask<Void, Void, Void> {

    private static final  String TAG = "DataWriter";

    private ArrayList<String> mData;
    private String mFilePath;
    private String mFolderPath;
    private boolean mIsSyncWriting;

    public DataWriter(ArrayList<String> data, String folderPath, String filePath, boolean sync) {
        mData = data;
        mFilePath = filePath;
        mFolderPath = folderPath;
        mIsSyncWriting = sync;

        if (mIsSyncWriting) {

            if (mData != null && !mData.isEmpty()) {

                // creates file
                writeFile();
            }
        }
    }

    private void writeFile(){

        // creates folder
        File folder = new File(mFolderPath);
        if (!folder.exists())
            folder.mkdirs();

        File file = new File(mFilePath);
        FileWriter fw;

        try {
            fw = new FileWriter(file, true);
            for(String line : mData){
                fw.write(line + "\n");
            }
            fw.flush();
            fw.close();
            Log.v(TAG,mData.size() + " Data write ok: " + file.getAbsolutePath());
            mData = new ArrayList<String>();


        } catch (IOException e) {
            Log.v(TAG,"Data write BROKEN: " + file.getAbsolutePath() + " " + e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mIsSyncWriting)
            return null;

        if (mData != null && !mData.isEmpty()) {
            // creates file
            writeFile();
        }

        return null;
    }


}
