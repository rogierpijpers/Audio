/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyse;

import audio.Audio;

/**
 *
 * @author Rogier
 */
public class VADAnalysis {

    private static final int AMPLITUDE_THRESHOLD = 100;
    private static final int TIME_THRESHOLD_MS = 100;

    public void analyse(Audio audio) {
        int numberOfSilences = 0;
        int totalLengthOfSilence = 0;
        int startSilence = 0;
        int stopSilence = 0;
        int timeInMs = 0;
        int amplitudeValue;
        boolean silenceStarted = false;
        boolean isSilent;
        boolean lastIteration;
        int numberOfSamples = audio.getNumberOfSamples();
        int durationInMilliSeconds = audio.getDurationInMilliSeconds();

        for (int i = 0; i < numberOfSamples; i += (numberOfSamples / durationInMilliSeconds)) {

            amplitudeValue = audio.getAmplitude(i);
            isSilent = amplitudeValue < AMPLITUDE_THRESHOLD && amplitudeValue > -AMPLITUDE_THRESHOLD;
            lastIteration = i + (numberOfSamples / durationInMilliSeconds) >= numberOfSamples;

            if (isSilent && !silenceStarted) {
                startSilence = timeInMs;
                silenceStarted = true;
            }

            if ((!isSilent || lastIteration) && silenceStarted) {
                stopSilence = timeInMs;
                if (stopSilence - startSilence > TIME_THRESHOLD_MS || lastIteration) {
                    totalLengthOfSilence += stopSilence - startSilence;
                    numberOfSilences++;
                }
                silenceStarted = false;
            }

            timeInMs++;
        }

        toString(audio, totalLengthOfSilence, numberOfSilences);
    }

    private void toString(Audio audio, int totalLengthOfSilence, int numberOfSilences) {
        System.out.println("\n--- Silence statistics ---");
        System.out.println("Total time in ms: \t" + audio.getDurationInMilliSeconds());
        System.out.println("Total silence in ms: \t" + totalLengthOfSilence);
        System.out.println("Number of silences: \t" + numberOfSilences);
        System.out.println("Silence percentage: \t" + (totalLengthOfSilence * 100.0f) / audio.getDurationInMilliSeconds());
    }
}
