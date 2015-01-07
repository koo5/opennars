package nars.io.meter;

import java.io.Serializable;
import nars.io.meter.event.ValueMeter;

/** emotional value; self-felt internal mental states; variables used to record emotional values */
public class EmotionMeter implements Serializable {

    /** average desire-value */
    private float happy;
    /** average priority */
    private float busy;

    public final ValueMeter happyMeter = new ValueMeter("happy");
    public final ValueMeter busyMeter = new ValueMeter("busy");
    
    
    public EmotionMeter() {
    }

    public EmotionMeter(float happy, float busy) {
        set(happy, busy);
    }

    public void set(float happy, float busy) {
        this.happy = happy;
        this.busy = busy;
        update();
    }

    public float happy() {
        return happy;
    }

    public float busy() {
        return busy;
    }

    public void adjustHappy(float newValue, float weight) {
        //        float oldV = happyValue;
        happy += newValue * weight;
        happy /= 1.0f + weight;
        //        if (Math.abs(oldV - happyValue) > 0.1) {
        //            Record.append("HAPPY: " + (int) (oldV*10.0) + " to " + (int) (happyValue*10.0) + "\n");
        update();
    }

    public void adjustBusy(float newValue, float weight) {
        //        float oldV = busyValue;
        busy += newValue * weight;
        busy /= (1.0f + weight);
        //        if (Math.abs(oldV - busyValue) > 0.1) {
        //            Record.append("BUSY: " + (int) (oldV*10.0) + " to " + (int) (busyValue*10.0) + "\n");
        update();
    }
    
    protected void update() {
        happyMeter.set(happy);
        busyMeter.set(busy);
    }
}
