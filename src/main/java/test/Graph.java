
package test;

import analyse.AnalysisResult;
import analyse.FrequencySpectrum;
import analyse.VADAnalysis;
import audio.Audio;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
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
            case 4: return new File("resources/ppa_sd.wav");
            case 5: return new File("resources/ppa_pnfa.wav");
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
//        XYChart.Series series4 = new XYChart.Series();
        
        final CategoryAxis barX = new CategoryAxis();
        final NumberAxis barY = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(barX, barY);
        
        File file = getTestFile(4);
        
        Audio wav = new Audio(file);
        
        VADAnalysis analysis = new VADAnalysis(wav);
        AnalysisResult result = analysis.analyse();
        boolean[] activity = analysis.getActivity();
        
        int timeValue = 0;
        int silenceIndex = 0;
        int stepSize = wav.getSampleRate() / 100;
        int amplitudePeak = wav.getMaxAmplitude(0, wav.getNumberOfSamples());
        
        int frequencyWindowSize = 512;
       FrequencySpectrum frequencyWrapper = new FrequencySpectrum(wav.getSampleRate(), frequencyWindowSize);
        
        int[] amplitudeData = new int[(wav.getNumberOfSamples() / ((wav.getSampleRate() / 100) / 2)) + 1];
        int[] envelopeData = new int[(wav.getNumberOfSamples() / ((wav.getSampleRate() / 100) / 2)) + 1];
        int[] activityData = new int[(wav.getNumberOfSamples() / ((wav.getSampleRate() / 100) / 2)) + 1];
        double[] dominantFrequencyData = new double[(wav.getNumberOfSamples() / ((wav.getSampleRate() / 100) / 2)) + 1];
        
        for (int i = 0; i < wav.getNumberOfSamples(); i += stepSize / 2) { //(wav.getNumberOfSamples() / duration) 
            boolean lastIteration = ( i + stepSize ) >= wav.getNumberOfSamples();
            int endSample = lastIteration ? wav.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = wav.getMaxAmplitude(i, endSample);
            
            series.getData().add(new XYChart.Data(timeValue, amplitudeValue));     
            series2.getData().add(new XYChart.Data(timeValue, wav.getAmplitude(i)));
            
            int yValue = activity[silenceIndex] ? amplitudePeak : 0;
            series3.getData().add(new XYChart.Data(timeValue, yValue));
            
//            if(i + frequencyWindowSize < wav.getNumberOfSamples()){
//                int[] amps = wav.getAmplitudeWindow(i, frequencyWindowSize);
//                double dominantFrequency = frequencyWrapper.getDominantFrequency(amps);
//                dominantFrequencyData[silenceIndex] = dominantFrequency;
//                series4.getData().add(new XYChart.Data(Integer.toString(timeValue), dominantFrequency));
//            }
            
            amplitudeData[silenceIndex] = wav.getAmplitude(i);
            envelopeData[silenceIndex] = amplitudeValue;
            activityData[silenceIndex] = yValue;
            
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
//        barChart.getData().add(series4);
//        series4.setName("Dominant Frequency");
        
        root.getChildren().addAll(lineChart, barChart);
        
        Scene scene = new Scene(root, 600, 800);

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
