/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import analyse.AnalysisResult;
import analyse.VADAnalysis;
import audio.Audio;
import chart.VADAnalysisChart;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rogier
 */
public class AnalysisTest {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException{
        File file = new File("resources/ftdl_ppa_sande_spontansprache_gegenwart_ui.wav");
        Audio wav = new Audio(file);
        System.out.println(wav.toString());
        
        VADAnalysis analysis = new VADAnalysis(wav);
        AnalysisResult result = analysis.analyse();
        //analysis.getActivities().forEach(activity -> System.out.println(activity.toString()));
        System.out.println(result.getResultDescription());
        
        System.out.println(((20 * Math.log10(wav.getMaxAmplitude(0, wav.getNumberOfSamples())))));
        
        VADAnalysisChart chart = new VADAnalysisChart(  );
        
    }
}
