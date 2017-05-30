/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rogier
 */
class VADAnalysisProperties {
    private final boolean[] activity;
    private final Audio audio;
    private final int windowSize;
    
    public VADAnalysisProperties(boolean[] activity, Audio audio, int windowSize){
        this.activity = activity;
        this.audio = audio;
        this.windowSize = windowSize;
    }
    
    public Map<String, Double> toMap(){
        Map<String, Double> map = new HashMap<>();
        map.put("total_length", (double) audio.getDurationInMilliSeconds());
        map.put("silence_percentage", getSilencePercentage());
        map.put("total_silence_length", getTotalLengthOfSilence());
        map.put("total_speech_length", getTotalLengthOfSpeech());
        map.put("total_silence_count", (double) getTotalNumberOfSilences());
        map.put("total_speech_count", (double) getTotalNumberOfWords());
        map.put("average_silence_length", getAverageSilenceLength());  
        return map;
    }
    
    public double getSilencePercentage(){
        return (getTotalLengthOfSilence() * 100.0f) / audio.getDurationInMilliSeconds();
    }
    
    public double getTotalLengthOfSilence(){
        return getNumberOfBoolValues(activity, false) * (audio.getSampleRate() / (windowSize * 2) * 0.1);
    }
    
    public double getTotalLengthOfSpeech(){
        return getNumberOfBoolValues(activity, true) * (audio.getSampleRate() / (windowSize * 2) * 0.1);
    }
    
    public int getTotalNumberOfSilences(){
        return getFollowingNumberOfBoolValues(activity, false);
    }
    
    public int getTotalNumberOfWords(){
        return getFollowingNumberOfBoolValues(activity, true);
    }
    
    public double getAverageSilenceLength(){
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
    
    
    // POSSIBLE BUG!! silences from > 1000 ms are (probably) also counted as > 50ms
    public int getSilencesLongerThanMilliSeconds(int numberOfMs){
        int numberOfValues = 0;
        boolean start = false;
        int tmpMeasuredValues = 0;
        for(int i = 0; i < activity.length; i++){
            if(!activity[i])
                start = true;
            
            if(start)
                tmpMeasuredValues++;
            
            if((activity[i] && start) || (!activity[i] && i == activity.length - 1)){
                start = !start;
                if(tmpMeasuredValues > numberOfMs * 2){
                    numberOfValues++;
                    tmpMeasuredValues = 0;
                }
            }
        }
        return numberOfValues;
    }
    
    public double calculateRMS(double[] values){
        double x = 0;
        for(int i = 0; i < values.length; i++){
            x += Math.pow(values[i], 2);
        }
        x /= values.length;
        return Math.sqrt(x);
    }
}
