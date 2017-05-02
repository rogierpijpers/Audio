/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Rogier
 */
public class Scheduler {
    private static Scheduler instance;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    
    private Scheduler(){
        Thread thread = new Thread(() -> {
            for(int i = 0; i <= 20; i++){
                System.out.println(i);
            }
        });
        int initDelay = getHoursUntil(12);
         initDelay = getHoursUntil(12);
        executorService.scheduleAtFixedRate(thread, initDelay, 24, TimeUnit.HOURS);
        System.out.println("Init delay: " + initDelay);
        System.out.println("Method: " + getHoursUntil(12));
    }
    
    public static Scheduler getInstance(){
        if(instance == null){
            instance = new Scheduler();
        }
        return instance;
    }
    
    // scheduledHour in 24 hours a day format
    public static int getHoursUntil(int scheduledHour){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, scheduledHour);
        long milliseconds = (calendar.getTimeInMillis() - System.currentTimeMillis());
        int hours = (int) ((milliseconds / (1000*60*60)) % 24);
        return hours;
    }
    
}
