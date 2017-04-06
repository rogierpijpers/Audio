package audio;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Rogier on 4-4-2017.
 */
public class Test {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        try {
            //File file = new File("D:\\Sources\\speech-recognition\\resources\\jennifer.wav");
            File file = new File("D:\\Sources\\Hans audio graph\\WavReader\\sine.wav");
            Audio wav = new Audio(file);

            for (int i = 0; i < wav.getNumberOfSamples(); i ++) {
                System.out.println((int) wav.getAmplitude(i));
            }

            System.out.println(wav.toString());

        }catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }
}
