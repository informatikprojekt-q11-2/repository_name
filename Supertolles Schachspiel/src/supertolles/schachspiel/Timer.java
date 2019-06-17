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
    private long timeInSec;
    private long timeRemainingWhite, timeRemainingBlack;
    private long startTime;
    private GameLogic logic;
    private String currentColor=Constants.Color_WHITE;
    
    public Timer(GameLogic logic, GameOverlay overlay, long timeInSec){
        refreshTimer = new java.util.Timer();
        if(timeInSec > 0){
            //TODO  JLabels timeWhite und timeBlack hier einfuegen anstatt in GameOverlay
            this.logic = logic;
            this.timeInSec = timeRemainingWhite = timeRemainingBlack = timeInSec;
            
            limitedClock();
        }else{
            //TODO playingTime dem GameOverlay hinzufügen
            unlimitedClock();
        }
        clock();
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
                //TODO Text von playingTime wird auf time gesetzt
                System.out.println(getTime_H_Min_Sek(currentTime));
            }
                
        };
    }
    
    private void limitedClock(){
    	refreshTask = new TimerTask() {
			@Override
			public void run() {
				//gesamte Spielzeit
				long currentTime = getTime(TimeUnit.SECONDS);
				if(currentColor == Constants.Color_WHITE){
					if(timeRemainingWhite==0){
						timeUp(Constants.Color_WHITE);
					}
					timeRemainingWhite = timeInSec - currentTime + startTime;
				}else{
					if(timeRemainingBlack==0){
						timeUp(Constants.Color_WHITE);
					}
					timeRemainingBlack = timeInSec - currentTime + startTime;
				}
                //TODO Text von playingTime wird auf time gesetzt, bzw. auf die jeweiligen Label der Spieler
                System.out.println("Spielzeit: "+getTime_H_Min_Sek(currentTime)+ "    Spielzeit wei�:"+getTime_H_Min_Sek(timeRemainingWhite)+"    Spielzeit schwarz:"+getTime_H_Min_Sek(timeRemainingBlack));
			}
		};
    }
    
    public void nextColor(){
    	if(currentColor == Constants.Color_WHITE){
    		currentColor = Constants.Color_BLACK;
    		timeRemainingBlack = timeInSec;
    		startTime = getTime(TimeUnit.SECONDS);
    	}else{
    		currentColor = Constants.Color_WHITE;
    		timeRemainingWhite = timeInSec;
    		startTime = getTime(TimeUnit.SECONDS);
    	}
    }
    
    private void timeUp(String color){
    	refreshTimer.cancel();
    	stop();
    	//TODO Spiel beenden
    }
    
    private String getTime_H_Min_Sek(long currentTime){
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
        
        return strH+":"+strMin+":"+strSek;
    }
    
}
