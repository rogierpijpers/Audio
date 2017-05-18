/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import analyse.AnalysisResult;
import analyse.VADAnalysis;
import audio.Audio;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rogier
 */
public class AnalysisTest {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException{
        File file = new File("resources/jennifer.wav");
        Audio wav = new Audio(file);
        System.out.println(wav.toString());
        
        VADAnalysis analysis = new VADAnalysis(wav);
        AnalysisResult result = analysis.analyse();
        analysis.getActivities().forEach(activity -> System.out.println(activity.toString()));
        System.out.println(result.getResultDescription());
        
        System.out.println(((20 * Math.log10(wav.getMaxAmplitude(0, wav.getNumberOfSamples())))));
        
//        int stepSize = 441;
//        List<Integer> amplitudes = new ArrayList<>();
//        for (int i = 0; i < wav.getNumberOfSamples(); i += stepSize) { //(wav.getNumberOfSamples() / duration) 
//            boolean lastIteration = ( i + stepSize ) >= wav.getNumberOfSamples();
//            int endSample = lastIteration ? wav.getNumberOfSamples() : i + stepSize;
//            amplitudes.add(Math.abs(wav.getMaxAmplitude(i, endSample)));
//        }
//        
//        Collections.sort(amplitudes, (i1, i2) -> {
//            return i1 - i2;
//        });
//        
//        amplitudes.forEach(amp -> System.out.println(amp));
    }
}
