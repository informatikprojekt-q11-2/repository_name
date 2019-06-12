/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel.test;

import supertolles.schachspiel.Constants;
import supertolles.schachspiel.GameLogic;
import supertolles.schachspiel.Piece;

/**
 *Klasse zum Testen von den Methoden zum Figurensuchen in GameLogic
 * @author Niklas
 */
public class Test_SearchPieces extends GameLogic{
    

    public Test_SearchPieces(){
        super(null, 8);
        for(int i = 0; i<8;i++){
            for(int j = 0; j<8;j++){
                board[i][j] = null;
            }
        }
        test1();
    }
    
    void test1(){
        board[4][4]= new Piece(4,4, Constants.Color_WHITE, Constants.KING);
        board[0][4]= new Piece(0,4, Constants.Color_BLACK, Constants.QUEEN);
        board[3][4]= new Piece(3,4, Constants.Color_WHITE, Constants.BISHOP);
        board[2][3]= new Piece(2,3, Constants.Color_BLACK, Constants.KNIGHT);
        board[6][3]= new Piece(6,3, Constants.Color_BLACK, Constants.KNIGHT);
        board[2][5]= new Piece(2,5, Constants.Color_WHITE, Constants.KNIGHT);
        board[4][0]= new Piece(4,0, Constants.Color_BLACK, Constants.QUEEN);
        board[5][4]= new Piece(5,4, Constants.Color_WHITE, Constants.PAWN);
        board[7][4]= new Piece(7,4, Constants.Color_BLACK, Constants.ROOK);
        board[4][6]= new Piece(4,6, Constants.Color_BLACK, Constants.BISHOP);
        board[4][7]= new Piece(4,7, Constants.Color_BLACK, Constants.ROOK);
        
        Piece[] threat = searchThreatFigure(4, 4);
        System.out.println(threat.length);
        for(int i=0; i<threat.length;i++){
            System.out.println(threat[i].getType()+" "+threat[i].getColour()
                    +"  x:"+threat[i].getX()+"  y:"+threat[i].getY());
        }
    }
 
}
