/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author natanael.hoza
 */
public class GameOverlay extends JPanel{
    
    public class ChessButton extends JButton{
        int boardX,boardY;
        
        //eigene ButtonKlasse mit dem zusatz von Spielfeldkoordinaten
        public ChessButton(int x, int y){
            super();
            boardX = x;
            boardY = y;
        }
        
        public int[] getBoardCoordinates(){
            return new int[]{boardX,boardY};
        }
    }
    
    GameLogic logic;
    int gamestate, clickedX, clickedY;
    ArrayList<Integer[]> currentMoveOptions;
    ChessButton[][] board;
    
    JPanel piecesWhite, piecesBlack, playingField;
    JButton surrender;
    Timer timer;
    
    public GameOverlay(int timeInMs, int boardLength){
        int buttonHeight = 80;
        
        setLayout(null);
        
        logic = new GameLogic(this, boardLength);
        timer = new Timer(logic, this, timeInMs);
        playingField = new JPanel(null);
        playingField.setBounds(0, 0, boardLength*buttonHeight, boardLength*buttonHeight);
        
        board = new ChessButton[boardLength][boardLength];
        for(int x = 0; x < boardLength; x++){
            for(int y = 0; y < boardLength; y++){
                ChessButton button = new ChessButton(x, y);
                if((x+y)%2 == 0){
                    button.setBackground(Color.WHITE);
                }else{
                    button.setBackground(Color.BLACK);
                }
                
                button.setBounds(x*buttonHeight, y*buttonHeight, buttonHeight, buttonHeight);
                button.setFocusable(false);
                button.setBorderPainted(false);
                button.setRolloverEnabled(false);
                //TO DO: Farbwechsel bei Klick entfernen ?   
                button.setVisible(true);
                playingField.add(button);
                
                board[x][y] = button;
                
            }
        }
        
        add(playingField);
    }
    
}
