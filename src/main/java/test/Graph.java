
package test;

import audio.Audio;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
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

    @Override
    public void start(Stage primaryStage) throws IOException, UnsupportedAudioFileException {

        StackPane root = new StackPane();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        File file = new File( "D:\\Sources\\speech-recognition\\resources\\jennifer.wav");
        //File file = new File("D:\\Sources\\Hans audio graph\\WavReader\\sine.wav");
        //File file = new File( "C:\\Users\\Hans\\Documents\\NetBeansProjects\\WavReader\\WavReader\\sine.wav");
        Audio wav = new Audio(file);

        int duration = wav.getDurationInMilliSeconds();
        int timeValue = 0;
        int maxAmp = 0;
        
        // envelope
        int stepSize = 441;
        for (int i = 0; i < wav.getNumberOfSamples(); i += stepSize) { //(wav.getNumberOfSamples() / duration)
            
            boolean lastIteration = ( i + stepSize ) >= wav.getNumberOfSamples();
            int endSample = lastIteration ? wav.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = wav.getMaxAmplitude(i, endSample);

            series.getData().add(new XYChart.Data(timeValue, amplitudeValue));
            series2.getData().add(new XYChart.Data(timeValue, wav.getAmplitude(i)));
            timeValue += 10;
        }
        
        lineChart.getData().add(series);
        series.setName("Amplitude");
        lineChart.getData().add(series2);
        series2.setName("Envelope");
        
        root.getChildren().add(lineChart);
        

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("WavPlot");
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
