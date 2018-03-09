package assistive.com.brailleshapes;

/**
 * Created by andre on 28-May-15.
 */
public class Touch implements Comparable<Touch> {
    private float x;
    private float y;
    private long time;
    private float pressure;
    private float size;
    private int type;

public Touch(String s){
    String [] split =s.split(",");
    x=Float.parseFloat(split[0]);
    y=Float.parseFloat(split[1]);
    time=Long.parseLong(split[2]);
    pressure=Float.parseFloat(split[3]);
    size=Float.parseFloat(split[4]);
    type=Integer.parseInt(split[5]);
}

    public String toJSON(){
        return "{\"x\":"+x+" , \"y\":"+y+", \"time\":"+time+", \"pressure\":"+pressure+" ,\"size\":"+size+", \"type\":"+type+"}";
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public long getTime() {
        return time;
    }

    public float getPressure() {
        return pressure;
    }

    public float getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    @Override
    public int compareTo(Touch t) {

        long timestamp = ((Touch) t).getTime();

        //ascending order
        return (int) (this.time - timestamp);



    }
}
