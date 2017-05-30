package audio;

/**
 * Created by Rogier on 4-4-2017.
 */
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Audio {
    private String fileName;
    private int sampleRate;
    private int bitRate;
    private int sampleSize;
    private int numberOfChannels;
    private int numberOfSamples;
    private byte[] data;

    public Audio(File wavFile) throws UnsupportedAudioFileException, IOException {
        AudioFormat audioFormat;
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile)) {
            audioFormat = audioInputStream.getFormat();

            numberOfSamples = (int) audioInputStream.getFrameLength();
            sampleRate = (int) audioFormat.getSampleRate();
            bitRate = audioFormat.getSampleSizeInBits();
            numberOfChannels = audioFormat.getChannels();
            sampleSize = bitRate / 8;
            
            int dataLength = (int) audioInputStream.getFrameLength() * audioFormat.getSampleSizeInBits() * numberOfChannels / 8;
            data = new byte[dataLength];
            audioInputStream.read(data);
            
            this.fileName = wavFile.getName();
        }

        if(!isRightFormat(audioFormat)){
            //throw new UnsupportedAudioFileException();
        }
    }
    
    public String getFileName(){
        return fileName;
    }

    private boolean isRightFormat(AudioFormat audioFormat){
        return (bitRate == 16) && (sampleRate == 44100) && !audioFormat.isBigEndian(); // && (numberOfChannels == 1)
    }

    public int getNumberOfSamples(){
        return numberOfSamples;
    }
    
    public int getSampleRate(){
        return sampleRate;
    }

    public int getDurationInMilliSeconds(){
        return numberOfSamples / (sampleRate / 1000);
    }

    public double getDecibel(int sampleNumber){
        int amplitude = getAmplitude(sampleNumber);
        return amplitude == 0 ? 0 : (20 * Math.log10(amplitude));
    }

    public int getAmplitude(int sampleNumber){
        int index = sampleNumber * sampleSize * numberOfChannels;
        byte LSB = data[index];
        byte MSB = data[index + 1];
        return MSB << 8 | (255 & LSB);
    }
    
    public int[] getAmplitudeWindow(int startSample, int windowSize){
        int[] result = new int[windowSize];
        for(int i = 0; i < windowSize; i++){
            result[i] = getAmplitude(startSample + i);
        }
        return result;
    }
    
    public int getMaxAmplitude(int startSample, int endSample){
        List<Integer> ampList = new ArrayList<>();
        for(int y = startSample; y < endSample; y++){
            ampList.add(this.getAmplitude(y));
        }
        return Math.abs(Collections.max(ampList));
    }
    
    private boolean isValidSampleNumber(int sampleNumber) {
        return !(sampleNumber < 0 || sampleNumber >= data.length / sampleSize);
    }
    
    @Override
    public String toString(){
        String result = "";
        result += "sampleRate: \t\t" + sampleRate + "\n";
        result += "bitRate: \t\t" + bitRate + "\n";
        result += "numberOfChannels: \t" + numberOfChannels + "\n";
        result += "numberOfSamples: \t" + numberOfSamples + "\n";
        return result;
    }

}
