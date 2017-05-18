/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chart;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rogier
 */
public class ChartDTO {
    private final String title;
    private final HashMap<List<ChartPoint>, String> series;
    
    public ChartDTO(String title){
        this.title = title;
        series = new HashMap<>();
    }
    
    public void addSeries(List<ChartPoint> values, String legend){
        series.put(values, title);
    }
    
    public String getTitle(){
        return title;
    }
    
    public HashMap<List<ChartPoint>, String> getSeries(){
        return series;
    }
}
