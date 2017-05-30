/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;
import chart.ChartDataSet;

/**
 *
 * @author Rogier
 */
public abstract class Analysis {
    protected final Audio audio;

    public Analysis(Audio audio){
        this.audio = audio;
    }

    public abstract AnalysisResult analyse();

    public abstract String getDescription();

    public abstract ChartDataSet getChartDataSet();
}
