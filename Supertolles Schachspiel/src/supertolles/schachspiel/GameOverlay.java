/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javafx.scene.transform.Scale;

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
    
    public GameOverlay(int timeInSec, int boardLength){
        int buttonHeight = 80;
        
        setLayout(null);
        
        gamestate = Constants.WHITE_TO_SELECT;
        logic = new GameLogic(this, boardLength);
        timer = new Timer(logic, this, timeInSec);
        playingField = new JPanel(null);
        playingField.setBounds(0, 0, boardLength*buttonHeight, boardLength*buttonHeight);
        currentMoveOptions = new ArrayList<Integer[]>();
        
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
                button.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent e) {
					}
					@Override
					public void mousePressed(MouseEvent e) {
						UIManager.put("Button.select", (((button.getBoardCoordinates())[0]+(button.getBoardCoordinates())[1])%2 == 0) ? Color.WHITE:Color.BLACK);
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
                });

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
        
        takenPiecesWhite = new ArrayList<Piece>();
        takenPiecesBlack = new ArrayList<Piece>();
        
        
        piecesWhite = new JPanel();
        piecesWhite.setBounds(80*boardLength+80*2, 80*boardLength, 75*11, (int)(75*2.2));
        piecesWhite.setLayout(null);
        piecesWhite.setVisible(true);
        piecesWhite.setBackground(Color.WHITE);
        add(piecesWhite);
        
        piecesBlack = new JPanel();
        piecesBlack.setBounds(80*boardLength+80*2, 80, 75*11, (int)(75*2.2));
        piecesBlack.setLayout(null);
        piecesBlack.setVisible(true);
        piecesBlack.setBackground(Color.BLACK);
        add(piecesBlack);
        
        updateBoard();
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
            	if(logic.board[y][x] != null){
            		if(logic.board[y][x].getColour() == Constants.Color_WHITE){
            			takenPiecesWhite.add(logic.board[y][x]);
            		}else{
            			takenPiecesBlack.add(logic.board[y][x]);
            		}
            	}
                logic.movePiece(new int[]{clickedX, clickedY}, x, y);
                timer.clock(); 
                update();
                
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
                	for(int i = 0; i<currentMoveOptions.size();i++){
                         if((currentMoveOptions.get(i)[0] + currentMoveOptions.get(i)[1])%2 == 0){
                             board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.WHITE);
                         }
                         else{
                             board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(Color.BLACK);
                         }
                    }
                    currentMoveOptions = logic.computeMoveOptions(x,y);
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
    	HashMap<String, Integer> takenPieces = new HashMap<String, Integer>();
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
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		case Constants.KNIGHT:
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		case Constants.BISHOP:
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		case Constants.ROOK:
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		case Constants.QUEEN:
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		case Constants.KING:
    			takenPieces.put(takenPiecesBlack.get(i).getType(), takenPieces.get(takenPiecesBlack.get(i).getType())-1);
    			break;
    		}
    	}
    	
    	//Update von der Anzeige der geschlagenen Figuren
    	for(int i=0; i<piecesWhite.getComponentCount();i++){
    		piecesWhite.remove(i);
    	}
    	for(int i=0; i<piecesBlack.getComponentCount();i++){
    		piecesBlack.remove(i);
    	}
    	for(int i=0; i< takenPieces.size(); i++){
    		if(takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i])<0){
    			for(int j=takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i]); j < 0; j++){
    				addTakenPieceToOverlay(takenPieces.keySet().toArray(new String[takenPieces.size()])[i], Constants.Color_BLACK);
    			}
    		}else{
    			for(int j=takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i]); j > 0; j--){
    				addTakenPieceToOverlay(takenPieces.keySet().toArray(new String[takenPieces.size()])[i], Constants.Color_WHITE);
    			}
    		}
    	}
    	updateBoard();
    	//Kurzer Delay nach gemachtem Zug, bevor sich das Spielfeld umdreht
/*    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/    	logic.nextMove();
    	updateBoard();
    	
    }
    
    /**
     * The method adds a taken piece dependent on the parameters to a specific <code>JPanel</code> on the Overlay
     * @author Niklas
     * @param type Type of the taken Piece
     * @param color Color of the taken Piece
     */
    private void addTakenPieceToOverlay(String type, String color){
    	if(color == Constants.Color_BLACK){
    		ImageIcon whiteIcon=null;
    		//TODO URL zu Bildern einfügen
    		switch(type){
    		case Constants.PAWN:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackPawn));
    			break;
    		case Constants.KNIGHT:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKnight));
    			break;
    		case Constants.BISHOP:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackBishop));
    			break;
    		case Constants.ROOK:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackRook));
    			break;
    		case Constants.QUEEN:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen));
    			break;
    		case Constants.KING:
    			whiteIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKing));
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
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhitePawn));
    			break;
    		case Constants.KNIGHT:
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKnight));
    			break;
    		case Constants.BISHOP:
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteBishop));
    			break;
    		case Constants.ROOK:
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteRook));
    			break;
    		case Constants.QUEEN:
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteQueen));
    			break;
    		case Constants.KING:
    			blackIcon = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKing));
    			break;
    		}
    		JLabel blackPiece = new JLabel(blackIcon);
    		//TODO Größe anpassen
    		blackPiece.setBounds(75*piecesBlack.getComponentCount(), 75*(piecesBlack.getComponentCount()/10), 75, 75);
    		piecesBlack.add(blackPiece);
    	}
    }
    
    /**
     * The method updates the buttons of the board
     * @author Niklas
     */
    //TODO URL zu den Icons einfügen!!!!
    void updateBoard(){
    	ImageIcon skin = null;
    	Image scaled = null;
    	for(int y = 0 ; y < board.length; y++){
    		for(int x = 0 ; x < board.length; x++){
        		Piece temp = logic.board[y][x];
        		if(temp != null){
        			switch(temp.getColour()){
        			case Constants.Color_WHITE:
        				switch(temp.getType()){
            			case Constants.PAWN:
            				skin = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhitePawn));
            				scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);
                			break;
                		case Constants.KNIGHT:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKnight));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);
                			break;
                		case Constants.BISHOP:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteBishop));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);
                			break;
                		case Constants.ROOK:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteRook));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);
                			break;
                		case Constants.QUEEN:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteQueen));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.KING:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKing));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
            			}
        				break;
        			case Constants.Color_BLACK:
        				switch(temp.getType()){
        				case Constants.PAWN:
        					skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackPawn));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.KNIGHT:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKnight));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.BISHOP:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackBishop));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.ROOK:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackRook));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.QUEEN:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
                		case Constants.KING:
                			skin =new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKing));
                			scaled = skin.getImage().getScaledInstance(board[y][x].getWidth(), board[y][x].getHeight(), Image.SCALE_DEFAULT);
            				skin.setImage(scaled);
            				board[y][x].setIcon(skin);break;
            			}
        				break;
        			}

        		}else{
        			board[y][x].setIcon(null);
        		}
        	}
    	}
    	repaint();
    }
    
}
