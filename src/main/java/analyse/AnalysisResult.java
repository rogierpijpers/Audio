/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

/**
 *
 * @author Rogier
 */
public class AnalysisResult {
    private double value;
    private String unit;
    private String resultDescription;

    public AnalysisResult(double value, String unit, String resultDescription) {
        this.value = value;
        this.unit = unit;
        this.resultDescription = resultDescription;
    }
    
    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    
    
}
