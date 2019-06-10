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
public class GameLogic {
    
    Piece[][] board;
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
        board[0][0] = new Piece(0, 0, "black", "Rook");
        board[0][boardlength-1] = new Piece(0, boardlength-1, "black", "Rook");
        board[0][1] = new Piece(0, 1, "black", "Knight");
        board[0][boardlength-2] = new Piece(0, boardlength-2, "black", "Knight");
        board[0][2] = new Piece(0, 2, "black", "Bishop");
        board[0][boardlength-3] = new Piece(0, boardlength-3, "black", "Bishop");
        board[0][3] = new Piece(0, 3, "black", "Queen");
        board[0][4] = new Piece(0, 4, "black", "King");
        for (int i = 0; i < boardlength; i++) {
            board[1][i] = new Piece(1, i, "black", "Pawn");
        }
        
        board[boardlength-1][0] = new Piece(boardlength-1, 0, "white", "Rook");
        board[boardlength-1][boardlength-1] = new Piece(boardlength-1, boardlength-1, "white", "Rook");
        board[boardlength-1][1] = new Piece(boardlength-1, 1, "white", "Knight");
        board[boardlength-1][boardlength-2] = new Piece(boardlength-1, boardlength-2, "white", "Knight");
        board[boardlength-1][2] = new Piece(boardlength-1, 2, "white", "Bishop");
        board[boardlength-1][boardlength-3] = new Piece(boardlength-1, boardlength-3, "white", "Bishop");
        board[boardlength-1][3] = new Piece(boardlength-1, 3, "white", "Queen");
        board[boardlength-1][4] = new Piece(boardlength-1, 4, "white", "King");
        for (int i = 0; i < boardlength; i++) {
            board[boardlength-2][i] = new Piece(boardlength-2, i, "white", "Pawn");
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
        //TODO Methode implementieren 
    }
    
    void movePiece(int[] currentCoordinate, int x, int y){
        //TODO Methode implementieren  
    }
    
    void checkWinConditions(){
        //TODO Methode implementieren    
    }
    
    boolean isCheckMate(){
        //TODO Methode implementieren
        return false;
    }
    
    boolean isCheck(){
        //TODO Methode implementieren    
        return false;
    }
    
    boolean isStaleMate(){
        //TODO Methode implementieren    
        return false;
    }
    
}
