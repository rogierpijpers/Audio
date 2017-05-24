/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author Rogier
 */
public class FrequencyWrapper {
    private int WINDOW_SIZE = 1024;
    private int sampleRate;
    
    public FrequencyWrapper(int sampleRate, int windowSize){
        this.sampleRate = sampleRate;
        this.WINDOW_SIZE = windowSize;
    }
    
    public FrequencyWrapper(int sampleRate){
        this.sampleRate = sampleRate;
    }
    
    public Map<Double, Double> getFrequencySpectrum(int[] input){
        return getFrequencySpectrum(toDoubleArray(input));
    }
    
    public Map<Double, Double> getFrequencySpectrum(double[] input){
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] cmplx = transformer.transform(input, TransformType.FORWARD);

        Map<Double, Double> frequencySpectrum = new HashMap<>();
        
        // gets frequency spectrum between bands of 86 Hz and 22.006 Hz. Using frequency bands of 42 Hz
        for(int i = 2; i < cmplx.length / 2; i++){
            double real = cmplx[i].getReal();
            double im = cmplx[i].getImaginary();
            
            double frequency = (sampleRate * i) / WINDOW_SIZE;
            double magnitude = Math.sqrt((real * real) + (im * im)); 
            
            frequencySpectrum.put(frequency, magnitude);
        }
        return frequencySpectrum;
    }
    
    public double getDominantFrequency(int[] input){
        return getDominantFrequency(toDoubleArray(input));
    }
    
    public double getDominantFrequency(double[] input){
        Map<Double, Double> spectrum = getFrequencySpectrum(input);
        
        Map.Entry<Double, Double> maxEntry = null;
        for(Map.Entry<Double, Double> entry : spectrum.entrySet()){
            if((maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0))
                maxEntry = entry;
        }
        return maxEntry.getKey();
    }
    
    public int getWindowSize(){
        return WINDOW_SIZE;
    }
    
    private double[] toDoubleArray(int[] amplitudes){
        double[] result = new double[amplitudes.length];
        for(int i = 0; i < amplitudes.length; i++){
            result[i] = amplitudes[i];
        }
        return result;
    }
}
