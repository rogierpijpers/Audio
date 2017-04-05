
package audio;

import java.io.File;
import java.io.IOException;

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
 * @author Hans
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
        //File file = new File( "D:\\Sources\\speech-recognition\\resources\\jennifer.wav");
        File file = new File( "D:\\Sources\\Hans audio graph\\WavReader\\sine.wav");
        Audio wav = new Audio(file);

        int duration = wav.getDurationInMilliSeconds();
        int yIndex = 0;
        int maxAmp = 0; 
        
        for (int i = 0; i < wav.getNumberOfSamples(); i += (wav.getNumberOfSamples() / duration)) {

//            System.out.println("Duration: " + duration);
//            System.out.println("Number of samples: " + wav.getNumberOfSamples());
//            System.out.println("Stepsize: " + duration / wav.getNumberOfSamples());
//            System.out.println("dB: " + wav.getDecibel(i));
//            System.out.println("Amplitude: " + wav.getAmplitude(i));
            
            if(wav.getAmplitude(i) > maxAmp){
                maxAmp = wav.getAmplitude(i);
            }

            series.getData().add(new XYChart.Data(yIndex, wav.getAmplitude(i)));
            yIndex++;
        }
        
        System.out.println("MaxAmp: \t" + maxAmp);

        lineChart.getData().add(series);
        root.getChildren().add(lineChart);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
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
