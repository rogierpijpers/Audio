/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import analyse.Analysis;
import analyse.AnalysisResult;
import analyse.VADAnalysis;
import audio.Audio;
import chart.ChartGenerator;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rogier
 */
public class AnalysisTest {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException{
        File file = new File("resources/ppa_pnfa.wav");
        Audio wav = new Audio(file);
        System.out.println(wav.toString());
        
        Analysis analysis = new VADAnalysis(wav);
        AnalysisResult result = analysis.analyse();
        //analysis.getActivities().forEach(activity -> System.out.println(activity.toString()));
        System.out.println("File name: " + file.getName());
        System.out.println(result.getResultDescription() + "\n");
        System.out.println("--- Analysis properties:");
        for(Map.Entry<String, Double> entry : result.getProperties().entrySet()){
            System.out.println(entry.getKey() + " \t\t\t " + entry.getValue());
        }
        System.out.println("\n");

        System.out.println("Creating chart...");
        ChartGenerator chartGenerator = new ChartGenerator(analysis.getChartDataSet());
        chartGenerator.createPNGFromChart();
        System.out.println("Done!");
    }
}
