/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;

/**
 *
 * @author Rogier
 */
public class VADAnalysis {
    private static final int AMPLITUDE_THRESHOLD = 15;
    private ActivityDetector silenceDetector;
    private ActivityDetector speechDetector;
    private final Audio audio;
    private final int stepSize;
    
    public VADAnalysis(Audio audio){
        this.audio = audio;
        this.stepSize = audio.getSampleRate() / 100; // 10ms
    }
    
    public AnalysisResult analyse() {    
        silenceDetector = new ActivityDetector();
        speechDetector = new ActivityDetector();
        
        int timeInMs = 0;
        for (int i = 0; i < audio.getNumberOfSamples(); i += stepSize) {
            int endSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = audio.getMaxAmplitude(i, endSample);
            
            silenceDetector.setStartCondition(isSilent(amplitudeValue));
            silenceDetector.setStopCondition((!isSilent(amplitudeValue) || isLastIteration(i)));
            
            speechDetector.setStartCondition(!isSilent(amplitudeValue));
            speechDetector.setStopCondition((isSilent(amplitudeValue) || isLastIteration(i)));
            
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
}
