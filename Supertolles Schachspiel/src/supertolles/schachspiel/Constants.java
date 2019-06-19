/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

/**
 *
 * @author Alexander, Niklas
 */
public class Constants {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    
    //gamestates
    public static final int WHITE_TO_SELECT = 2;
    public static final int WHITE_TO_MOVE = 3;
    public static final int BLACK_TO_SELECT = 4;
    public static final int BLACK_TO_MOVE = 5;
    
    //Figure Types
    public static final String ROOK = "Rook";
    public static final String BISHOP = "Bishop";
    public static final String PAWN = "Pawn";
    public static final String KING = "King";
    public static final String QUEEN = "Queen";
    public static final String KNIGHT = "Knight";
    
    //Colors
    public static final String Color_WHITE = "white";
    public static final String Color_BLACK = "black";
    
    //URL to Pieces
    //Temp
    public static final String Picture_TempPawn="/supertolles/schachspiel/Pictures/PawnTemp.png";
    public static final String Picture_TempKnight="/supertolles/schachspiel/Pictures/KnightTemp.png";
    public static final String Picture_TempBishop="/supertolles/schachspiel/Pictures/BishopTemp.png";
    public static final String Picture_TempRook="/supertolles/schachspiel/Pictures/RookTemp.png";
    public static final String Picture_TempQueen="/supertolles/schachspiel/Pictures/QueenTemp.png";
    public static final String Picture_TempKing="/supertolles/schachspiel/Pictures/KingTemp.png";
    
    //final Piece skins
    public static final String Picture_WhitePawn="/supertolles/schachspiel/Pictures/PawnW.png";
    public static final String Picture_BlackPawn="/supertolles/schachspiel/Pictures/PawnB.png";
    public static final String Picture_WhiteKnight="/supertolles/schachspiel/Pictures/KnightW.png";
    public static final String Picture_BlackKnight="/supertolles/schachspiel/Pictures/KnightB.png";
    public static final String Picture_WhiteBishop="/supertolles/schachspiel/Pictures/BishopW.png";
    public static final String Picture_BlackBishop="/supertolles/schachspiel/Pictures/BishopB.png";
    public static final String Picture_WhiteRook="/supertolles/schachspiel/Pictures/RookW.png";
    public static final String Picture_BlackRook="/supertolles/schachspiel/Pictures/RookB.png";
    public static final String Picture_WhiteQueen="/supertolles/schachspiel/Pictures/QueenW.png";
    public static final String Picture_BlackQueen="/supertolles/schachspiel/Pictures/QueenB.png";
    public static final String Picture_WhiteKing="/supertolles/schachspiel/Pictures/KingW.png";
    public static final String Picture_BlackKing="/supertolles/schachspiel/Pictures/KingB.png";
    //optional
    public static final String Picture_WhiteCaesar="/supertolles/schachspiel/Pictures/CaesarW.png";
    public static final String Picture_BlackCaesar="/supertolles/schachspiel/Pictures/CaesarB.png";
    
    
    //win reasons
    public static final int Reason_Surrender = 0;
    public static final int Reason_CheckMate = 1;
    public static final int Reason_StaleMate = 2;
    public static final int Reason_TimeUp = 3;
    
}
