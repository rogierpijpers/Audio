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
    private static final int AMPLITUDE_THRESHOLD = 100;
    private final Audio audio;
    private final int numberOfSamples;
    private final int durationInMilliSeconds;
    private int totalLengthOfSilence;
    private int numberOfSilences;
    private final int stepSize;
    
    public VADAnalysis(Audio audio){
        this.audio = audio;
        this.totalLengthOfSilence = 0;
        this.numberOfSilences = 0;
        this.numberOfSamples = audio.getNumberOfSamples();
        this.durationInMilliSeconds = audio.getDurationInMilliSeconds();
        this.stepSize = 441;//(numberOfSamples / durationInMilliSeconds); // 1ms
    }
    
    public AnalysisResult analyse() {
        boolean silenceStarted = false;
        int startSilence = 0;
        int timeInMs = 0;
        
        for (int i = 0; i < numberOfSamples; i += stepSize) {
            // create envelope
            int endSample = isLastIteration(i) ? audio.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = audio.getMaxAmplitude(i, endSample);
            
            if (isSilent(amplitudeValue) && !silenceStarted) {
                startSilence = timeInMs;
                silenceStarted = true;
            }

            if ((!isSilent(amplitudeValue) || isLastIteration(i)) && silenceStarted) {
                int stopSilence = timeInMs;
                totalLengthOfSilence += stopSilence - startSilence;
                numberOfSilences++;
                silenceStarted = false;
            }
            timeInMs += 10;
        }
        AnalysisResult result = new AnalysisResult(getSilencePercentage(), "percent", toString());
        return result;
    }
    
    private boolean isLastIteration(int index){
        return ( index + stepSize ) >= numberOfSamples;
    }
    
    private boolean isSilent(int amplitudeValue){
        return amplitudeValue < AMPLITUDE_THRESHOLD && amplitudeValue > -AMPLITUDE_THRESHOLD;
    }
    
    // following getters can be used after analysis
    
    public float getSilencePercentage(){
        return (totalLengthOfSilence * 100.0f) / audio.getDurationInMilliSeconds();
    }
    
    public float getAverageSilenceLength(){
        return ( totalLengthOfSilence > 0 ) ? ( totalLengthOfSilence / numberOfSilences ) : 0;
    }
    
    @Override
    public String toString() {
        String result = "";
        result += "\n--- Silence statistics --- \n";
        result += "Total time in ms: \t\t" + audio.getDurationInMilliSeconds() + "\n";
        result += "Total silence in ms: \t\t" + totalLengthOfSilence + "\n";
        result += "Number of silences: \t\t" + numberOfSilences + "\n";
        result += "Silence percentage: \t\t" + getSilencePercentage() + "\n";
        result += "Average silence length: \t" + getAverageSilenceLength() + "\n";
        return result;
    }
}
