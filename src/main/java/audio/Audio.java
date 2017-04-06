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

public class Audio {
    private int sampleRate;
    private int bitRate;
    private int sampleSize;
    private int numberOfChannels;
    private int numberOfSamples;
    private byte[] data;
    private boolean isBigEndian;

    public Audio(File wavFile) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
        AudioFormat audioFormat = audioInputStream.getFormat();

        numberOfSamples = (int) audioInputStream.getFrameLength();
        System.out.println(numberOfSamples);
        sampleRate = (int) audioFormat.getSampleRate();
        bitRate = audioFormat.getSampleSizeInBits();
        numberOfChannels = audioFormat.getChannels();
        sampleSize = bitRate / 8;
        isBigEndian = audioFormat.isBigEndian();

        int dataLength = (int) audioInputStream.getFrameLength() * audioFormat.getSampleSizeInBits() * numberOfChannels / 8;
        data = new byte[dataLength];
        audioInputStream.read(data);
        audioInputStream.close();

        if(!isRightFormat(audioFormat)){
            throw new UnsupportedAudioFileException();
        }
    }
    
    public byte[] getData(){
        return data;
    }

    private boolean isRightFormat(AudioFormat audioFormat){
        return (bitRate == 16) && (sampleRate == 44100) && !audioFormat.isBigEndian(); // && (numberOfChannels == 1)
    }

    public int getNumberOfSamples(){
        return numberOfSamples;
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
