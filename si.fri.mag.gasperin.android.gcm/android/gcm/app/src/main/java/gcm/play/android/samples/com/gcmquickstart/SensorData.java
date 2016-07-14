package gcm.play.android.samples.com.gcmquickstart;

/**
 * Created by Nejc on 23.3.2016.
 */
public class SensorData {

    public String x;
    public String y;
    public String z;
    public String systolic;
    public String diastolic;
    public String title;

    public SensorData(String title, String systolic, String diastolic, String x, String y, String z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.title = title;
    }
}
