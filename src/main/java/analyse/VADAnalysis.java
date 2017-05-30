/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;
import chart.ChartDataSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.ArrayUtilities;

/**
 *
 * @author Rogier
 */
public class VADAnalysis extends Analysis {
    private final static int LOW_PASS_FILTER = 440;
    private final static int HIGH_PASS_FILTER = 800;
    
    private final Threshold threshold;
    private final int windowSize;
    private final boolean[] activity;
    
    private VADAnalysisProperties vadProperties;
    
    public VADAnalysis(Audio audio){
        super(audio);
        this.windowSize = audio.getSampleRate() / 100; // 10ms
        this.activity = new boolean[(audio.getNumberOfSamples() / (windowSize / 2)) + 1];
        
        threshold = new Threshold(audio, windowSize, LOW_PASS_FILTER, HIGH_PASS_FILTER);
    }
    
    @Override
    public AnalysisResult analyse() {    
        int silenceIndex = 0;
        FrequencySpectrum frequencySpectrum;
        
        for (int i = 0; i < audio.getNumberOfSamples(); i += (windowSize / 2)) {
            int freqWindowSize = (( i + windowSize ) >= audio.getNumberOfSamples()) ? audio.getNumberOfSamples() - i : windowSize;
            
            frequencySpectrum = new FrequencySpectrum(audio.getSampleRate(), freqWindowSize);   
            int[] freqWindow = audio.getAmplitudeWindow(i, frequencySpectrum.getWindowSize());
              
            Map<Double, Double> spectrum = frequencySpectrum.getSpectrum(freqWindow, LOW_PASS_FILTER, HIGH_PASS_FILTER);
            
            List<Double> magnitudeValues = new ArrayList<>();
            
            spectrum.entrySet().forEach((entry) -> {
                magnitudeValues.add(entry.getValue());
            });
            
            activity[silenceIndex] = !magnitudeValues.isEmpty() ? isSilent(magnitudeValues) : false;
            silenceIndex++;
        }
        
        AnalysisResult result = new AnalysisResult(getDescription());
        vadProperties = new VADAnalysisProperties(activity, audio, windowSize); 
        result.setProperties(vadProperties.toMap());
        
        return result;
    }
    
    private boolean isSilent(List<Double> magnitudeValues){
        return ArrayUtilities.getAverage(magnitudeValues) > threshold.getValue();
    }
    
    public boolean[] getActivity(){
        return activity;
    }
    
    @Override
    public String getDescription(){
        String result = "";
        result += "VAD analysis looks for activity in a wav file, larger than calculated threshold.\n";
        result += "Activity is measured for voice frequencies only, currently set at:\n";
        result += "\t-low pass filter: " + LOW_PASS_FILTER + "\n";
        result += "\t-high pass filter: " + HIGH_PASS_FILTER;
        return result;
    }
    
    @Override
    public ChartDataSet getChartDataSet(){
        int stepSize = audio.getSampleRate() / 100;
        int loopIndex = 0;
        
        double[] amplitudeData = new double[(audio.getNumberOfSamples() / ((audio.getSampleRate() / 100) / 2)) + 1];
        double[] envelopeData = new double[(audio.getNumberOfSamples() / ((audio.getSampleRate() / 100) / 2)) + 1];
        double[] activityData = new double[(audio.getNumberOfSamples() / ((audio.getSampleRate() / 100) / 2)) + 1]; 
        
        int maxAmplitude = audio.getMaxAmplitude(0, audio.getNumberOfSamples());

        for (int i = 0; i < audio.getNumberOfSamples(); i += stepSize / 2) {
            boolean lastIteration = ( i + stepSize ) >= audio.getNumberOfSamples();
            int endSample = lastIteration ? audio.getNumberOfSamples() : i + stepSize;
            int amplitudeValue = audio.getMaxAmplitude(i, endSample);
            
            amplitudeData[loopIndex] = audio.getAmplitude(i);
            envelopeData[loopIndex] = amplitudeValue;
            activityData[loopIndex] = activity[loopIndex] ? maxAmplitude : 0;
            loopIndex++;
        }
        
        String chartName = "VAD_analysis_" + audio.getFileName();
        
        ChartDataSet dataSet = new ChartDataSet(chartName, "Time", "Amplitude");
        dataSet.addData("amplitude", amplitudeData);
        dataSet.addData("envelope", envelopeData);
        dataSet.addData("activity", activityData);
        
        dataSet.setXAxisScale(5);
        //dataSet.setPath("charts");
        
        return dataSet;
    }
}
