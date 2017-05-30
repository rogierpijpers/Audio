/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import audio.Audio;
import util.ArrayUtilities;

/**
 *
 * @author Rogier
 */
class Threshold {
    private final Audio audio;
    private final int lowPassFilter;
    private final int highPassFilter;
    private final int windowSize;
    
    private double thresholdFactor = 0.8;
    
    private final int thresholdValue;
    
    public Threshold(Audio audio, int windowSize, int lowPassFilter, int highPassFilter){
        this.audio = audio;
        this.windowSize = windowSize;
        this.lowPassFilter = lowPassFilter;
        this.highPassFilter = highPassFilter;
        
        this.thresholdValue = calculateThreshold();
    }
    
    public int getValue(){
        return thresholdValue;
    }
    
    public void setThresholdFactor(double factor){
        this.thresholdFactor = factor;
    }
    
    private int calculateThreshold(){
        // loop over 30 seconds, as long as it fits.
        int loopSize = (audio.getNumberOfSamples() < audio.getSampleRate() * 30) ? audio.getNumberOfSamples() : audio.getSampleRate() * 30;

        List<Double> amplitudeValues = new ArrayList<>();
        
        // loop over windows of 50ms
        for (int i = 0; i < loopSize; i += audio.getSampleRate() / 20) {
            int freqWindowSize = (( i + windowSize ) >= audio.getNumberOfSamples()) ? audio.getNumberOfSamples() - i : windowSize;
 
            FrequencySpectrum frequencyWrapper = new FrequencySpectrum(audio.getSampleRate(), freqWindowSize);
            int[] input = audio.getAmplitudeWindow(i, frequencyWrapper.getWindowSize());
            
            Map<Double, Double> spectrum = frequencyWrapper.getSpectrum(input, lowPassFilter, highPassFilter);
            
            List<Double> avg = new ArrayList<>();
            
            spectrum.entrySet().forEach((entry) -> {
                avg.add(entry.getValue());
            });
            amplitudeValues.add(ArrayUtilities.getAverage(avg));
        }
        
        double lowestAmplitudeValue = Collections.min(amplitudeValues);
        double averageAmplitudeValue = ArrayUtilities.getAverage(amplitudeValues);
        //double medianAmplitudeValue = getMedianAmplitude(amplitudeValues);
        
        return (int) (averageAmplitudeValue - (thresholdFactor * (averageAmplitudeValue - lowestAmplitudeValue)));
    }

}
