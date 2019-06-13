/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supertolles.schachspiel;

/**
 *
 * @author Alexander
 */
public class Piece {
    String type,colour;
    int x,y;
    
    public Piece(int X, int Y, String col, String typ){
        type = typ;
        colour = col;
        x = X;
        y = Y;
    }
    
    public void setType(String typ){
        type = typ;
    }
    
    public void setColour(String col){
        colour = col;
    }
    
    public void setCoordinates(int X, int Y){
        x = X;
        y = Y;
    }
    
    public String getType(){
        return type;
    }
    
    public String getColour(){
        return colour;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}
