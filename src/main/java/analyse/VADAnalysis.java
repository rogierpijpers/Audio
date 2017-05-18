/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Rogier
 */
public class VADAnalysis {
    private int AMPLITUDE_THRESHOLD = 200; // 18 for Jenny;
    private ActivityDetector silenceDetector;
    private ActivityDetector speechDetector;
    private final Audio audio;
    private final int stepSize;
    
    public VADAnalysis(Audio audio){
        this.audio = audio;
        this.stepSize = audio.getSampleRate() / 100; // 10ms
        System.out.println("Stepsize: " + stepSize);
        AMPLITUDE_THRESHOLD = calculateThreshold();
        System.out.println(AMPLITUDE_THRESHOLD);
    }
    
    public AnalysisResult analyse() {    
        silenceDetector = new ActivityDetector();
        speechDetector = new ActivityDetector();
        
        int timeInMs = 0;
        for (int i = 0; i < audio.getNumberOfSamples(); i += stepSize) {
            int endSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = audio.getMaxAmplitude(i, endSample);
            
            silenceDetector.setStartCondition(isSilent(amplitudeValue));
            silenceDetector.setStopCondition(!isSilent(amplitudeValue) || isLastIteration(i));
            
            speechDetector.setStartCondition(!isSilent(amplitudeValue));
            speechDetector.setStopCondition(isSilent(amplitudeValue) || isLastIteration(i));
            
            silenceDetector.detect(timeInMs);
            speechDetector.detect(timeInMs);
            
            timeInMs += 10;
        }
        AnalysisResult result = new AnalysisResult(silenceDetector.getMeasurementPercentage(audio.getDurationInMilliSeconds()), "percent", toString());
        return result;
    }
    
    private boolean isLastIteration(int index){
        return ( index + stepSize ) >= audio.getNumberOfSamples();
    }
    
    private boolean isSilent(int amplitudeValue){
        return amplitudeValue < AMPLITUDE_THRESHOLD && amplitudeValue > -AMPLITUDE_THRESHOLD;
    }
    
    @Override
    public String toString() {
        String result = "";
        result += "\n--- Silence statistics --- \n";
        result += "Total time in ms: \t\t" + audio.getDurationInMilliSeconds() + "\n";
        result += "Total silence in ms: \t\t" + silenceDetector.getTotalMeasurementLength() + "\n";
        result += "Number of silences: \t\t" + silenceDetector.getNumberOfMeasurements() + "\n";
        result += "Silence percentage: \t\t" + silenceDetector.getMeasurementPercentage(audio.getDurationInMilliSeconds()) + "\n";
        result += "Average silence length: \t" + silenceDetector.getAverageMeasurementLength() + "\n";
        result += "Total length of speech: \t" + speechDetector.getTotalMeasurementLength() + "\n";
        result += "Number of words: \t\t" + speechDetector.getNumberOfMeasurements() + "\n";
        return result;
    }
    
    public List<Activity> getActivities(){
        List<Activity> silenceActivities = silenceDetector.getActivities();
        List<Activity> speechActivities = speechDetector.getActivities();
        
        silenceActivities.forEach(activity -> activity.setHighActivity(false));
        speechActivities.forEach(activity -> activity.setHighActivity(true));
        
        silenceActivities.addAll(speechActivities);
        Collections.sort(silenceActivities, (a1, a2) -> a1.getStart() - a2.getStart());
        return silenceActivities;
    }
    
    private int calculateThreshold(){
        int loopSize = 0;
        if(audio.getNumberOfSamples() > audio.getSampleRate() * 30){ // 30 seconds
            loopSize = audio.getNumberOfSamples();
        }else {
            loopSize = audio.getSampleRate() * 30;
        }
        
        List<Integer> amplitudeValues = new ArrayList<>();
        for (int i = 0; i < audio.getNumberOfSamples(); i += audio.getSampleRate() / 20) { // 50ms
            int endSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + stepSize;
            amplitudeValues.add(audio.getMaxAmplitude(i, endSample));
        }
        
        int lowestAmplitudeValue = Collections.min(amplitudeValues);
        int medianAmplitudeValue = 0;
        
        Collections.sort(amplitudeValues);
        double median = 0;
        if (amplitudeValues.size() > 0) {
            int x = amplitudeValues.size() / 2;
            if (amplitudeValues.size() % 2 == 0) 
                median = (amplitudeValues.get(x - 1) + amplitudeValues.get(x)) / 2.0;
            else median = amplitudeValues.get(x);
        }
        medianAmplitudeValue = (int) median;
        
        return (int) (medianAmplitudeValue - (0.8 * (medianAmplitudeValue - lowestAmplitudeValue)));
    }

}
