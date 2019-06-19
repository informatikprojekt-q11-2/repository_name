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
    private GameOverlay overlay;
    private String currentColor=Constants.Color_WHITE;
    
    /**
     * @author Niklas
     * @param logic
     * @param overlay
     * @param timeInSec if <code>timeInSec = 0</code>, the game time won't be limited
     */
    public Timer(GameLogic logic, GameOverlay overlay, long timeInSec){
        refreshTimer = new java.util.Timer();
    	this.overlay = overlay;
        if(timeInSec > 0){
            //TODO  Größen anpassen
        	timeWhite = new JLabel(getTime_H_Min_Sek(timeInSec));
        	timeWhite.setBounds(1000-250, 25, 250, 25);
        	timeBlack = new JLabel(getTime_H_Min_Sek(timeInSec));
        	timeBlack.setBounds(1000-250, 50, 250, 25);
        	playingTime = new JLabel("00:00:00");
        	playingTime.setBounds(1000-250, 0, 250, 25);
        	overlay.add(playingTime);
        	overlay.add(timeWhite);
        	overlay.add(timeBlack);
            this.logic = logic;
            this.timeInSec = timeRemainingWhite = timeRemainingBlack = timeInSec;
            
            limitedClock();
        }else{
        	playingTime = new JLabel("00:00:00");
        	playingTime.setBounds(1000-250, 0, 250, 25);
        	overlay.add(playingTime);
            unlimitedClock();
        }
        
    }
    
    /**
     * 
     * Starts the Timer and changes the attribute <code>currentColor</code> after every move
     * @author Niklas
     */
    public void clock(){
        if(!isStarted()){
            start();
            refreshTimer.scheduleAtFixedRate(refreshTask, 999, 999);
        }
        nextColor();
    }
    
    /**
     * creates a TimerTask for the gamemode with unlimited time
     * @author Niklas
     */
    private void unlimitedClock(){
        refreshTask = new TimerTask(){
            @Override
            public void run() {
                long currentTime = getTime(TimeUnit.SECONDS);
                playingTime.setText(getTime_H_Min_Sek(currentTime));
                overlay.repaint();
            }
                
        };
    }
    
    /**
     * creates a TimerTask for the gamemode with limited time
     * @author Niklas
     */
    private void limitedClock(){
    	refreshTask = new TimerTask() {
			@Override
			public void run() {
				//gesamte Spielzeit
				long currentTime = getTime(TimeUnit.SECONDS);
				if(currentColor == Constants.Color_WHITE){
					if(timeRemainingWhite==0){
						timeUp(Constants.Color_WHITE);
						return;
					}
					timeRemainingWhite = timeInSec - currentTime + startTime;
				}else{
					if(timeRemainingBlack==0){
						timeUp(Constants.Color_BLACK);
						return;
					}
					timeRemainingBlack = timeInSec - currentTime + startTime;
				}
				playingTime.setText(getTime_H_Min_Sek(currentTime));
				timeWhite.setText(getTime_H_Min_Sek(timeRemainingWhite));
				timeBlack.setText(getTime_H_Min_Sek(timeRemainingBlack));
				overlay.repaint();
                //System.out.println("Spielzeit: "+getTime_H_Min_Sek(currentTime)+ "    Spielzeit weiï¿½:"+getTime_H_Min_Sek(timeRemainingWhite)+"    Spielzeit schwarz:"+getTime_H_Min_Sek(timeRemainingBlack));
			}
		};
    }
    
    /**
     * sets the next color and the startTime of the current move
     * @author Niklas
     */
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
    
    /**
     * Method used for canceling the game after one player's time is 0 
     * @author Niklas
     * @param color The color specifies which color's time is up
     */
    private void timeUp(String color){
    	refreshTimer.cancel();
    	stop();
    	if(color==Constants.Color_WHITE){
    		System.out.println("Schwarz hat gewonnen!");
    		overlay.gameOver(Constants.Reason_TimeUp, Constants.Color_BLACK);
    	}else{
    		System.out.println("Weiß hat gewonnen!");
    		overlay.gameOver(Constants.Reason_TimeUp, Constants.Color_WHITE);
    	}
    }
    
    /**
     * @author Niklas
     * @param currentTime Time in seconds which should be formatted
     * @return Returns a <code>String</code> formatted in HH:MM:SS
     */
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
