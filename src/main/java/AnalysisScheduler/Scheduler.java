/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalysisScheduler;

/**
 *
 * @author Hans
 */
public class Scheduler {

    private static Scheduler scheduler = null;

    private Scheduler() {
    }

    public static Scheduler create() {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        return scheduler;
    }

}
