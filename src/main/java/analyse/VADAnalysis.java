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

/**
 *
 * @author Rogier
 */
public class VADAnalysis {
    private final int AMPLITUDE_THRESHOLD;
    private final Audio audio;
    private final int windowSize;
    private final boolean[] activity;
    
    public VADAnalysis(Audio audio){
        this.audio = audio;
        this.windowSize = audio.getSampleRate() / 100; // 10ms
        AMPLITUDE_THRESHOLD = calculateThreshold();
        this.activity = new boolean[(audio.getNumberOfSamples() / (windowSize / 2)) + 1];
    }
    
    public AnalysisResult analyse() {    
        int silenceIndex = 0;
        for (int i = 0; i < audio.getNumberOfSamples(); i += (windowSize / 2)) {
            int lastWindowSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + windowSize;
            int envelopeAmplitude = audio.getMaxAmplitude(i, lastWindowSample);
            
            activity[silenceIndex] = !isSilent(envelopeAmplitude);
            
            silenceIndex++;
        }
        AnalysisResult result = new AnalysisResult(getSilencePercentage(), "percent", toString());
        return result;
    }
    
    private boolean isLastIteration(int index){
        return ( index + windowSize ) >= audio.getNumberOfSamples();
    }
    
    private boolean isSilent(int amplitudeValue){
        return amplitudeValue < AMPLITUDE_THRESHOLD & amplitudeValue > -AMPLITUDE_THRESHOLD;
    }
    
    @Override
    public String toString() {
        String result = "";
        result += "\n--- Silence statistics --- \n";
        result += "Total time in ms: \t\t" + audio.getDurationInMilliSeconds() + "\n";
        result += "Total silence in ms: \t\t" + getTotalLengthOfSilence() + "\n";
        result += "Number of silences: \t\t" + getTotalNumberOfSilences() + "\n";
        result += "Silence percentage: \t\t" + getSilencePercentage() + "\n";
        result += "Average silence length: \t" + getAverageSilenceLength() + "\n";
        result += "Total length of speech: \t" + getTotalLengthOfSpeech() + "\n";
        result += "Number of words: \t\t" + getTotalNumberOfWords() + "\n";
        return result;
    }
    
    private int calculateThreshold(){
        int loopSize = 0;
        if(audio.getNumberOfSamples() < audio.getSampleRate() * 30){ // 30 seconds
            loopSize = audio.getNumberOfSamples();
        }else {
            loopSize = audio.getSampleRate() * 30;
        }

        List<Integer> amplitudeValues = new ArrayList<>();
        for (int i = 0; i < loopSize; i += audio.getSampleRate() / 20) { // 50ms
            int endSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + windowSize;
            amplitudeValues.add(audio.getMaxAmplitude(i, endSample));
        }
        
        int lowestAmplitudeValue = Collections.min(amplitudeValues);
        int medianAmplitudeValue = getMedianAmplitude(amplitudeValues);
        
        return (int) (medianAmplitudeValue - (0.8 * (medianAmplitudeValue - lowestAmplitudeValue)));
    }

    private int getMedianAmplitude(List<Integer> amplitudeValues) {
        Collections.sort(amplitudeValues);
        double median = 0;
        int ampsMiddle = amplitudeValues.size() / 2;
        if (amplitudeValues.size() % 2 == 0){
            median = (amplitudeValues.get(ampsMiddle - 1) + amplitudeValues.get(ampsMiddle)) / 2.0;
        }else {
            median = amplitudeValues.get(ampsMiddle);
        }
        return (int) median;
    }
    
    public boolean[] getActivity(){
        return activity;
    }
    
    private double getSilencePercentage(){
        return (getTotalLengthOfSilence() * 100.0f) / audio.getDurationInMilliSeconds();
    }
    
    private double getTotalLengthOfSilence(){
        return getNumberOfBoolValues(activity, false) * (audio.getSampleRate() / (windowSize * 2) * 0.1);
    }
    
    private double getTotalLengthOfSpeech(){
        return getNumberOfBoolValues(activity, true) * (audio.getSampleRate() / (windowSize * 2) * 0.1);
    }
    
    private int getTotalNumberOfSilences(){
        return getFollowingNumberOfBoolValues(activity, false);
    }
    
    private int getTotalNumberOfWords(){
        return getFollowingNumberOfBoolValues(activity, true);
    }
    
    private double getAverageSilenceLength(){
        return ( getTotalLengthOfSilence() > 0 ) ? ( getTotalLengthOfSilence() / getTotalNumberOfSilences() ) : 0;
    }
    
    private int getNumberOfBoolValues(boolean[] values, boolean expected){
        int numberOfValues = 0;
        for(int i = 0; i < values.length; i++){
            if(values[i] == expected)
                numberOfValues++;
        }
        return numberOfValues;
    }
    
    private int getFollowingNumberOfBoolValues(boolean[] values, boolean expected){
        int numberOfValues = 0;
        boolean start = false;
        for(int i = 0; i < values.length; i++){
            if(values[i] == expected)
                start = true;
            
            if((values[i] != expected && start) || (values[i] == expected && i == values.length - 1)){
                numberOfValues++;
                start = !start;
            }
        }
        return numberOfValues;
    }
}
