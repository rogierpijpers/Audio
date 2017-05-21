
package test;

import analyse.Activity;
import analyse.AnalysisResult;
import analyse.FrequencyWrapper;
import analyse.VADAnalysis;
import analyse.VADAnalysisImpl;
import audio.Audio;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Hans & Rogier
 */
public class Graph extends Application {
    
    private File getTestFile(int index){
        switch(index){
            case 1: return new File("resources\\pdd-4-1-1.wav");
            case 2: return new File("resources\\jennifer.wav");
            case 3: return new File("resources/ftdl_ppa_sande_spontansprache_gegenwart_ui.wav");
            default: return null;
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException, UnsupportedAudioFileException {

        StackPane root = new StackPane();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        XYChart.Series series4 = new XYChart.Series();

        File file = getTestFile(1);
        
        Audio wav = new Audio(file);
        
        VADAnalysis analysis = new VADAnalysis(wav);
        AnalysisResult result = analysis.analyse();
        boolean[] activity = analysis.getActivity();
        
        int timeValue = 0;
        int silenceIndex = 0;
        int stepSize = wav.getSampleRate() / 100;
        int amplitudePeak = wav.getMaxAmplitude(0, wav.getNumberOfSamples());
        
        int frequencyWindowSize = 1024;
        FrequencyWrapper frequencyWrapper = new FrequencyWrapper(frequencyWindowSize);
        
        for (int i = 0; i < wav.getNumberOfSamples(); i += stepSize) { //(wav.getNumberOfSamples() / duration) 
            boolean lastIteration = ( i + stepSize ) >= wav.getNumberOfSamples();
            int endSample = lastIteration ? wav.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = wav.getMaxAmplitude(i, endSample);
            
            series.getData().add(new XYChart.Data(timeValue, amplitudeValue));     
            series2.getData().add(new XYChart.Data(timeValue, wav.getAmplitude(i)));
            
            int yValue = activity[silenceIndex] ? amplitudePeak : 0;
            series3.getData().add(new XYChart.Data(timeValue, yValue));
            
//            if(i + frequencyWindowSize < wav.getNumberOfSamples()){
//                int[] amps = wav.getAmplitudeWindow(i, frequencyWindowSize);
//                double dominantFrequency = frequencyWrapper.getDominantFrequency(frequencyWrapper.toDoubleArray(amps), wav.getSampleRate());
//                System.out.println(dominantFrequency);
//                if(!activity[silenceIndex])
//                    dominantFrequency = 0;
//                series4.getData().add(new XYChart.Data(timeValue, -dominantFrequency));
//            }
 
            timeValue += 10;
            silenceIndex++;
        }     
        
        System.out.println(result.getResultDescription());
        
        lineChart.getData().add(series);
        series.setName("Envelope");
        lineChart.getData().add(series2);
        series2.setName("Amplitude");
        lineChart.getData().add(series3);
        series3.setName("Voice Activity");
        lineChart.getData().add(series4);
        series4.setName("Dominant Frequency");
        
        root.getChildren().add(lineChart);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle(file.getName());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
