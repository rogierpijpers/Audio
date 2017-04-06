/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import analyse.VADAnalysis;
import audio.Audio;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rogier
 */
public class AnalysisTest {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException{
        File file = new File("D:\\Sources\\speech-recognition\\resources\\jennifer.wav");
        Audio wav = new Audio(file);
        
        VADAnalysis analysis = new VADAnalysis();
        analysis.analyse(wav);
    }
}
