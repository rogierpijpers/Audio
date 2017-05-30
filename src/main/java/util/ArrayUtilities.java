/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Rogier
 */
public class ArrayUtilities {
    public static double getMedian(List<Double> amplitudeValues) {
        Collections.sort(amplitudeValues);
        double median = 0;
        int ampsMiddle = amplitudeValues.size() / 2;
        if (amplitudeValues.size() % 2 == 0){
            median = (amplitudeValues.get(ampsMiddle - 1) + amplitudeValues.get(ampsMiddle)) / 2.0;
        }else {
            median = amplitudeValues.get(ampsMiddle);
        }
        return median;
    }
    
    public static double getAverage(List <Double> marks) {
        double sum = 0;
        if(!marks.isEmpty()) {
          for (double mark : marks) {
              sum += mark;
          }
          return sum / marks.size();
        }
        return sum;
    }
    
    public static double[] toDoubleArray(int[] amplitudes){
        double[] result = new double[amplitudes.length];
        for(int i = 0; i < amplitudes.length; i++){
            result[i] = amplitudes[i];
        }
        return result;
    }
}
