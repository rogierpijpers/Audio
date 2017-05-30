/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Rogier
 */
public class ChartGenerator {
    private ChartDataSet dataSet;

    public ChartGenerator(ChartDataSet dataSet){
        this.dataSet = dataSet;
    }

    /**
     *
     * @return path to chart image
     * @throws IOException
     */
    public String createPNGFromChart() throws IOException {
        String chartFileName = setFilePathWithExtension(dataSet.getChartName());

        File png = new File(chartFileName);
        
        if(dataSet.getPath() != null && !dataSet.getPath().equals(""))
            png.getParentFile().mkdirs();

        JFreeChart chart = ChartFactory.createXYLineChart(dataSet.getChartName(), dataSet.getxAxisLabel(), dataSet.getyAxisLabel(), createDatasets(), PlotOrientation.VERTICAL, true, true, false);
        ChartUtilities.saveChartAsPNG(png, chart, 1600, 900);
        return png.getAbsolutePath();
    }

    private String setFilePathWithExtension(String chartName) {
        if(!chartName.endsWith(".png"))
            chartName += ".png";

        if(dataSet.getPath() == null || dataSet.getPath().equals(""))
            return chartName;
        
        String path = dataSet.getPath();

        path = addPathDelimiters(path);
        chartName = path + chartName;

        return chartName;
    }

    private String addPathDelimiters(String path){
        if(!path.endsWith(File.separator))
            path += File.separator;
        return path;
    }

    private XYSeriesCollection createDatasets( ) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        for(Map.Entry<String, double[]> entry : dataSet.getAllData().entrySet()){
            XYSeries series = new XYSeries(entry.getKey());
            double[] data = entry.getValue();
            for(int i = 0; i < data.length; i++){
                series.add(i * dataSet.getXAxisScale(), data[i]);
            }
            seriesCollection.addSeries(series);
        }
        return seriesCollection;
    }
}
