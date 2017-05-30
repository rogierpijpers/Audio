/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rogier
 */
public class ChartDataSet {
    private String chartName;
    private String path;
    private String xAxisLabel;
    private String yAxisLabel;
    private int xAxisScale = 1;
    private Map<String, double[]> datasets;

    public ChartDataSet(String chartName, String xAxisLabel, String yAxisLabel){
        this.chartName = chartName;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.datasets = new HashMap<>();
        this.path = "charts";
    }

    public String getChartName(){
        return chartName;
    }

    public void addData(String label, double[] data){
        datasets.put(label, data);
    }

    public Map<String, double[]> getAllData(){
        return datasets;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public void setXAxisScale(int scale){
        this.xAxisScale = scale;
    }

    public int getXAxisScale(){
        return xAxisScale;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }
}
