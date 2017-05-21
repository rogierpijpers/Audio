/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart;

import analyse.AnalysisResult;
import analyse.FrequencyWrapper;
import analyse.VADAnalysis;
import audio.Audio;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Rogier
 */
public class VADAnalysisChart {
    private int[] amplitudeData;
    private int[] envelopeData;
    private int[] activityData;
    private double[] dominantFrequencyData;
    
    public void chartToPNG() throws IOException{
         JFreeChart chart = ChartFactory.createXYLineChart("VAD Analysis", "Time", "Amplitude", createDatasets(), PlotOrientation.VERTICAL, true, true, false);
         ChartUtilities.saveChartAsPNG(new File("VAD analysis.png"), chart, 1600, 900);
    }
    
    private XYSeriesCollection createDatasets( ) {
      XYSeriesCollection seriesCollection = new XYSeriesCollection();
        
      XYSeries amplitudeDataset = new XYSeries("Amplitude");
      XYSeries envelopeDataset = new XYSeries("Envelope");
      XYSeries activityDataset = new XYSeries("Activity");
      XYSeries dominantFrequencyDataset = new XYSeries("Dominant frequency");
      
      for(int i = 0; i < amplitudeData.length; i++){
          amplitudeDataset.add((i * 5), amplitudeData[i]);
      }
      
      for(int i = 0; i < envelopeData.length; i++){
          envelopeDataset.add((i * 5), envelopeData[i]);
      }
      
      for(int i = 0; i < activityData.length; i++){
          activityDataset.add((i * 5), activityData[i]);
      }
      
      for(int i = 0; i < dominantFrequencyData.length; i++){
          dominantFrequencyDataset.add((i * 5), dominantFrequencyData[i]);
      }
      
      seriesCollection.addSeries(amplitudeDataset);
      seriesCollection.addSeries(envelopeDataset);
      seriesCollection.addSeries(activityDataset);
      seriesCollection.addSeries(dominantFrequencyDataset);
      
      return seriesCollection;
   }

    public int[] getAmplitudeData() {
        return amplitudeData;
    }

    public void setAmplitudeData(int[] amplitudeData) {
        this.amplitudeData = amplitudeData;
    }

    public int[] getEnvelopeData() {
        return envelopeData;
    }

    public void setEnvelopeData(int[] envelopeData) {
        this.envelopeData = envelopeData;
    }

    public int[] getActivityData() {
        return activityData;
    }

    public void setActivityData(int[] activityData) {
        this.activityData = activityData;
    }

    public double[] getDominantFrequencyData() {
        return dominantFrequencyData;
    }

    public void setDominantFrequencyData(double[] dominantFrequencyData) {
        this.dominantFrequencyData = dominantFrequencyData;
    }
    
    
}
