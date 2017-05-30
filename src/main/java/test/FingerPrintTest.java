/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import analyse.FrequencySpectrum;
import audio.Audio;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rogier
 *  using 80Hz until 3.4 kHz
 */
public class FingerPrintTest {
    
    public static class Tuple<X, Y> { 
        private X x; 
        private Y y; 
        public Tuple(X x, Y y) { 
          this.x = x; 
          this.y = y; 
        } 
        public void setX(X x){
            this.x = x;
        }
        
        public X getX(){
            return x;
        }
        
        public void setY(Y y){
            this.y = y;
        }
        
        public Y getY(){
            return y;
        }
    } 
    
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException{
        List<Long> distinctVoices = new ArrayList<>();
        
        File file = new File("resources/ftdl_ppa_sande_spontansprache_gegenwart_ui.wav");
        Audio wav = new Audio(file);
        
        int frequencyWindowSize = 4096 * 8; //1048576;
        FrequencySpectrum frequencyWrapper = new FrequencySpectrum(wav.getSampleRate(), frequencyWindowSize);
        
        int stepSize = wav.getSampleRate() * 1; //30; process every 30 seconds   1.323.000
        
        System.out.println("Stepsize: " + stepSize);
        System.out.println("NOSamples: " + wav.getNumberOfSamples());
        
        Map<Integer, String> freqHightTable = new LinkedHashMap<>();
        Map<Integer, String> freqMagTable = new LinkedHashMap<>();
        
        int timeValue = 0;
        for (int i = 0; i < wav.getNumberOfSamples(); i += stepSize) {
            if(i + frequencyWrapper.getWindowSize() > wav.getNumberOfSamples())
                break;
            
            int[] amplitudeChunk = wav.getAmplitudeWindow(i, frequencyWrapper.getWindowSize());
            
            Map<Double, Double> spectrum = frequencyWrapper.getSpectrum(amplitudeChunk);
            
            List<Tuple<Double, Double>> frequencyBands = new ArrayList<>();
            Tuple<Double, Double> firstBandDominantFrequency = new Tuple<>(0.0, 0.0);
            Tuple<Double, Double> secondBandDominantFrequency = new Tuple<>(0.0, 0.0);
            Tuple<Double, Double> thirdBandDominantFrequency = new Tuple<>(0.0, 0.0);
            Tuple<Double, Double> fourthBandDominantFrequency = new Tuple<>(0.0, 0.0);
            Tuple<Double, Double> fifthBandDominantFrequency = new Tuple<>(0.0, 0.0);
            Tuple<Double, Double> sixthBandDominantFrequency = new Tuple<>(0.0, 0.0);
            
            frequencyBands.add(firstBandDominantFrequency);
            frequencyBands.add(secondBandDominantFrequency);
            frequencyBands.add(thirdBandDominantFrequency);
            frequencyBands.add(fourthBandDominantFrequency);
            frequencyBands.add(fifthBandDominantFrequency);
            frequencyBands.add(sixthBandDominantFrequency);
            
            for(Map.Entry<Double, Double> entry : spectrum.entrySet()){

                if((entry.getKey() > 80 && entry.getKey() < 200) && entry.getValue() > firstBandDominantFrequency.getY()){
                        firstBandDominantFrequency.setX(entry.getKey());
                        firstBandDominantFrequency.setY(entry.getValue());
                }
                    
                if((entry.getKey() > 200 && entry.getKey() < 440) && entry.getValue() > secondBandDominantFrequency.getY()){
                        secondBandDominantFrequency.setX(entry.getKey());
                        secondBandDominantFrequency.setY(entry.getValue()); 
                }
                
                if((entry.getKey() > 440 && entry.getKey() < 600) && entry.getValue() > thirdBandDominantFrequency.getY()){
                        thirdBandDominantFrequency.setX(entry.getKey());
                        thirdBandDominantFrequency.setY(entry.getValue());
                }

                if((entry.getKey() > 600 && entry.getKey() < 1000) && entry.getValue() > fourthBandDominantFrequency.getY()){
                        fourthBandDominantFrequency.setX(entry.getKey());
                        fourthBandDominantFrequency.setY(entry.getValue());
                }  
                
                if((entry.getKey() > 1000 && entry.getKey() < 2000) && entry.getValue() > fifthBandDominantFrequency.getY()){
                        fifthBandDominantFrequency.setX(entry.getKey());
                        fifthBandDominantFrequency.setY(entry.getValue());
                } 
                
                if((entry.getKey() > 2000 && entry.getKey() < 3400) && entry.getValue() > sixthBandDominantFrequency.getY()){
                        sixthBandDominantFrequency.setX(entry.getKey());
                        sixthBandDominantFrequency.setY(entry.getValue());
                } 
            }
            
            String freqHeightValue = "";
            for(Tuple<Double, Double> freqBand : frequencyBands){
                freqHeightValue += "," + freqBand.getX().toString();
            }
            
            String freqMagValue = "";
            for(Tuple<Double, Double> freqBand : frequencyBands){
                freqMagValue += "," + freqBand.getY().toString();
            }
            
            freqHightTable.put(timeValue, freqHeightValue);
            freqMagTable.put(timeValue, freqMagValue);
            
            
            long fingerprint = hash(firstBandDominantFrequency.getX().longValue(), secondBandDominantFrequency.getX().longValue(), thirdBandDominantFrequency.getX().longValue(), fourthBandDominantFrequency.getX().longValue());
            
            //System.out.println(fingerprint);
            
            if(!distinctVoices.contains(fingerprint))
                distinctVoices.add(fingerprint);
            
            timeValue += 30;
        }
        
        printTable(freqHightTable);
        System.out.println("\n");
        printTable(freqMagTable);
        
        //distinctVoices.forEach(fingerprint -> System.out.println(fingerprint));
    }
    
    private static final int FUZ_FACTOR = 2;

    private static long hash(long p1, long p2, long p3, long p4) {
        return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR)) * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100 + (p1 - (p1 % FUZ_FACTOR));
    }
    
    private static void printTable(Map<Integer, String> table){
        String result = "Time S,80 - 200,200 - 440,440 - 600,600 - 1000,1000 - 2000,2000 - 3400,,80 - 200,200 - 440,440 - 600,600 - 1000,1000 - 2000,2000 - 3400\n";
        for(Map.Entry<Integer, String> entry : table.entrySet()){
            result += entry.getKey() + entry.getValue() + "\n";
        }
        System.out.println(result);
    }
}
