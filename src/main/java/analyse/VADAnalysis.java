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
    
    public void analyse(Audio audio){
        int numberOfSilences = 0;
        int totalLengthOfSilence = 0;
        
        boolean silenceStarted = false;
        int startSilence = 0;
        int stopSilence = 0;
        int timeInMs = 0;
        for (int i = 0; i < audio.getNumberOfSamples(); i += (audio.getNumberOfSamples() / audio.getDurationInMilliSeconds())) {
            int amplitudeValue = audio.getAmplitude(i);
        
            boolean isSilent = amplitudeValue < AMPLITUDE_THRESHOLD && amplitudeValue > -AMPLITUDE_THRESHOLD;
            if(isSilent && !silenceStarted){
                startSilence = timeInMs;
                silenceStarted = true;
            }
            
            boolean lastIteration = i + (audio.getNumberOfSamples() / audio.getDurationInMilliSeconds()) >= audio.getNumberOfSamples();
            if((!isSilent || lastIteration) && silenceStarted){
                stopSilence = timeInMs;
                if(stopSilence - startSilence > TIME_THRESHOLD_MS || lastIteration){
                    totalLengthOfSilence += stopSilence - startSilence;
                    numberOfSilences++;
                }
                silenceStarted = false;
            }   
            timeInMs++;
        }
        
        System.out.println("\n--- Silence statistics ---");
        System.out.println("Total time in ms: \t" + audio.getDurationInMilliSeconds());
        System.out.println("Total silence in ms: \t" + totalLengthOfSilence);
        System.out.println("Number of silences: \t" + numberOfSilences);
        System.out.println("Silence percentage: \t" + (totalLengthOfSilence * 100.0f) / audio.getDurationInMilliSeconds());
    }
}
