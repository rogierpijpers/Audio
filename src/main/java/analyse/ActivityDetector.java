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
public class ActivityDetector {
    private boolean startCondition;
    private boolean stopCondition;
    private boolean measurementStarted;
    private int startValue;
    private int totalMeasurementLength;
    private int numberOfMeasurements;
    
    public ActivityDetector(){
        this.measurementStarted = false;
        this.totalMeasurementLength = 0;
        this.numberOfMeasurements = 0;
    }
    
    public boolean isStartCondition() {
        return startCondition;
    }

    public void setStartCondition(boolean startCondition) {
        this.startCondition = startCondition;
    }

    public boolean isStopCondition() {
        return stopCondition;
    }

    public void setStopCondition(boolean stopCondition) {
        this.stopCondition = stopCondition;
    }
    
    public void detect(int xAxis){
        if(startCondition && !measurementStarted){
            measurementStarted = true;
            startValue = xAxis;
            return;
        }
        
        if(stopCondition && measurementStarted){
            totalMeasurementLength += xAxis - startValue;
            numberOfMeasurements++;
            measurementStarted = false;
        }
    }
    
    public int getTotalMeasurementLength(){
        return totalMeasurementLength;
    }
    
    public int getNumberOfMeasurements(){
        return numberOfMeasurements;
    }
    
    public float getMeasurementPercentage(int total){
        return (totalMeasurementLength * 100.0f) / total;
    }
    
    public float getAverageMeasurementLength(){
        return ( totalMeasurementLength > 0 ) ? ( totalMeasurementLength / numberOfMeasurements ) : 0;
    }
    
}
