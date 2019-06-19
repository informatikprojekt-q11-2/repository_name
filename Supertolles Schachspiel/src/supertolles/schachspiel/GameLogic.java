/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

import java.util.ArrayList;

/**
 *
 * @author natanael.hoza
 */
public class GameLogic {
    
    public Piece[][] board;
    public GameOverlay game;
    final int boardlength;
    
    public GameLogic(GameOverlay g, int boardl){
        boardlength = boardl;
        board = new Piece[boardlength][boardlength];
        game = g;
        
        startPosition();
    }
    
    /**
     * @author Alexander
     */
    public void startPosition(){
        board[0][0] = new Piece(0, 0, Constants.Color_BLACK, Constants.ROOK);
        board[0][boardlength-1] = new Piece(boardlength-1, 0, Constants.Color_BLACK, Constants.ROOK);
        board[0][1] = new Piece(1, 0, Constants.Color_BLACK, Constants.KNIGHT);
        board[0][boardlength-2] = new Piece(boardlength-2, 0, Constants.Color_BLACK, Constants.KNIGHT);
        board[0][2] = new Piece(2, 0, Constants.Color_BLACK, Constants.BISHOP);
        board[0][boardlength-3] = new Piece(boardlength-3, 0, Constants.Color_BLACK, Constants.BISHOP);
        board[0][3] = new Piece(3, 0, Constants.Color_BLACK, Constants.QUEEN);
        board[0][4] = new Piece(4, 0, Constants.Color_BLACK, Constants.KING);
        for (int i = 0; i < boardlength; i++) {
            board[1][i] = new Piece(i, 1, Constants.Color_BLACK, Constants.PAWN);
        }
        
        board[boardlength-1][0] = new Piece(0, boardlength-1, Constants.Color_WHITE, Constants.ROOK);
        board[boardlength-1][boardlength-1] = new Piece(boardlength-1, boardlength-1, Constants.Color_WHITE, Constants.ROOK);
        board[boardlength-1][1] = new Piece(1, boardlength-1, Constants.Color_WHITE, Constants.KNIGHT);
        board[boardlength-1][boardlength-2] = new Piece(boardlength-2, boardlength-1, Constants.Color_WHITE, Constants.KNIGHT);
        board[boardlength-1][2] = new Piece(2, boardlength-1, Constants.Color_WHITE, Constants.BISHOP);
        board[boardlength-1][boardlength-3] = new Piece(boardlength-3, boardlength-1, Constants.Color_WHITE, Constants.BISHOP);
        board[boardlength-1][3] = new Piece(3, boardlength-1, Constants.Color_WHITE, Constants.QUEEN);
        board[boardlength-1][4] = new Piece(4, boardlength-1, Constants.Color_WHITE, Constants.KING);
        for (int i = 0; i < boardlength; i++) {
            board[boardlength-2][i] = new Piece(i, boardlength-2, Constants.Color_WHITE, Constants.PAWN);
        }   
    }
    
    public void printBoard(){
        for (int i = 0; i < boardlength; i++) {
            System.out.print("       " + i + "       "); 
        }
        System.out.println("");
        
        for(int i = 0; i < boardlength; i++){
            System.out.print(i + " ");
            for (int j = 0; j < boardlength; j++) {
                if(board[i][j] != null){
                    System.out.print(board[i][j].getType() + ", " + board[i][j].getColour() + " | ");
                }
                else{
                    System.out.print("     -      | ");
                }
            }
            System.out.println("");
        }
    }
    
    /**
     * @author Niklas
     */
    void flipBoard(){
        Piece[][] newBoard = new Piece[boardlength][boardlength];
        for(int y = 0 ; y < boardlength ; y++){
            for(int x = 0 ; x < boardlength ; x++){
                if(board[y][x] != null){
                    board[y][x].setCoordinates(x, boardlength-y-1);
                    //System.out.println(board[y][x].getType()+" "+board[y][x].getColour()+" "+ board[y][x].getX()+ " "+ board[y][x].getY());
                    newBoard[board[y][x].getY()][board[y][x].getX()] = board[y][x];
                }
            }
        }
        board = newBoard;
        
    }
    
    /**
     * @author Niklas
     */
    public void movePiece(int[] currentCoordinate, int x, int y){
        if(!board[currentCoordinate[1]][currentCoordinate[0]].getMoved()){
            board[currentCoordinate[1]][currentCoordinate[0]].setMoved(true);
        }
        board[y][x] = board[currentCoordinate[1]][currentCoordinate[0]];
        board[currentCoordinate[1]][currentCoordinate[0]] = null;
        board[y][x].setCoordinates(x, y);
    }
    
    void checkWinConditions(){
    	System.out.println(game.gamestate);
        if(isCheckMate()){
        	if(game.gamestate == Constants.BLACK_TO_MOVE || game.gamestate == Constants.BLACK_TO_SELECT){
        		game.gameOver(Constants.Reason_CheckMate, Constants.Color_WHITE);
        	}else{
        		game.gameOver(Constants.Reason_CheckMate, Constants.Color_BLACK);
        	}
        }else{
        	if(isStaleMate()){
        		game.gameOver(Constants.Reason_StaleMate, "");
        	}
        }
    }
    
    /**
     * @author Niklas
     */
    public boolean isCheckMate(){
        if(isCheck(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1]) 
        		&& computeMoveOptions(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1]).size() == 0){
        	Piece[] threatFigure = searchThreatFigure(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1]);
        	if(threatFigure.length > 1){
        		return true;
        	}
        	for(int i = 0 ; i < boardlength; i++){
        		for(int j = 0 ; j < boardlength; j++){
            		if(computeMoveOptions(i, j).size() > 0){
            			return false;
            		}
            	}
        	}
        }else{
            return false;
        }
        
        return true;
    }
    
    /**
     * @author Niklas
     */
    public boolean isCheck(int x, int y){
        if(searchThreatFigure(x, y).length > 0){
        	return true;
        }
        return false;
    }
    
    /**
     * @author Niklas
     */
    public boolean isStaleMate(){
        for(int y=0; y<boardlength ; y++){
        	for(int x=0; x<boardlength ; x++){
            	if(board[y][x] != null && computeMoveOptions(x, y) != null && computeMoveOptions(x, y).size() > 0){
            		return false;
            	}
            }
        }
        return true;
    }
    
    /**
     * @author Niklas
     * @param x X-Coordinate on which the investigated figure is standing
     * @param y Y-Coordinate on which the investigated figure is standing
     * @return returns all figures which are threatening the figure (x,y)
     */
    public Piece[] searchThreatFigure(int x, int y){
        ArrayList<Piece> figures = new ArrayList<Piece>();
        
        Piece[] temp = searchKnight(x, y, board);
        
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        temp = searchStraights(x, y, board);
        
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        temp = searchDiagonals(x, y, board);
        
        for(int i=0; i<temp.length; i++){
        	figures.add(temp[i]);
        }
        
        temp = searchKing(x, y, board);
        for(int i=0; i<temp.length; i++){
        	figures.add(temp[i]);
        }
        
        return arrayListToArray(figures);
    }
    
    /**
     * This method is needed for ArrayList<Integer[]> computeKingMovement(int x, int y) !
     * @author Niklas
     * @param x X-Coordinate on which the investigated figure would be standing
     * @param y Y-Coordinate on which the investigated figure would be standing
     * @param piece Piece which should be investigated on field (x,y) temporarily 
     * @return returns all figures which are threatening the figure (x,y)
     */
    public Piece[] searchThreatFigure(int x, int y, Piece piece){
    	Piece[][] tempBoard = new Piece[boardlength][boardlength];
        
        for(int i = 0; i< boardlength;i++){
            for (int j = 0; j < boardlength; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }
                
    	tempBoard[y][x] = piece;
    	tempBoard[piece.getY()][piece.getX()]=null;
        int xActually = piece.getX(), yActually = piece.getY();
    	tempBoard[y][x].setCoordinates(x, y);
    	
        ArrayList<Piece> figures = new ArrayList<Piece>();
        
        Piece[] temp = searchKnight(x, y, tempBoard);
        
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        temp = searchStraights(x, y, tempBoard);
        
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        temp = searchDiagonals(x, y, tempBoard);
        
        for(int i=0; i<temp.length; i++){
        	figures.add(temp[i]);
        }
        
        temp = searchKing(x, y, tempBoard);
        for(int i=0; i<temp.length; i++){
        	figures.add(temp[i]);
        }
        
        piece.setCoordinates(xActually, yActually);
        //board[yActually][xActually] = piece;
        return arrayListToArray(figures);
    }
    
    /**
     * @author Niklas
     * @param x
     * @param y
     * @return Returns all Knights which are threatening the figure with coordinates (x,y)
     */
    public Piece[] searchKnight(int x, int y, Piece[][] board){
        ArrayList<Piece> figures = new ArrayList<Piece>();
        try{
        	try{
        		
	            if(isInBounds(y+1, x+2) && board[y+1][x+2]!=null && board[y+1][x+2].getType() == Constants.KNIGHT 
	                && board[y+1][x+2].getColour() != board[y][x].getColour()){
	            figures.add(board[y+1][x+2]);
	            }
	            if(isInBounds(y-1, x+2) && board[y-1][x+2]!=null && board[y-1][x+2].getType() == Constants.KNIGHT 
	                    && board[y-1][x+2].getColour() != board[y][x].getColour()){
	                figures.add(board[y-1][x+2]);
	            }
	            if(isInBounds(y+1, x-2) && board[y+1][x-2]!=null && board[y+1][x-2].getType() == Constants.KNIGHT 
	                    && board[y+1][x-2].getColour() != board[y][x].getColour()){
	                figures.add(board[y+1][x-2]);
	            }
	            if(isInBounds(y-1, x-2) && board[y-1][x-2]!=null && board[y-1][x-2].getType() == Constants.KNIGHT 
	                    && board[y-1][x-2].getColour() != board[y][x].getColour()){
	                figures.add(board[y-1][x-2]);
	            }
	            if(isInBounds(y+2, x+1) && board[y+2][x+1]!=null && board[y+2][x+1].getType() == Constants.KNIGHT 
	                    && board[y+2][x+1].getColour() != board[y][x].getColour()){
	                figures.add(board[y+2][x+1]);
	            }
	            if(isInBounds(y-2, x+1) && board[y-2][x+1]!=null && board[y-2][x+1].getType() == Constants.KNIGHT 
	                    && board[y-2][x+1].getColour() != board[y][x].getColour()){
	                figures.add(board[y-2][x+1]);
	            }
	            if(isInBounds(y+2, x-1) && board[y+2][x-1]!=null && board[y+2][x-1].getType() == Constants.KNIGHT 
	                    && board[y+2][x-1].getColour() != board[y][x].getColour()){
	                figures.add(board[y+2][x-1]);
	            }
	            if(isInBounds(y-2, x-1) && board[y-2][x-1]!=null && board[y-2][x-1].getType() == Constants.KNIGHT 
	                    && board[y-2][x-1].getColour() != board[y][x].getColour()){
	                figures.add(board[y-2][x-1]);
	            }
	            
        	}catch(ArrayIndexOutOfBoundsException e0){
        		throw new IllegalArgumentException();
        	}
        	
        }catch(NullPointerException e1){
            throw new IllegalStateException();
        }
        
        return arrayListToArray(figures);
    }
    
    /**
     * @author Niklas
     */
    public Piece[] searchStraights(int x, int y, Piece[][] board){
        ArrayList<Piece> figures = new ArrayList<Piece>();
        //Vertikale absuchen
        for(int i = 0; i < boardlength; i++){
            if(y-i > 0 && board[i][x] != null){
                if(figures.size() == 0){
                    figures.add(board[i][x]);
                }else{
                    figures.set(0, board[i][x]);
                }
            }else{
                if(board[i][x] != null && i > y){
                    figures.add(board[i][x]);
                    break;
                }
            }
        }
        int size = figures.size();
        for(int i = 0; i < boardlength; i++){
            if(x-i > 0 && board[y][i] != null){
                if(figures.size() == size){
                    figures.add(board[y][i]);
                }else{
                    figures.set(size, board[y][i]);
                }
            }else{
                if(board[y][i] != null && i > x){
                    figures.add(board[y][i]);
                    break;
                }
            }
        }
        for(int i = 0; i < figures.size(); i++){
            if(figures.get(i) != null && !(figures.get(i).getColour() != board[y][x].getColour() 
                    && (figures.get(i).getType() == Constants.QUEEN || figures.get(i).getType() == Constants.ROOK))){
                figures.remove(i);
                i--;
            }
        }
        
        return arrayListToArray(figures);
    }

    public Piece[] searchDiagonals(int x, int y, Piece[][] board){
    	ArrayList<Piece> figures = new ArrayList<Piece>();
    	int x1, y1, sideDistance;
    	
    	if(x >= y){
    		sideDistance = y;
    	}else{
    		sideDistance = x;
    	}
    	x1 = x - sideDistance;
    	y1 = y - sideDistance;
    	
    	while(isInBounds(x1, y1)){
    		if(x1 < x && board[y1][x1]!= null){
    			if(figures.size()==0){
    				figures.add(board[y1][x1]);
    			}else{
    				figures.set(0, board[y1][x1]);
    			}
    		}else{
    			if(x1 > x && board[y1][x1] != null){
        			figures.add(board[y1][x1]);
        			break;
    			}
    		}
    		x1++;
    		y1++;
    	}
    	
    	if(boardlength-x >= y){
    		sideDistance = y;
    	}else{
    		sideDistance = boardlength-1-x;
    	}
    	
    	x1 = x + sideDistance;
    	y1 = y - sideDistance;
    	
    	int size = figures.size();
    	while(isInBounds(x1, y1)){
    		if(x1 > x && board[y1][x1]!= null){
    			if(figures.size()==size){
    				figures.add(board[y1][x1]);
    			}else{
    				figures.set(size, board[y1][x1]);
    			}
    		}else{
    			if(x1 < x && board[y1][x1] != null){
        			figures.add(board[y1][x1]);
        			break;
    			}
    		}
    		x1--;
    		y1++;
    	}
    	for(int i=0; i<figures.size(); i++){
    		if(!((figures.get(i).getType() == Constants.QUEEN || figures.get(i).getType() == Constants.BISHOP) 
    				&& board[y][x].getColour() != figures.get(i).getColour())){
                    if(!(figures.get(i).getType() == Constants.PAWN && board[y][x].getColour() != figures.get(i).getColour() && y==figures.get(i).getY()+1)){
                        figures.remove(i);
                        i--;
                    }
    		}
    	}
    	return arrayListToArray(figures);
    }
    
    public Piece[] searchKing(int x, int y, Piece[][] board){
    	for(int i=y-1; i<=y+1;i++){
    		for(int j=x-1; j<=x+1;j++){
        		if(isInBounds(j, i) && board[i][j] != null && board[i][j].getType() == Constants.KING && board[i][j].getColour() != board[y][x].getColour()){
        			return new Piece[]{board[i][j]};
        		}
        	}
    	}
    	return new Piece[]{};
    }
    
    /**
     * @author Niklas
     */
    public static Piece[] arrayListToArray(ArrayList<Piece> arrayList){
        Piece[] temp = new Piece[arrayList.size()];
        for(int i=0;i<temp.length; i++){
            temp[i]=arrayList.get(i);
        }
        return temp;
    }
    
    /**
     * @author Alexander
     */
    public ArrayList<Integer[]> computeMoveOptions(int x, int y){
        ArrayList<Integer[]> generalMovement = new ArrayList<Integer[]>();
        ArrayList<Integer[]> finalMovement = new ArrayList<Integer[]>();
        boolean[][] checkRestrictions = new boolean[boardlength][boardlength];
        boolean[][] pinRestrictions = new boolean[boardlength][boardlength];
        
        if(board[y][x] == null){
            return finalMovement;
        }else{
            if((game.gamestate == Constants.BLACK_TO_MOVE || game.gamestate == Constants.BLACK_TO_SELECT) && board[y][x].getColour().equals(Constants.Color_WHITE)||
               (game.gamestate == Constants.WHITE_TO_MOVE || game.gamestate == Constants.WHITE_TO_SELECT) && board[y][x].getColour().equals(Constants.Color_BLACK)){
                return finalMovement;
            }else{
                if(searchThreatFigure(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1]).length > 1){
                    if(board[y][x].getType() == Constants.KING){
                        return computeKingMovement(x,y);
                    }else{
                        return finalMovement;
                    }    
                }else{
                    if(searchThreatFigure(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1]).length == 0){
                        for (int i = 0; i < boardlength; i++){
                            for (int j = 0; j < boardlength; j++){
                                checkRestrictions[i][j] = true;
                            }
                        }
                    }else{
                        checkRestrictions = computeCheckRestrictions(searchThreatFigure(getKingCoordinatesCurrentTurn()[0], getKingCoordinatesCurrentTurn()[1])[0]);
                    }
                }
                
                pinRestrictions = computePinRestrictions(x,y);
                  
                switch(board[y][x].getType()){
                    
                    case Constants.PAWN:    generalMovement = computePawnMovement(x,y);
                                            for (int i = 0; i < generalMovement.size(); i++) {
                                                if(pinRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]] && checkRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]]){
                                                    finalMovement.add(generalMovement.get(i));
                                                }
                                            }
                                            break;
                                            
                    case Constants.KNIGHT:  generalMovement = computeKnightMovement(x,y);
                                            for (int i = 0; i < generalMovement.size(); i++) {
                                                if(pinRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]] && checkRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]]){
                                                    finalMovement.add(generalMovement.get(i));
                                                }
                                            }
                                            break;
                                            
                    case Constants.ROOK:    generalMovement = computeStraightMovement(x,y);
                                            for (int i = 0; i < generalMovement.size(); i++) {
                                                if(pinRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]] && checkRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]]){
                                                    finalMovement.add(generalMovement.get(i));
                                                }
                                            }
                                            break;
                                            
                    case Constants.BISHOP:  generalMovement = computeDiagonalMovement(x,y);
                                            for (int i = 0; i < generalMovement.size(); i++) {
                                                if(pinRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]] && checkRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]]){
                                                    finalMovement.add(generalMovement.get(i));
                                                }
                                            }
                                            break;
                                            
                    case Constants.QUEEN:   generalMovement = computeStraightMovement(x,y);
                                            ArrayList<Integer[]> movement = computeDiagonalMovement(x,y);
                                            for (int i = 0; i < movement.size(); i++){
                                                generalMovement.add(movement.get(i));
                                            }
                                            for (int i = 0; i < generalMovement.size(); i++) {
                                                if(pinRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]] && checkRestrictions[generalMovement.get(i)[1]][generalMovement.get(i)[0]]){
                                                    finalMovement.add(generalMovement.get(i));
                                                }
                                            }
                                            break;
                                            
                    case Constants.KING:    finalMovement = computeKingMovement(x,y);
                                            break;
                }
            }
        }
        
        return finalMovement;
    }
    
    /**
     * @author Alexander
     */
    public boolean[][] computeCheckRestrictions(Piece threat){
        int xk = getKingCoordinatesCurrentTurn()[0], yk =getKingCoordinatesCurrentTurn()[1];
        int x = threat.getX(), y = threat.getY();
        boolean[][] possibleMovement = new boolean[boardlength][boardlength];
        int xDirection = (int) Math.signum(x-xk), yDirection = (int) Math.signum(y-yk);
        
        if(threat.getType() != Constants.KNIGHT && threat.getType() != Constants.PAWN){
            for (int i = 1; Math.abs(i*xDirection) <= Math.abs(x-xk) && Math.abs(i*yDirection) <= Math.abs(y-yk); i++) {
                possibleMovement[yk + (i*yDirection)][xk + (i*xDirection)] = true;
            }
        }
        possibleMovement[y][x] = true;
        
        return possibleMovement;
    }
    
    /**
     * @author Alexander
     */
    public boolean[][] computePinRestrictions(int x, int y){
        int xk = getKingCoordinatesCurrentTurn()[0], yk =getKingCoordinatesCurrentTurn()[1];
        boolean[][] possibleMovement = new boolean[boardlength][boardlength];
        int xDirection = (int) Math.signum(x-xk), yDirection = (int) Math.signum(y-yk);
        boolean pin = true;
        ArrayList<Integer[]> pinRestrictions = new ArrayList<Integer[]>();
        
        if(xDirection != 0 || yDirection != 0){
            if(Math.abs(x-xk) == Math.abs(y-yk) || x==xk || y==yk){
                for (int i = 1; i < Math.abs(x-xk) || i < Math.abs(y-yk); i++) {
                    if(board[yk+(i*yDirection)][xk+(i*xDirection)] != null){
                        pin = false;
                    }
                }
                if(pin){
                    for(int i = 1; isInBounds(x+(i*xDirection), y+(i*yDirection)) && board[y+(i*yDirection)][x+(i*xDirection)] == null; i++){
                        pinRestrictions.add(new Integer[]{x+(i*xDirection), y+(i*yDirection)});
                    }
                    
                    int xPinPiece = x+((pinRestrictions.size()+1)*xDirection);
                    int yPinPiece = y+((pinRestrictions.size()+1)*yDirection);
                    
                    if(isInBounds(xPinPiece, yPinPiece)){
                    	 if((((x==xk || y==yk) && board[yPinPiece][xPinPiece].getType() == Constants.ROOK) ||
                                 (Math.abs(x-xk) == Math.abs(y-yk) && board[yPinPiece][xPinPiece].getType() == Constants.BISHOP)||
                                 (board[yPinPiece][xPinPiece].getType() == Constants.QUEEN))&&board[yPinPiece][xPinPiece].getColour() != board[y][x].getColour()){
                                      pinRestrictions.add(new Integer[]{xPinPiece, yPinPiece});
                                      for (int i = 0; i < pinRestrictions.size(); i++) {
                                          possibleMovement[pinRestrictions.get(i)[1]][pinRestrictions.get(i)[0]] = true;
                                      }
                              }else{
                                  for (int i = 0; i < boardlength; i++) {
                                      for (int j = 0; j < boardlength; j++) {
                                          possibleMovement[i][j] = true;
                                      }
                                  }
                              }
                    }else{
                    	for (int i = 0; i < boardlength; i++) {
                            for (int j = 0; j < boardlength; j++) {
                                possibleMovement[i][j] = true;
                            }
                        }
                    }   
                }else{
                    for (int i = 0; i < boardlength; i++) {
                        for (int j = 0; j < boardlength; j++) {
                            possibleMovement[i][j] = true;
                        }
                    }
                }
            }else{
                for (int i = 0; i < boardlength; i++) {
                    for (int j = 0; j < boardlength; j++) {
                        possibleMovement[i][j] = true;
                    }
                }
            }
        }else{
            for (int i = 0; i < boardlength; i++) {
                for (int j = 0; j < boardlength; j++) {
                    possibleMovement[i][j] = true;
                }
            }
        }
        
        return possibleMovement;
    }
    
    /**
     * @author Alexander
     * 
     * computes the basic Movement of a Pawn standing on the field with the given parameters
     * 
     * @param x: x-Coordinate of the field
     * @param y: y-Coordinate of the field
     * @return integer ArrayList of all the available fields to move on
     *  index 0: x-Coordinate of the available field
     *  index 1: y-Coordinate of the available field
     *  index 2: integer to indicate if this field houses a piece that can be taken
     */
    public ArrayList<Integer[]> computePawnMovement(int x, int y){
        ArrayList<Integer[]> moveOptions = new ArrayList<Integer[]>();
        if(isInBounds(x, y-1)){
            if(board[y-1][x] == null){
                moveOptions.add(new Integer[]{x, y-1, 0});
            }
        }
        if(isInBounds(x+1, y-1)){
            if(board[y-1][x+1] != null && board[y-1][x+1].getColour() != board[y][x].getColour()){
                moveOptions.add(new Integer[]{x+1, y-1, 1});
            }
        }
        if(isInBounds(x-1, y-1)){
            if(board[y-1][x-1] != null && board[y-1][x-1].getColour() != board[y][x].getColour()){
                moveOptions.add(new Integer[]{x-1, y-1, 1});
            }
        }
        
        if(board[y-1][x] == null && board[y-2][x] == null && !board[y][x].getMoved()){
        	moveOptions.add(new Integer[]{x, y-2, 0});
        }
        return moveOptions;
    }
    
    /**
     * @author Alexander
     * 
     * computes the basic Movement of a Knight standing on the field with the given parameters
     * 
     * @param x: x-Coordinate of the field
     * @param y: y-Coordinate of the field
     * @return integer ArrayList of all the available fields to move on
     *  index 0: x-Coordinate of the available field
     *  index 1: y-Coordinate of the available field
     *  index 2: integer to indicate if this field houses a piece that can be taken
     */
    public ArrayList<Integer[]> computeKnightMovement(int x, int y){
        ArrayList<Integer[]> moveOptions = new ArrayList<Integer[]>();
        if(isInBounds(x+2, y+1)){
            if(board[y+1][x+2] == null){
                moveOptions.add(new Integer[]{x+2, y+1, 0});
            }else{
                if(board[y+1][x+2].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x+2, y+1, 1});
                }
            }
        }
        if(isInBounds(x+2, y-1)){
            if(board[y-1][x+2] == null){
                moveOptions.add(new Integer[]{x+2, y-1, 0});
            }else{
                if(board[y-1][x+2].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x+2, y-1, 1});
                }
            }
        }
        if(isInBounds(x+1, y-2)){
            if(board[y-2][x+1] == null){
                moveOptions.add(new Integer[]{x+1, y-2, 0});
            }else{
                if(board[y-2][x+1].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x+1, y-2, 1});
                }
            }
        }
        if(isInBounds(x-1, y-2)){
            if(board[y-2][x-1] == null){
                moveOptions.add(new Integer[]{x-1, y-2, 0});
            }else{
                if(board[y-2][x-1].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x-1, y-2, 1});
                }
            }
        }
        if(isInBounds(x-2, y-1)){
            if(board[y-1][x-2] == null){
                moveOptions.add(new Integer[]{x-2, y-1, 0});
            }else{
                if(board[y-1][x-2].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x-2, y-1, 1});
                }
            }
        }
        if(isInBounds(x-2, y+1)){
            if(board[y+1][x-2] == null){
                moveOptions.add(new Integer[]{x-2, y+1, 0});
            }else{
                if(board[y+1][x-2].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x-2, y+1, 1});
                }
            }
        }
        if(isInBounds(x-1, y+2)){
            if(board[y+2][x-1] == null){
                moveOptions.add(new Integer[]{x-1, y+2, 0});
            }else{
                if(board[y+2][x-1].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x-1, y+2, 1});
                }
            }
        }
        if(isInBounds(x+1, y+2)){
            if(board[y+2][x+1] == null){
                moveOptions.add(new Integer[]{x+1, y+2, 0});
            }else{
                if(board[y+2][x+1].getColour() != board[y][x].getColour()){
                    moveOptions.add(new Integer[]{x+1, y+2, 1});
                }
            }
        }
        
        return moveOptions;
    }
    
    /**
     * @author Alexander
     * 
     * computes the possible Movement of a king standing on the field with the given parameters
     * 
     * @param x: x-Coordinate of the field
     * @param y: y-Coordinate of the field
     * @return integer ArrayList of all the available fields to move on
     *  index 0: x-Coordinate of the available field
     *  index 1: y-Coordinate of the available field
     *  index 2: integer to indicate if this field houses a piece that can be taken
     */
    public ArrayList<Integer[]> computeKingMovement(int x, int y){
        ArrayList<Integer[]> moveOptions = new ArrayList<Integer[]>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(isInBounds(x+j, y+i)){
                    if(!(x+j == x && y+i == y)){
                        if(searchThreatFigure(x+j, y+i, board[y][x]).length == 0){
                            if(board[y+i][x+j] == null){
                                moveOptions.add(new Integer[]{x+j, y+i, 0});
                            }else{
                                if(board[y+i][x+j].getColour() != board[y][x].getColour()){
                                    moveOptions.add(new Integer[]{x+j, y+i, 1});
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return moveOptions;
    }
    
    /**
     * @author Alexander
     * 
     * computes the basic straight Movement outward from the field with the given parameters
     * 
     * @param x: x-Coordinate of the field
     * @param y: y-Coordinate of the field
     * @return integer ArrayList of all the available fields to move on
     *  index 0: x-Coordinate of the available field
     *  index 1: y-Coordinate of the available field
     *  index 2: integer to indicate if this field houses a piece that can be taken
     */
    public ArrayList<Integer[]> computeStraightMovement(int x, int y){
        ArrayList<Integer[]> moveOptions = new ArrayList<Integer[]>();
        ArrayList<Integer[]> moveOptionsX = new ArrayList<Integer[]>();
        ArrayList<Integer[]> moveOptionsY = new ArrayList<Integer[]>();
        boolean PieceX = false, PieceY = false;
        
        for (int i = 0; i < boardlength; i++) {
            if(i<x){
                if(board[y][i] != null){
                    moveOptionsX.clear();
                    if(board[y][i].getColour() != board[y][x].getColour()){
                        moveOptionsX.add(new Integer[]{i,y,1});
                    }
                }else{
                    moveOptionsX.add(new Integer[]{i,y,0});
                }
            }
            
            if(i>x && !PieceX){
                if(board[y][i] != null){
                    PieceX = true;
                    if(board[y][i].getColour() != board[y][x].getColour()){
                        moveOptionsX.add(new Integer[]{i,y,1});
                    }
                }else{
                    moveOptionsX.add(new Integer[]{i,y,0});
                }
            }
            
            if(i<y){
                if(board[i][x] != null){
                    moveOptionsY.clear();
                    if(board[i][x].getColour() != board[y][x].getColour()){
                        moveOptionsY.add(new Integer[]{x,i,1});
                    }
                }else{
                    moveOptionsY.add(new Integer[]{x,i,0});
                }
            }
            
            if(i>y && !PieceY){
                if(board[i][x] != null){
                    PieceY = true;
                    if(board[i][x].getColour() != board[y][x].getColour()){
                        moveOptionsY.add(new Integer[]{x,i,1});
                    }
                }else{
                    moveOptionsY.add(new Integer[]{x,i,0});
                }                
            }
        }
        
        for (int i = 0; i < moveOptionsX.size(); i++) {
            moveOptions.add(moveOptionsX.get(i));
        }
        for (int i = 0; i < moveOptionsY.size(); i++) {
            moveOptions.add(moveOptionsY.get(i));
        }
        
        return moveOptions;
    }
    
    /**
     * @author Alexander
     * 
     * computes the basic diagonal Movement outward from the field with the given parameters
     * 
     * @param x: x-Coordinate of the field
     * @param y: y-Coordinate of the field
     * @return integer ArrayList of all the available fields to move on
     *  index 0: x-Coordinate of the available field
     *  index 1: y-Coordinate of the available field
     *  index 2: integer to indicate if this field houses a piece that can be taken
     */
    public ArrayList<Integer[]> computeDiagonalMovement(int x, int y){
        ArrayList<Integer[]> moveOptions = new ArrayList<Integer[]>();
        ArrayList<Integer[]> movementDownRight = new ArrayList<Integer[]>();
        ArrayList<Integer[]> movementUpRight = new ArrayList<Integer[]>();
        boolean PieceDownRight = false, PieceUpRight = false;
        int xUpLeft, yUpLeft;
        int xDownLeft, yDownLeft;
        
        if(x<y){
            xUpLeft = 0;
            yUpLeft = y-x;
        }else{
            xUpLeft = x-y;
            yUpLeft = 0;
        }
        if(x<(boardlength-1-y)){
            xDownLeft = 0;
            yDownLeft = x+y;
        }else{
            xDownLeft = x-(boardlength-1-y);
            yDownLeft = boardlength-1;
        }
        
        for(int i = 0; i<boardlength; i++){
            if(xUpLeft+i<x){
                if(board[yUpLeft+i][xUpLeft+i] != null){
                    movementDownRight.clear();
                    if(board[yUpLeft+i][xUpLeft+i].getColour() != board[y][x].getColour()){
                        movementDownRight.add(new Integer[]{xUpLeft+i, yUpLeft+i, 1});
                    }
                }else{
                    movementDownRight.add(new Integer[]{xUpLeft+i, yUpLeft+i, 0});
                }
            }
            
            if(xUpLeft+i>x && isInBounds(xUpLeft+i, yUpLeft+i) && !PieceDownRight){
                if(board[yUpLeft+i][xUpLeft+i] != null){
                    PieceDownRight = true;
                    if(board[yUpLeft+i][xUpLeft+i].getColour() != board[y][x].getColour()){
                        movementDownRight.add(new Integer[]{xUpLeft+i, yUpLeft+i, 1});
                    }
                }else{
                    movementDownRight.add(new Integer[]{xUpLeft+i, yUpLeft+i, 0});
                }
            }
            
            if(xDownLeft+i<x){
                if(board[yDownLeft-i][xDownLeft+i] != null){
                    movementUpRight.clear();
                    if(board[yDownLeft-i][xDownLeft+i].getColour() != board[y][x].getColour()){
                        movementUpRight.add(new Integer[]{xDownLeft+i, yDownLeft-i, 1});
                    }
                }else{
                    movementUpRight.add(new Integer[]{xDownLeft+i, yDownLeft-i, 0});
                }
            }
            
            if(xDownLeft+i>x && isInBounds(xDownLeft+i, yDownLeft-i) && !PieceUpRight){
                if(board[yDownLeft-i][xDownLeft+i] != null){
                    PieceUpRight = true;
                    if(board[yDownLeft-i][xDownLeft+i].getColour() != board[y][x].getColour()){
                        movementUpRight.add(new Integer[]{xDownLeft+i, yDownLeft-i, 1});
                    }
                }else{
                    movementUpRight.add(new Integer[]{xDownLeft+i, yDownLeft-i, 0});
                }
            }
        }
        
        for (int i = 0; i < movementDownRight.size(); i++) {
            moveOptions.add(movementDownRight.get(i));
        }
        for (int i = 0; i < movementUpRight.size();i++) {
            moveOptions.add(movementUpRight.get(i));
        }
        
        return moveOptions;
    }
    
    /**
     * @author Alexander
     * 
     * gets the Coordinates of the King of the colour that can currently move
     * 
     * @return integer Array of size 2 with the Coordinates of the King of the colour that can currently move
     *      index 0: x-Coordinate
     *      index 1: y-Coordinate
     */
    public int[] getKingCoordinatesCurrentTurn(){
        for(int i = 0; i < boardlength; i++){
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != null && board[i][j].getType() == Constants.KING){
                    if(((game.gamestate == Constants.BLACK_TO_MOVE || game.gamestate == Constants.BLACK_TO_SELECT) && board[i][j].getColour() == Constants.Color_BLACK)||
                        ((game.gamestate == Constants.WHITE_TO_MOVE || game.gamestate == Constants.WHITE_TO_SELECT) && board[i][j].getColour() == Constants.Color_WHITE)){
                            return new int[]{board[i][j].getX(), board[i][j].getY()};
                    }
                }
            }
        }
        
        return null;
    }

    public GameLogic() {
        this.boardlength = 0;
    }
    
    /**
     * @author Alexander
     * 
     * tests if a field is in bounds
     * 
     * @param x: x-Coordinate of the tested field
     * @param y: y-Coordinate of the tested field
     * @return true if the field is in boudns
     * returns false if the field is out of bounds
     */
    public boolean isInBounds(int x, int y){
        return (0 <= x && x < boardlength && 0 <= y && y < boardlength);
    }
    
    public void nextMove(){
    	flipBoard();
    	System.out.println("methode aufgerufen");
    	checkWinConditions();
    }
    
}