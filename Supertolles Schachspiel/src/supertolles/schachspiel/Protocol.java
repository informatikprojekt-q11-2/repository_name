/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

/**
 *
 * @author natanael.hoza
 */
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author natanael.hoza
 */
public class Protocol {
    
    JTextArea textArea;
    JScrollPane scrollPane;
    int moveNumber;
    GameLogic logic;
    GameOverlay overlay;
    
    public Protocol (GameOverlay game){
        
        textArea = new JTextArea("Ein neues Schachspiel hat begonnen." + "\n", 1, 1);
        textArea.setEnabled(true);
        textArea.setEditable(false);
        textArea.setBounds(0, 0, 360, 1000);
        textArea.setTabSize(6);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(game.playingField.getX()/2 - ((game.playingField.getX()-40 > 360) ? 360 : game.playingField.getX()-40)/2, game.playingField.getY()+game.playingField.getHeight()/2-125, (game.playingField.getX()-40 > 360) ? 360 : game.playingField.getX()-40, 250);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        moveNumber = 1;
        overlay = game;
        logic = overlay.logic;
        overlay.add(scrollPane);
    }
    
    public void updateProtocol(Piece movedPiece, boolean pieceCaptured, boolean rochade){
        
        
        if(Constants.Color_WHITE.equals(movedPiece.getColour())){
         textArea.append(moveNumber + ".Zug: " + "\t");
        } else {
            moveNumber++;
        }
        
        //Rochade-Notation
        if(rochade){
                if (rochade == true && (movedPiece.getX()== 2)){
                    textArea.append("O-O-O");
                }
                if (rochade == true && (movedPiece.getX()== 6)){
                    textArea.append("O-O");
        }} else {
      
        switch(movedPiece.getType()){
            
            case Constants.KING:
                textArea.append("K");
                break;
            case Constants.QUEEN:
                textArea.append("Q");  
                break;
            case Constants.ROOK:
                textArea.append("R");
                break;
            case Constants.BISHOP:
               textArea.append("B");
                break;
            case Constants.KNIGHT:
               textArea.append("N");
                break;
            case Constants.PAWN:
               textArea.append(" ");
                break;
        }
         if(pieceCaptured == true){
             textArea.append("x");
         }
         
        String[] k = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if(movedPiece.getColour() == Constants.Color_WHITE){
            textArea.append(k[movedPiece.getX()] + Integer.toString(logic.boardlength-movedPiece.getY()));
        }else{
        textArea.append( k[movedPiece.getX()] + Integer.toString(movedPiece.getY()+1));            
        }
        
        //TODO ein + wird herausgegeben wenn der gegnerische König nach dem Zug bedroht wurde
        for (int i=0; i<logic.boardlength; i++){
           for (int j=0; j<logic.boardlength; j++){
               if(logic.board[i][j] != null && logic.board[i][j].getType()== Constants.KING && logic.board[i][j].getColour() != movedPiece.getColour()){
                   if (logic.isCheck(j, i)){
                       int prevGs = overlay.gamestate;
                       overlay.updateGamestate();
                       logic.flipBoard();
                       if(logic.isCheckMate()){
                           textArea.append("#"+"\t");
                       }else{
                           textArea.append("+"+"\t");
                       }
                       overlay.gamestate = prevGs;
                       logic.flipBoard();
                   } else {
                       textArea.append("\t");
                       break;
                   }
               }
           } 
        }
        if(movedPiece.getColour()==Constants.Color_BLACK){
           textArea.append("\n"); 
        }
        }
            
    }
    
}
