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
        board[0][boardlength-1] = new Piece(boardlength-1, 0, "black", "Rook");
        board[0][1] = new Piece(1, 0, "black", "Knight");
        board[0][boardlength-2] = new Piece(boardlength-2, 0, "black", "Knight");
        board[0][2] = new Piece(2, 0, "black", "Bishop");
        board[0][boardlength-3] = new Piece(boardlength-3, 0, "black", "Bishop");
        board[0][3] = new Piece(3, 0, "black", "Queen");
        board[0][4] = new Piece(4, 0, "black", "King");
        for (int i = 0; i < boardlength; i++) {
            board[1][i] = new Piece(i, 1, "black", "Pawn");
        }
        
        board[boardlength-1][0] = new Piece(0, boardlength-1, "white", "Rook");
        board[boardlength-1][boardlength-1] = new Piece(boardlength-1, boardlength-1, "white", "Rook");
        board[boardlength-1][1] = new Piece(1, boardlength-1, "white", "Knight");
        board[boardlength-1][boardlength-2] = new Piece(boardlength-2, boardlength-1, "white", "Knight");
        board[boardlength-1][2] = new Piece(2, boardlength-1, "white", "Bishop");
        board[boardlength-1][boardlength-3] = new Piece(boardlength-3, boardlength-1, "white", "Bishop");
        board[boardlength-1][3] = new Piece(3, boardlength-1, "white", "Queen");
        board[boardlength-1][4] = new Piece(4, boardlength-1, "white", "King");
        for (int i = 0; i < boardlength; i++) {
            board[boardlength-2][i] = new Piece(i, boardlength-2, "white", "Pawn");
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
    
    boolean isCheck(){
        //TODO Methode implementieren    
        return false;
    }
    
    boolean isStaleMate(){
        //TODO Methode implementieren    
        return false;
    }
    
}
