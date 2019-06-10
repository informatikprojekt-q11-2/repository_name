/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.util.TimerTask;
import javax.swing.JLabel;
import org.apache.commons.lang3.time.StopWatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Niklas
 */
public class Timer extends StopWatch{
    private JLabel timeWhite, timeBlack, playingTime;
    private java.util.Timer refreshTimer;
    private TimerTask refreshTask;
    private int timeInMs;
    private GameLogic logic;
    
    public Timer(GameLogic logic, GameOverlay overlay, int timeInMs){
        refreshTimer = new java.util.Timer();
        if(timeInMs > 0){
            //TODO  JLabels timeWhite und timeBlack hier einfuegen anstatt in GameOverlay
            this.logic = logic;
            this.timeInMs = timeInMs;
            
            limitedClock();
        }else{
            //TODO playingTime dem GameOverlay hinzufügen
            unlimitedClock();
        }
    }
    
    //TODO Methode wird nach dem ersten Zug aufgerufen
    public void clock(){
        if(!isStarted()){
            start();
            refreshTimer.scheduleAtFixedRate(refreshTask, 999, 999);
        }
    }
    
    private void unlimitedClock(){
        refreshTask = new TimerTask(){
            @Override
            public void run() {
                long currentTime = getTime(TimeUnit.SECONDS);
                long sek = currentTime%60;
                long min = currentTime / 60;
                long h = min / 60;
                min = min % 60;
                    
                String strSek, strMin, strH;
                    
                if(sek<10){
                    strSek="0"+sek;
                }else{
                    strSek=sek+"";
                }
                    
                if(min<10){
                    strMin="0"+min;
                }else{
                    strMin=min+"";
                }
                    
                if(h<10){
                    strH="0"+h;
                }else{
                    strH=h+"";
                }
                    
                String time = strH+":"+strMin+":"+strSek;
                //TODO Text von playingTime wird auf time gesetzt
                System.out.println(time);
            }
                
        };
    }
    
    private void limitedClock(){
        //TODO unlimitedClock() aufrufen für gesamte Spielzeit + Zeit für schwarz/weiß herunterzählen lassen
    }
    
}
