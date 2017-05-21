/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

/**
 *
 * @author Rogier
 */
@Deprecated
public class Activity {
    private int start;
    private int stop;
    private boolean highActivity;

    public Activity(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }
    
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }
    
    public boolean isHighActivity() {
        return highActivity;
    }

    public void setHighActivity(boolean highActivity) {
        this.highActivity = highActivity;
    }
    
    @Override
    public String toString(){
        String type = highActivity ? "High" : "Low";
        return "Start:\t" + start + "\tStop:\t" + stop + "\tType:\t" + type;
    }
    
}
