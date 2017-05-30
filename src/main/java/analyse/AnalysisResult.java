/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rogier
 */
public class AnalysisResult {
    private Map<String, Double> properties;
    private final String resultDescription;

    public AnalysisResult(String resultDescription) {
        this.properties = new HashMap<>();
        this.resultDescription = resultDescription;
    }
    
    public void addProperty(String name, double value){
        properties.put(name, value);
    }
    
    public void setProperties(Map<String, Double> properties){
        this.properties = properties;
    }
    
    public Map<String, Double> getProperties(){
        return properties;
    }

    public String getResultDescription() {
        return resultDescription;
    }

}
