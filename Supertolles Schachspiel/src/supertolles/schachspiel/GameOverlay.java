/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import supertolles.schachspiel.gui.Game;


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
    Protocol protocol;
    //0=white 1= black | 0=pawn, 1=knight, 2=bishop, 3=rook, 4=queen, 5=king
    ImageIcon[][] skins;
    private Game game;
    
    public GameOverlay(int timeInSec, int boardLength, Game g){
        int buttonHeight = 80;
        game = g;
        
        setLayout(null);
        gamestate = Constants.WHITE_TO_SELECT;
        logic = new GameLogic(this, boardLength);
        timer = new Timer(logic, this, timeInSec);
        playingField = new JPanel(null);
        playingField.setBounds(0, 0, boardLength*buttonHeight, boardLength*buttonHeight);
        currentMoveOptions = new ArrayList<Integer[]>();
        protocol = new Protocol(this);
        board = new ChessButton[boardLength][boardLength];
        for(int y = 0; y < boardLength; y++){
            for(int x = 0; x < boardLength; x++){
                ChessButton button = new ChessButton(x, y);
                if((x+y)%2 == 0){
                    button.setBackground(new Color(243, 234, 226));
                }else{
                    button.setBackground(new Color(183, 123, 79));
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
						UIManager.put("Button.select", (((button.getBoardCoordinates())[0]+(button.getBoardCoordinates())[1])%2 == 0) ? new Color(243, 234, 226):new Color(183, 123, 79));
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
        
        surrender = new JButton("Surrender");
        surrender.setBounds(660, 150, 100, 100);
        surrender.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOver(Constants.Reason_Surrender, (gamestate == Constants.BLACK_TO_MOVE || gamestate == Constants.BLACK_TO_MOVE) ? Constants.Color_WHITE: Constants.Color_BLACK);
			}
        });
        surrender.setFocusable(false);
        surrender.setBorderPainted(false);
        surrender.setContentAreaFilled(false);
        add(surrender);
        
        createScaledImages();

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
                protocol.updateProtocol(logic.getBoard()[clickedY][clickedX], logic.getBoard()[y][x] != null, false);
                logic.movePiece(new int[]{clickedX, clickedY}, x, y);
                
                for(int i = 0; i<currentMoveOptions.size();i++){
                    if((currentMoveOptions.get(i)[0] + currentMoveOptions.get(i)[1])%2 == 0){
                        board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(new Color(243, 234, 226));
                    }
                    else{
                        board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(new Color(183, 123, 79));
                    }
                }
                if(y == 0 && logic.getBoard()[y][x].getType() == Constants.PAWN){
                	upgradePiece(x, y);
                	updateBoard();
                }else{
                    updateGamestate();
                    update();
                    timer.clock();
                }
                
            }
        }
        if(!isMoveOption){
            if(logic.getBoard()[y][x] != null){
                if(((gamestate == Constants.WHITE_TO_MOVE || gamestate == Constants.WHITE_TO_SELECT) && logic.getBoard()[y][x].getColour() == Constants.Color_WHITE) || 
                   ((gamestate == Constants.BLACK_TO_MOVE || gamestate == Constants.BLACK_TO_SELECT) && logic.getBoard()[y][x].getColour() == Constants.Color_BLACK)){
                	for(int i = 0; i<currentMoveOptions.size();i++){
                         if((currentMoveOptions.get(i)[0] + currentMoveOptions.get(i)[1])%2 == 0){
                             board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(new Color(243, 234, 226));
                         }
                         else{
                             board[currentMoveOptions.get(i)[1]][currentMoveOptions.get(i)[0]].setBackground(new Color(183, 123, 79));
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
     * This method updates the overlay and the logic
     * @author Niklas
     */
    public void update(){
    	//0: Pawn, 1: Knight, 2: Bishop, 3: Rook, 4: Queen, 5: King
    	HashMap<String, Integer> takenPieces = new HashMap<String, Integer>();
    	takenPieces.put(Constants.PAWN, 0);
    	takenPieces.put(Constants.KNIGHT, 0);
    	takenPieces.put(Constants.BISHOP, 0);
    	takenPieces.put(Constants.ROOK, 0);
    	takenPieces.put(Constants.QUEEN, 0);
    	takenPieces.put(Constants.KING, 0);
    	
    	for(int y = 0; y < board.length; y++){
    		for(int x = 0; x < board.length; x++){
    			if(logic.getBoard()[y][x]!=null){
            		switch(logic.getBoard()[y][x].getColour()){
            		case Constants.Color_WHITE:
            			switch(logic.getBoard()[y][x].getType()){
                		case Constants.PAWN:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		case Constants.KNIGHT:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		case Constants.BISHOP:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		case Constants.ROOK:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		case Constants.QUEEN:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		case Constants.KING:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())+1);
                			break;
                		}
            			break;
            		case Constants.Color_BLACK:
            			switch(logic.getBoard()[y][x].getType()){
                		case Constants.PAWN:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		case Constants.KNIGHT:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		case Constants.BISHOP:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		case Constants.ROOK:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		case Constants.QUEEN:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		case Constants.KING:
                			takenPieces.put(logic.getBoard()[y][x].getType(), takenPieces.get(logic.getBoard()[y][x].getType())-1);
                			break;
                		}
            			break;
            		}
    			}
        	}
    	}
    	
    	//Update von der Anzeige der geschlagenen Figuren
    	for(int i=0; i<piecesWhite.getComponentCount();i++){
    		piecesWhite.remove(i);
    	}
    	for(int i=0; i<piecesBlack.getComponentCount();i++){
    		piecesBlack.remove(i);
    	}
    	if(piecesWhite.getComponentCount()>0){
    		for(int i=0; i<piecesWhite.getComponentCount();i++){
        		piecesWhite.remove(i);
        	}
    	}
    	if(piecesBlack.getComponentCount()>0){
    		for(int i=0; i<piecesBlack.getComponentCount();i++){
        		piecesBlack.remove(i);
        	}
    	}
    	
    	for(int i=0; i< takenPieces.size(); i++){
    		if(takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i])<0){
    			for(int j=takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i]); j < 0; j++){
    				addTakenPieceToOverlay(takenPieces.keySet().toArray(new String[takenPieces.size()])[i], Constants.Color_WHITE);
    			}
    		}else{
    			for(int j=takenPieces.get(takenPieces.keySet().toArray(new String[takenPieces.size()])[i]); j > 0; j--){
    				addTakenPieceToOverlay(takenPieces.keySet().toArray(new String[takenPieces.size()])[i], Constants.Color_BLACK);
    			}
    		}
    	}
    	piecesWhite.repaint();
    	piecesBlack.repaint();
    	
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
    	ImageIcon img;
    	if(color == Constants.Color_BLACK){
    		ImageIcon whiteIcon=null;
    		switch(type){
    		case Constants.PAWN:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackPawn));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.KNIGHT:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKnight));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.BISHOP:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackBishop));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.ROOK:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackRook));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.QUEEN:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.KING:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKing));
    			whiteIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		}
    		JLabel whitePiece = new JLabel(whiteIcon);
    		//TODO Gr��e anpassen
    		whitePiece.setBounds(75*piecesWhite.getComponentCount(), 75*(piecesWhite.getComponentCount()/10), 75, 75);
    		piecesWhite.add(whitePiece);
    	}else{
    		ImageIcon blackIcon=null;
    		switch(type){
    		case Constants.PAWN:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhitePawn));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.KNIGHT:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKnight));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.BISHOP:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteBishop));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.ROOK:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteRook));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.QUEEN:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteQueen));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		case Constants.KING:
    			img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKing));
    			blackIcon = new ImageIcon(img.getImage().getScaledInstance(75, 75, Image.SCALE_FAST));
    			break;
    		}
    		JLabel blackPiece = new JLabel(blackIcon);
    		//TODO Gr��e anpassen
    		blackPiece.setBounds(75*piecesBlack.getComponentCount(), 75*(piecesBlack.getComponentCount()/10), 75, 75);
    		piecesBlack.add(blackPiece);
    	}
    }
    
    /**
     * The method updates the buttons of the board
     * @author Niklas
     */
    void updateBoard(){
    	ImageIcon skin = null;
    	Image scaled = null;
    	for(int y = 0 ; y < board.length; y++){
    		for(int x = 0 ; x < board.length; x++){
        		Piece temp = logic.getBoard()[y][x];
        		if(temp != null){
        			switch(temp.getColour()){
        			case Constants.Color_WHITE:
        				switch(temp.getType()){
            			case Constants.PAWN:
            				board[y][x].setIcon(skins[0][0]);
                			break;
                		case Constants.KNIGHT:
            				board[y][x].setIcon(skins[0][1]);
                			break;
                		case Constants.BISHOP:
                			board[y][x].setIcon(skins[0][2]);
                			break;
                		case Constants.ROOK:
                			board[y][x].setIcon(skins[0][3]);
                			break;
                		case Constants.QUEEN:
                			board[y][x].setIcon(skins[0][4]);
            				break;
                		case Constants.KING:
                			board[y][x].setIcon(skins[0][5]);
                			break;
            			}
        				break;
        			case Constants.Color_BLACK:
        				switch(temp.getType()){
        				case Constants.PAWN:
        					board[y][x].setIcon(skins[1][0]);
            				break;
                		case Constants.KNIGHT:
                			board[y][x].setIcon(skins[1][1]);
            				break;
                		case Constants.BISHOP:
                			board[y][x].setIcon(skins[1][2]);
            				break;
                		case Constants.ROOK:
                			board[y][x].setIcon(skins[1][3]);
            				break;
                		case Constants.QUEEN:
                			board[y][x].setIcon(skins[1][4]);
            				break;
                		case Constants.KING:
                			board[y][x].setIcon(skins[1][5]);
            				break;
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
    
    /**
     * creates scaled ImageIcons for the buttons on the board
     * @author Niklas
     */
    private void createScaledImages(){
    	ImageIcon img;
    	if(board.length==8){
        	skins = new ImageIcon[2][6];
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhitePawn));
        	skins[0][0]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKnight));
        	skins[0][1]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteBishop));
        	skins[0][2]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteRook));
        	skins[0][3]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteQueen));
        	skins[0][4]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKing));
        	skins[0][5]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackPawn));
        	skins[1][0]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKnight));
        	skins[1][1]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackBishop));
        	skins[1][2]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackRook));
        	skins[1][3]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen));
        	skins[1][4]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	img = new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKing));
        	skins[1][5]=new ImageIcon(img.getImage().getScaledInstance(board[0][0].getWidth(), board[0][0].getHeight(), Image.SCALE_SMOOTH));
        	
    	}else{
    		//TODO falls eine weitere Figur eingef�gt wederden soll, hier einf�gen.
    		throw new IllegalArgumentException();
    	}
    }
    
    /**
     * @author Niklas
     * @param reason Reason why the game is over
     * @param color The winner of the game. if reason is <code>Constants.Reason_StaleMate</code> it is not necessary to specify a color
     */
    public void gameOver(int reason, String color){
    	for(int i=0; i<board.length;i++){
    		for(int j = 0;j<board.length;j++ ){
    			board[i][j].setEnabled(false);
    		}
    	}
    	
    	switch(reason){
    	case Constants.Reason_Surrender:
    		JOptionPane.showMessageDialog(null, color.toUpperCase()+" is victorious due to his opponent's surrender!");
    		game.getMenu().enableMainmenu(game);
    		game.remove(this);
    		game.repaint();
    		break;
    	case Constants.Reason_CheckMate:
    		JOptionPane.showMessageDialog(null, color.toUpperCase()+" is victorious!");
    		game.getMenu().enableMainmenu(game);
    		game.remove(this);
    		game.repaint();
    		break;
    	case Constants.Reason_StaleMate:
    		JOptionPane.showMessageDialog(null, "Stalemate! Nobody wins!");
    		game.getMenu().enableMainmenu(game);
    		game.remove(this);
    		game.repaint();
    		break;
    	case Constants.Reason_TimeUp:
    		JOptionPane.showMessageDialog(null, color.toUpperCase()+ " is victorious due to the expired time of the opponent!");
    		game.getMenu().enableMainmenu(game);
    		game.remove(this);
    		game.repaint();
    		break;
    	}
    }
    
    /**
     * Method creates a JDialog for choosing the type of piece to which the pawn (x, y) should be upgraded
     * @author Niklas
     * @param x x-coordinate on which the figure which should be upgraded is standing
     * @param y y-coordinate on which the figure which should be upgraded is standing
     */
    private void upgradePiece(int x, int y){
    	JDialog dialog = new JDialog(game);
    	dialog.setBounds(getWidth()/2-150, getHeight()/2-150, 300, 300);
    	dialog.setLocation(game.getX()+game.getWidth()/2-150, game.getY()+game.getHeight()/2-150);
    	dialog.getContentPane().setLayout(null);
    	dialog.setTitle("Upgrade Piece");
    	dialog.setIconImage(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_IconUpgrade)).getImage());
    	
    	Cursor c = new Cursor(Cursor.HAND_CURSOR);
    	
    	JButton knight = new JButton("");
    	knight.setBounds(15, 16, 100, 100);
    	knight.setContentAreaFilled(false);
    	knight.setBorderPainted(false);
    	knight.setCursor(c);
    	knight.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logic.getBoard()[y][x].setType(Constants.KNIGHT);
				updateBoard();
				dialog.dispose();
				remove(dialog);
				updateGamestate();
                update();
                timer.clock(); 
			}
    	});
		dialog.getContentPane().add(knight);
		
		JButton bishop = new JButton("");
		bishop.setBounds(169, 16, 100, 100);
		bishop.setContentAreaFilled(false);
		bishop.setBorderPainted(false);
		bishop.setCursor(c);
		bishop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logic.getBoard()[y][x].setType(Constants.BISHOP);
				updateBoard();
				dialog.dispose();
				remove(dialog);
				repaint();
				updateGamestate();
                update();
                timer.clock(); 
			}
    	});
		dialog.getContentPane().add(bishop);
		
		JButton rook = new JButton("");
		rook.setBounds(15, 132, 100, 100);
		rook.setContentAreaFilled(false);
		rook.setBorderPainted(false);
		rook.setCursor(c);
		rook.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logic.getBoard()[y][x].setType(Constants.ROOK);
				updateBoard();
				dialog.dispose();
				remove(dialog);
				repaint();
				updateGamestate();
                update();
                timer.clock(); 
			}
    	});
		dialog.getContentPane().add(rook);
		
		JButton queen = new JButton("");
		queen.setBounds(169, 132, 100, 100);
		queen.setContentAreaFilled(false);
    	queen.setBorderPainted(false);
    	queen.setCursor(c);
		queen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logic.getBoard()[y][x].setType(Constants.QUEEN);
				updateBoard();
				dialog.dispose();
				remove(dialog);
				repaint();
				updateGamestate();
                update();
                timer.clock(); 
			}
    	});
		dialog.getContentPane().add(queen);
		
		if(logic.getBoard()[y][x].getColour() == Constants.Color_WHITE){
			knight.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteKnight)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			bishop.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteBishop)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			rook.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteRook)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			queen.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_WhiteQueen)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
		}else{
			knight.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackKnight)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			bishop.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackBishop)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			rook.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackRook)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
			queen.setIcon(new ImageIcon(new ImageIcon(GameOverlay.class.getResource(Constants.Picture_BlackQueen)).getImage().getScaledInstance(100, 100, Image.SCALE_FAST)));
		}
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
    }
}
