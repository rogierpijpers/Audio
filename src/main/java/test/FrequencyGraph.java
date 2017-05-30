
package test;

import analyse.FrequencySpectrum;
import audio.Audio;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Hans & Rogier
 * 
 * FFT frequentie banden van 43 Hz. 
 * Loopt vanaf 86 Hz tot 22.006 Hz.
 */
public class FrequencyGraph extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException, UnsupportedAudioFileException {

        StackPane root = new StackPane();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        
        //File file = new File("resources\\pdd-4-1-1.wav");
        File file = new File("resources\\jennifer.wav");
        //File file = new File("resources/ftdl_ppa_sande_spontansprache_gegenwart_ui.wav");
        Audio wav = new Audio(file);


        int timeDomain = 441;//(int) (88200 + 110.3) ; // 2 seconds and something
        FrequencySpectrum frequencyWrapper = new FrequencySpectrum(wav.getSampleRate());
        double[] doubleData = new double[frequencyWrapper.getWindowSize()];
        
        int[] amplitudes = wav.getAmplitudeWindow(timeDomain, frequencyWrapper.getWindowSize());
        Map<Double, Double> frequencySpectrum = frequencyWrapper.getSpectrum(amplitudes); 
        
        frequencySpectrum.entrySet().forEach((entry) -> {
            series.getData().add(new XYChart.Data(Double.toString(entry.getKey()), entry.getValue()));
        });
        
        barChart.getData().add(series);
        series.setName("Frequency");
       
        root.getChildren().add(barChart);
        

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
