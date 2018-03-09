package assistive.com.brailleshapes.shapes;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by andre on 03-Jun-15.
 */
public class StudyController {
    private ArrayList<String> randomLetters;
    private Random generator;
    private int repetitions;
    private final String TAG = "Brailler";

    public StudyController() {
        randomLetters = new ArrayList<String>();
        generateRandomLetterSequence();
        generator= new Random();
        repetitions=0;
    }

    private void generateRandomLetterSequence() {
        for(int i=65;i<91;i++){

            Log.d(TAG,new String (Character.toChars(i)) );

            randomLetters.add(new String (Character.toChars(i)));
        }
    }

    public String getNextLetter(){
        int size= randomLetters.size();
        if(size==0) {
            repetitions--;
            generateRandomLetterSequence();
            size= randomLetters.size();
            if(repetitions<1)
                return null;
        }
        int i = generator.nextInt(size);
        String letter = randomLetters.get(i);
        randomLetters.remove(i);
        return letter;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
