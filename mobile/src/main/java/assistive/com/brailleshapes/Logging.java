package assistive.com.brailleshapes;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import assistive.com.brailleshapes.shapes.Letter;
import assistive.com.brailleshapes.shapes.Stroke;

/**
 * Created by andre on 03-Jun-15.
 */
public class Logging {

    /* private static final String INIT_FILE = "{\"Letters\":[";
     private static final String INIT_SEQUENCE2 = "\" , \"touches\":[";
     private final String INIT_SEQUENCE = "{\"Letter\":\"";
     private final String END_SEQUENCE = "]},";
     private final String filepath = Environment.getExternalStorageDirectory().toString()+"/";
     private final String filepathEnd ="_braille.json";*/
    private final String TAG = "Brailler";


    private final String filepath = Environment.getExternalStorageDirectory().toString() + "/";
    private final String filepathEnd = "_braille.xml";

    //String letter;
    //private ArrayList<Touch> touches;
    Letter letter;
    private long endDraw = 0;
    private long startDraw = 0;
    private String username;

    public Logging(String name) {
        //touches = new ArrayList<Touch>();
        // letter = "";
        username = name;
    }

    public void addTouch(Touch t) {
        if (letter != null) {
            letter.addTouch(t);
        }
        //touches.add(t);
    }

    public void setLetter(String character) {
        //this.letter = letter;
        this.letter = new Letter(character);
        startDraw = System.currentTimeMillis();

    }


/*
    public void writeToFile() {
        if(touches.size()<1){
            return ;
        }
        endDraw = System.currentTimeMillis();

        File file = new File(filepath+username+filepathEnd);
        boolean exists = file.exists();
        FileWriter fw;
        try {
            fw = new FileWriter(file, true);
            if (!exists) {
                fw.write(INIT_FILE);
            }
            fw.write(INIT_SEQUENCE + letter + INIT_SEQUENCE2);
            boolean first = true;
            for (Touch t : touches) {
                if (first) {
                    fw.write(t.toJSON());
                    first = false;
                } else {
                    fw.write(" , " + t.toJSON());
                }
            }
            fw.write(END_SEQUENCE);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        touches = new ArrayList<Touch>();
    }*/

    public void writeToFile() {
        if (letter == null || letter.getSize() < 1) {
            return;
        }
        endDraw = System.currentTimeMillis();

        File file = new File(filepath + username + filepathEnd);
        boolean exists = file.exists();
        FileWriter fw;
        try {
            fw = new FileWriter(file, true);
            String toWrite="";
            if (!exists) {
                toWrite = initXML();
            }
            toWrite += writeLetterXml();


            fw.write(toWrite);

            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String initXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "MCALI");

            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String writeLetterXml() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);

            ArrayList<Stroke> strokes = letter.getStrokes();
            serializer.startTag("", "Gesture");
            serializer.attribute("", "Name", letter.getLetter());
            serializer.attribute("", "NumStrokes", strokes.size() + "");
            serializer.attribute("", "NumPts", letter.getSize() + "");

            int i = 0;
            for (Stroke stroke : strokes) {
                Log.d(TAG, "touch:" + i);

                i++;
                serializer.startTag("", "Stroke");
                serializer.attribute("", "ID", i + "");
                ArrayList<Touch> points = stroke.getPoints();
                for (Touch point : points) {

                    serializer.startTag("", "Point");
                    serializer.attribute("", "X", point.getX() + "");
                    serializer.attribute("", "Y", point.getY() + "");
                    serializer.attribute("", "MS", point.getTime() + "");

                    serializer.endTag("", "Point");
                }
                serializer.endTag("", "Stroke");
            }


            serializer.endTag("", "Gesture");

            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        //touches = new ArrayList<Touch>();
        if (letter != null)
            letter.clear();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void closeFile() {
        try {
            File file = new File(filepath + username + filepathEnd);
            FileWriter fw;

            fw = new FileWriter(file, true);
            String toWrite;
            fw.write("</MCALI>");
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
