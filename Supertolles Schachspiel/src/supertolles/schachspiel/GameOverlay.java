/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
    ArrayList<Piece> takenPiecesWhite, takenPiecesBlack;
    
    JPanel piecesWhite, piecesBlack, playingField;
    JButton surrender;
    Timer timer;
    Protocol protocol;
    
    public GameOverlay(int timeInMs, int boardLength){
        int buttonHeight = 80;
        
        setLayout(null);
        
        gamestate = Constants.WHITE_TO_SELECT;
        logic = new GameLogic(this, boardLength);
        //timer = new Timer(logic, this, timeInMs);
        playingField = new JPanel(null);
        playingField.setBounds(0, 0, boardLength*buttonHeight, boardLength*buttonHeight);
        
        board = new ChessButton[boardLength][boardLength];
        for(int y = 0; y < boardLength; y++){
            for(int x = 0; x < boardLength; x++){
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
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clicked((button.getBoardCoordinates())[0], (button.getBoardCoordinates())[1]);
                    }
                });
                
                button.setVisible(true);
                playingField.add(button);
                
                board[y][x] = button;
                
            }
        }
        
        add(playingField);
        
        currentMoveOptions = new ArrayList<Integer[]>();
        currentMoveOptions.add(new Integer[]{3,5,0});
        currentMoveOptions.add(new Integer[]{4,5,0});
        currentMoveOptions.add(new Integer[]{5,5,0});
        currentMoveOptions.add(new Integer[]{6,5,1});
        
        
        takenPiecesWhite = new ArrayList<Piece>();
        takenPiecesBlack = new ArrayList<Piece>();
    }
    
    
    /**
     * @author Alexander
     * 
     *  depending on the current gamestate the method either initiates the computing of the moveoptions of the piece on the clicked field
     *  or it initiates the moving of the figure
     * 
     * @param x: x-Coordinate of the clicked button
     * @param y: y-Coordinate of the clicked button 
     */
    public void clicked(int x, int y){
        boolean isMoveOption = false;
        
        if(gamestate == Constants.WHITE_TO_MOVE || gamestate == Constants.BLACK_TO_MOVE){
            
            for(int i = 0; i < currentMoveOptions.size(); i++){
                if(currentMoveOptions.get(i)[0] == x && currentMoveOptions.get(i)[1] == y){
                    isMoveOption = true;
                }
            }
            
            if(isMoveOption){
                logic.movePiece(new int[]{clickedX, clickedY}, x, y);
                //update();
                
                for(int i = 0; i<currentMoveOptions.size();i++){
                    if((currentMoveOptions.get(i)[0] + currentMoveOptions.get(i)[1])%2 == 0){
                        board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.WHITE);
                    }
                    else{
                        board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.BLACK);
                    }
                }
                
                updateGamestate();
                System.out.println(gamestate);
            }
        }
        if(!isMoveOption){
            if(logic.board[y][x] != null){
                if(((gamestate == Constants.WHITE_TO_MOVE || gamestate == Constants.WHITE_TO_SELECT) && logic.board[y][x].getColour() == Constants.Color_WHITE) || 
                   ((gamestate == Constants.BLACK_TO_MOVE || gamestate == Constants.BLACK_TO_SELECT) && logic.board[y][x].getColour() == Constants.Color_BLACK)){
                    //currentMoveOptions = logic.computeMoveOptions(x,y);
                    clickedX = x;
                    clickedY = y;

                    for(int i = 0; i<currentMoveOptions.size();i++){
                        if(currentMoveOptions.get(i)[2] == Constants.FALSE){
                            board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.blue);
                        }
                        else{
                            board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.red);
                        }
                    }
                    repaint();
                    if(gamestate == Constants.WHITE_TO_SELECT || gamestate == Constants.BLACK_TO_SELECT){
                        updateGamestate();
                    }
                    System.out.println(gamestate);
                }
            }
        }
    }
    
    public void updateGamestate(){
        if(gamestate == Constants.BLACK_TO_MOVE){
            gamestate = Constants.WHITE_TO_SELECT;
        }
        else{
            gamestate++;
        }
    }
    
    /**
     * @author Niklas
     */
    //TODO KLASSE PROTOCOL MUSS GEMACHT WERDEN!
    public void update(){
/*    	//protocol.protocol() ??
    	for(int y = 0; y < board.length; y++){
    		for(int x = 0; x < board.length; x++){
        		//Irgendetwas mit board[][] machen
        	}
    	}
*/    	
    	
    	//0: Pawn, 1: Knight, 2: Bishop, 3: Rook, 4: Queen, 5: King
    	//int[] takenPieces = new int[6];
    	HashMap<String, Integer> takenPieces = new HashMap();
    	takenPieces.put(Constants.PAWN, 0);
    	takenPieces.put(Constants.KNIGHT, 0);
    	takenPieces.put(Constants.BISHOP, 0);
    	takenPieces.put(Constants.ROOK, 0);
    	takenPieces.put(Constants.QUEEN, 0);
    	takenPieces.put(Constants.KING, 0);
    	for(int i=0; i<takenPiecesWhite.size(); i++){
    		switch(takenPiecesWhite.get(i).getType()){
    		case Constants.PAWN:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		case Constants.KNIGHT:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		case Constants.BISHOP:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		case Constants.ROOK:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		case Constants.QUEEN:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		case Constants.KING:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())+1);
    			break;
    		}
    	}
    	
    	for(int i=0; i<takenPiecesBlack.size(); i++){
    		switch(takenPiecesBlack.get(i).getType()){
    		case Constants.PAWN:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		case Constants.KNIGHT:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		case Constants.BISHOP:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		case Constants.ROOK:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		case Constants.QUEEN:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		case Constants.KING:
    			takenPieces.put(takenPiecesWhite.get(i).getType(), takenPieces.get(takenPiecesWhite.get(i).getType())-1);
    			break;
    		}
    	}
    	
    	//Update von der Anzeige der geschlagenen Figuren
    	for(int i=0; i< takenPieces.size(); i++){
    		if(takenPieces.get(takenPiecesWhite.get(i).getType())<0){
    			for(int j=takenPieces.get(takenPiecesWhite.get(i).getType()); j < 0; j++){
    				addTakenPieceToOverlay(((String[])takenPieces.keySet().toArray())[i], Constants.Color_BLACK);
    			}
    		}else{
    			for(int j=takenPieces.get(takenPiecesWhite.get(i).getType()); j > 0; j--){
    				addTakenPieceToOverlay(((String[])takenPieces.keySet().toArray())[i], Constants.Color_WHITE);
    			}
    		}
    	}
    	
    	logic.checkWinConditions();
    }
    
    /**
     * @author Niklas
     * @param type
     * @param color
     */
    private void addTakenPieceToOverlay(String type, String color){
    	if(color == Constants.Color_BLACK){
    		ImageIcon whiteIcon=null;
    		//TODO URL zu Bildern einfügen
    		switch(type){
    		case Constants.PAWN:
    			whiteIcon = new ImageIcon();
    			break;
    		case Constants.KNIGHT:
    			whiteIcon = new ImageIcon();
    			break;
    		case Constants.BISHOP:
    			whiteIcon = new ImageIcon();
    			break;
    		case Constants.ROOK:
    			whiteIcon = new ImageIcon();
    			break;
    		case Constants.QUEEN:
    			whiteIcon = new ImageIcon();
    			break;
    		case Constants.KING:
    			whiteIcon = new ImageIcon();
    			break;
    		}
    		JLabel whitePiece = new JLabel(whiteIcon);
    		//TODO Größe anpassen
    		whitePiece.setBounds(75*piecesWhite.getComponentCount(), 75*(piecesWhite.getComponentCount()/10), 75, 75);
    		piecesWhite.add(whitePiece);
    	}else{
    		ImageIcon blackIcon=null;
    		//TODO URL zu Bildern einfügen
    		switch(type){
    		case Constants.PAWN:
    			blackIcon = new ImageIcon();
    			break;
    		case Constants.KNIGHT:
    			blackIcon = new ImageIcon();
    			break;
    		case Constants.BISHOP:
    			blackIcon = new ImageIcon();
    			break;
    		case Constants.ROOK:
    			blackIcon = new ImageIcon();
    			break;
    		case Constants.QUEEN:
    			blackIcon = new ImageIcon();
    			break;
    		case Constants.KING:
    			blackIcon = new ImageIcon();
    			break;
    		}
    		JLabel blackPiece = new JLabel(blackIcon);
    		//TODO Größe anpassen
    		blackPiece.setBounds(75*piecesBlack.getComponentCount(), 75*(piecesBlack.getComponentCount()/10), 75, 75);
    		piecesBlack.add(blackPiece);
    	}
    }
}
