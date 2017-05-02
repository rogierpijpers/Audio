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
    private static final int TIME_THRESHOLD_MS = 100;
    private final Audio audio;
    private final int numberOfSamples;
    private final int durationInMilliSeconds;
    private int totalLengthOfSilence;
    private int numberOfSilences;
    
    public VADAnalysis(Audio audio){
        this.audio = audio;
        this.totalLengthOfSilence = 0;
        this.numberOfSilences = 0;
        this.numberOfSamples = audio.getNumberOfSamples();
        this.durationInMilliSeconds = audio.getDurationInMilliSeconds();
    }
    
    public AnalysisResult analyse() {
        boolean silenceStarted = false;
        boolean lastIteration;
        int startSilence = 0;
        int timeInMs = 0;

        for (int i = 0; i < numberOfSamples; i += (numberOfSamples / durationInMilliSeconds)) {
            int amplitudeValue = audio.getAmplitude(i);
            boolean isSilent = amplitudeValue < AMPLITUDE_THRESHOLD && amplitudeValue > -AMPLITUDE_THRESHOLD;
            lastIteration = i + (numberOfSamples / durationInMilliSeconds) >= numberOfSamples;

            if (isSilent && !silenceStarted) {
                startSilence = timeInMs;
                silenceStarted = true;
            }

            if ((!isSilent || lastIteration) && silenceStarted) {
                int stopSilence = timeInMs;
                if (stopSilence - startSilence > TIME_THRESHOLD_MS) {
                    totalLengthOfSilence += stopSilence - startSilence;
                    numberOfSilences++;
                }
                silenceStarted = false;
            }

            timeInMs++;
        }
        AnalysisResult result = new AnalysisResult(getSilencePercentage(), "percent", toString());
        return result;
    }
    
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
