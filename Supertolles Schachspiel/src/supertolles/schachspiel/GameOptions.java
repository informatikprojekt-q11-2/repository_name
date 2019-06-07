/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import javax.swing.JFrame;

/**
 *
 * @author natanael.hoza
 */
public class GameOptions {
    JFrame window;
    GameOverlay game;
    
    
    public GameOptions(){
        game = new GameOverlay(0, 8);
        
        window = new JFrame();
        window.setBounds(0, 0, 1000, 1000);
        window.setContentPane(game);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
}
