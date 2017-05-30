/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import util.ArrayUtilities;

/**
 *
 * @author Rogier
 */
public class FrequencySpectrum {
    private int WINDOW_SIZE = 1024;
    private final int sampleRate;
    
    public FrequencySpectrum(int sampleRate, int windowSize){
        this.sampleRate = sampleRate;
        this.WINDOW_SIZE = getLargestPowerOfTwo(windowSize);
    }
    
    public FrequencySpectrum(int sampleRate){
        this.sampleRate = sampleRate;
    }
    
    public Map<Double, Double> getSpectrum(int[] input){
        return getSpectrum(ArrayUtilities.toDoubleArray(input));
    }
    
    public Map<Double, Double> getSpectrum(double[] input){
        return getSpectrum(input, 20, 20000);
    }
    
    public Map<Double, Double> getSpectrum(int[] input, int lowPassFilter, int highPassFilter){
        return getSpectrum(ArrayUtilities.toDoubleArray(input), lowPassFilter, highPassFilter);
    }

    public Map<Double, Double> getSpectrum(double[] input, int lowPassFilter, int highPassFilter){
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] cmplx = transformer.transform(input, TransformType.FORWARD);

        Map<Double, Double> frequencySpectrum = new LinkedHashMap<>();
        
        // complx.length is devided by two because we most defenitly don't need frequencies higher than 20.000 Hz
        for(int i = 1; i < cmplx.length / 2; i++){
            double real = cmplx[i].getReal();
            double im = cmplx[i].getImaginary();
            
            double frequency = (sampleRate * i) / WINDOW_SIZE;
            double magnitude = Math.sqrt((real * real) + (im * im)); 
            
            if(frequency >= lowPassFilter && frequency <= highPassFilter)
                frequencySpectrum.put(frequency, magnitude);
        }
        return frequencySpectrum;
    }
    
    public double getDominantFrequency(int[] input){
        return getDominantFrequency(ArrayUtilities.toDoubleArray(input));
    }
    
    public double getDominantFrequency(double[] input){
        Map<Double, Double> spectrum = getSpectrum(input);
        
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
    
    private static int getLargestPowerOfTwo(int input){
        int result = 2;        
        while (result < input) {
                result *= 2;
        }
        if(result > input)
            result /= 2;
        
        return result;
    }
}
