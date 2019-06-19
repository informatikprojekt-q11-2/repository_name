/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import supertolles.schachspiel.gui.Game;

/**
 *
 * @author natanael.hoza
 */
public class GameOptions {
	JPanel setupOverlay;
    GameOverlay game;
    JLabel head, time;
    JTextField timeTxt;
    JButton cancel, start;
    
    public GameOptions(Game g){
    	
    }
    
    public void enableGameOptions(Game g){
    	Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    	setupOverlay = new JPanel();
    	setupOverlay.setBounds(0, 0, g.getWidth(), g.getHeight());
    	setupOverlay.setLayout(null);
    	
    	head = new JLabel("Setup");
		head.setBounds(setupOverlay.getWidth()/2, setupOverlay.getHeight()/100, setupOverlay.getWidth()/6, setupOverlay.getHeight()/12);
		head.setBounds(head.getX()-head.getWidth()/2, head.getY(), head.getWidth(), head.getHeight());
		head.setFont(new Font("Tahoma", Font.BOLD, (int) (head.getHeight()*0.5)));
		head.setVisible(true);
		setupOverlay.add(head);
		
		time = new JLabel("Time per player: ");
		time.setBounds(head.getWidth()/2, head.getHeight()*3, setupOverlay.getWidth()/8, setupOverlay.getHeight()/17);
		time.setFont(new Font("Tahoma", Font.PLAIN, (int) (time.getHeight()*0.4)));
		time.setVisible(true);
		setupOverlay.add(time);
		
		timeTxt = new JTextField();
		timeTxt.setBounds(time.getX()+time.getWidth(), time.getY()+time.getHeight()/5, setupOverlay.getWidth()/9, setupOverlay.getHeight()/25);
		timeTxt.setFont(time.getFont());
		timeTxt.setVisible(true);
		timeTxt.setText("HH:MM:SS");
		setupOverlay.add(timeTxt);
		
		start = new JButton("Start");
		start.setBounds(timeTxt.getX()+timeTxt.getWidth()+timeTxt.getWidth()/2, timeTxt.getY()+timeTxt.getHeight()+timeTxt.getHeight()/2, timeTxt.getWidth()+(int)(timeTxt.getWidth()*0.3), timeTxt.getHeight()+timeTxt.getHeight()/2);
		start.setCursor(cursor);
		start.setFont(new Font("Tahoma", Font.PLAIN, (int) (start.getHeight()*0.4)));
		start.setContentAreaFilled(false);
		start.setBorderPainted(false);
		start.setFocusable(false);
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				disableGameOptions(g);
				GameOverlay ov = new GameOverlay(getTimeInSeconds(timeTxt.getText()), 8);
				ov.setBounds(0, 0, g.getWidth(), g.getHeight());
				g.add(ov);
				g.getContentPane().repaint();
				g.repaint();
			}
		});
		setupOverlay.add(start);
		
		cancel= new JButton("Cancel");
		cancel.setBounds(start.getX(), start.getY()+start.getHeight()+start.getHeight()/5, start.getWidth(), start.getHeight());
		cancel.setCursor(cursor);
		cancel.setFont(start.getFont());
		cancel.setContentAreaFilled(false);
		cancel.setBorderPainted(false);
		cancel.setFocusable(false);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				disableGameOptions(g);
				g.getMenu().enableMainmenu(g);
				g.getContentPane().repaint();
				g.repaint();
			}
		});
		setupOverlay.add(cancel);
		
		g.add(setupOverlay);
		
    }
    
    private int getTimeInSeconds(String s){
    	if(s==""){
    		return 0;
    	}
    	if(!s.contains(":")){
    		try{
    			return Integer.parseInt(s);
    		}catch(Exception e){
    			return 0;
    		}
    	}else{
    		String[] str = s.split(":");
    		if(str.length==3){
    			int h, min, sek;
    			try{
        			h = Integer.parseInt(str[0]);
        		}catch(Exception e){
        			h=0;
        		}
    			try{
        			min = Integer.parseInt(str[1]);
        		}catch(Exception e){
        			min=0;
        		}
    			try{
        			sek = Integer.parseInt(str[2]);
        		}catch(Exception e){
        			sek=0;
        		}
    			System.out.println(sek+ min*60 + h*3600);
    			return sek+ min*60 + h*3600;
    			
    		}else{
    			if(str.length==2){
    				int h, min;
    				try{
            			h = Integer.parseInt(str[0]);
            		}catch(Exception e){
            			h=0;
            		}
        			try{
            			min = Integer.parseInt(str[1]);
            		}catch(Exception e){
            			min=0;
            		}
        			return min*60 + h*3600;
    			}
    		}
    	}
    	
    	return 0;
    }
    
    public void disableGameOptions(Game g){
    	g.getContentPane().remove(setupOverlay);
    	setupOverlay.setVisible(false);
    	setupOverlay=null;
    }
}
