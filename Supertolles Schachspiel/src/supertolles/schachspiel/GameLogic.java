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
    GameOverlay game;
    final int boardlength;
    
    public GameLogic(GameOverlay g, int boardl){
        boardlength = boardl;
        board = new Piece[boardlength][boardlength];
        game = g;
        
        startPosition();
        printBoard();
    }
    
    public void startPosition(){
        board[0][0] = new Piece(0, 0, "black", Constants.ROOK);
        board[0][boardlength-1] = new Piece(boardlength-1, 0, "black", Constants.ROOK);
        board[0][1] = new Piece(1, 0, "black", Constants.KNIGHT);
        board[0][boardlength-2] = new Piece(boardlength-2, 0, "black", Constants.KNIGHT);
        board[0][2] = new Piece(2, 0, "black", Constants.BISHOP);
        board[0][boardlength-3] = new Piece(boardlength-3, 0, "black", Constants.BISHOP);
        board[0][3] = new Piece(3, 0, "black", Constants.QUEEN);
        board[0][4] = new Piece(4, 0, "black", Constants.KING);
        for (int i = 0; i < boardlength; i++) {
            board[1][i] = new Piece(i, 1, "black", Constants.PAWN);
        }
        
        board[boardlength-1][0] = new Piece(0, boardlength-1, "white", Constants.ROOK);
        board[boardlength-1][boardlength-1] = new Piece(boardlength-1, boardlength-1, "white", Constants.ROOK);
        board[boardlength-1][1] = new Piece(1, boardlength-1, "white", Constants.KNIGHT);
        board[boardlength-1][boardlength-2] = new Piece(boardlength-2, boardlength-1, "white", Constants.KNIGHT);
        board[boardlength-1][2] = new Piece(2, boardlength-1, "white", Constants.BISHOP);
        board[boardlength-1][boardlength-3] = new Piece(boardlength-3, boardlength-1, "white", Constants.BISHOP);
        board[boardlength-1][3] = new Piece(3, boardlength-1, "white", Constants.QUEEN);
        board[boardlength-1][4] = new Piece(4, boardlength-1, "white", Constants.KING);
        for (int i = 0; i < boardlength; i++) {
            board[boardlength-2][i] = new Piece(i, boardlength-2, "white", Constants.PAWN);
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
    
    void movePiece(int[] currentCoordinate, int x, int y){
        board[x][y] = board[currentCoordinate[0]][currentCoordinate[1]];
        board[currentCoordinate[0]][currentCoordinate[1]] = null;
    }
    
    void checkWinConditions(){
        //TODO Methode implementieren
    }
    
    boolean isCheckMate(){
        //TODO Methode implementieren
        return false;
    }
    
    boolean isCheck(int x, int y){
        //TODO Methode implementieren    
        return false;
    }
    
    boolean isStaleMate(){
        //TODO Methode implementieren    
        return false;
    }
    
    /**
     * @author Niklas
     * @param x X-Coordinate on which the investigated figure is standing
     * @param y Y-Coordinate on which the investigated figure is standing
     * @return returns all figures which are threatening the figure (x,y)
     */
    public Piece[] searchThreatFigure(int x, int y){
        ArrayList<Piece> figures = new ArrayList<Piece>();
        
        Piece[] temp = searchKnight(x, y);
        
        System.err.println(temp.length);
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        temp = searchStraights(x, y);
        
        for(int i=0; i<temp.length; i++){
            figures.add(temp[i]);
        }
        
        
        return arrayListToArray(figures);
    }
    
    public Piece[] searchKnight(int x, int y){
        ArrayList<Piece> figures = new ArrayList<Piece>();
        try{
            if(board[y+1][x+2]!=null && board[y+1][x+2].getType() == Constants.KNIGHT 
                && board[y+1][x+2].getColour() != board[y][x].getColour()){
            figures.add(board[y+1][x+2]);
            }
            if(board[y-1][x+2]!=null && board[y-1][x+2].getType() == Constants.KNIGHT 
                    && board[y-1][x+2].getColour() != board[y][x].getColour()){
                figures.add(board[y-1][x+2]);
            }
            if(board[y+1][x-2]!=null && board[y+1][x-2].getType() == Constants.KNIGHT 
                    && board[y+1][x-2].getColour() != board[y][x].getColour()){
                figures.add(board[y+1][x-2]);
            }
            if(board[y-1][x-2]!=null && board[y-1][x-2].getType() == Constants.KNIGHT 
                    && board[y-1][x-2].getColour() != board[y][x].getColour()){
                figures.add(board[y-1][x-2]);
            }
            if(board[y+2][x+1]!=null && board[y+2][x+1].getType() == Constants.KNIGHT 
                    && board[y+2][x+1].getColour() != board[y][x].getColour()){
                figures.add(board[y+2][x+1]);
            }
            if(board[y-2][x+1]!=null && board[y-2][x+1].getType() == Constants.KNIGHT 
                    && board[y-2][x+1].getColour() != board[y][x].getColour()){
                figures.add(board[y-2][x+1]);
            }
            if(board[y+2][x-1]!=null && board[y+2][x-1].getType() == Constants.KNIGHT 
                    && board[y+2][x-1].getColour() != board[y][x].getColour()){
                figures.add(board[y+2][x-1]);
            }
            if(board[y-2][x-1]!=null && board[y-2][x-1].getType() == Constants.KNIGHT 
                    && board[y-2][x-1].getColour() != board[y][x].getColour()){
                figures.add(board[y-2][x-1]);
            }
        }catch(NullPointerException e){
            
        }
        return arrayListToArray(figures);
    }
    
    public Piece[] searchStraights(int x, int y){
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
        
        for(int i = 0; i < boardlength; i++){
            if(x-i > 0 && board[y][i] != null){
                if(figures.size() == 2){
                    figures.add(board[y][i]);
                }else{
                    figures.set(2, board[y][i]);
                }
            }else{
                if(board[y][i] != null && i > x){
                    figures.add(board[y][i]);
                    break;
                }
            }
        }
        for(int i = 0; i < figures.size(); i++){
            if(!(figures.get(i).getColour() != board[y][x].getColour() 
                    && (figures.get(i).getType() == Constants.QUEEN || figures.get(i).getType() == Constants.ROOK))){
                figures.remove(i);
                i--;
            }
        }
        
        return arrayListToArray(figures);
    }

    
    public static Piece[] arrayListToArray(ArrayList<Piece> arrayList){
        Piece[] temp = new Piece[arrayList.size()];
        for(int i=0;i<temp.length; i++){
            temp[i]=arrayList.get(i);
        }
        return temp;
    }
}
