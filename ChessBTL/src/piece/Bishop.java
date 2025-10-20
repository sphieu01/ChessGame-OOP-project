/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;


public class Bishop extends Piece {
       public Bishop(int color, int col, int row){
        super(color, col, row);
        
        type = Type.BISHOP;
        
        if(color == GamePanel.WHITE){
            image = getImage("w-bishop");
        }
        else{
            image = getImage("b-bishop");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol, targetRow) == false){
            // ti le la hang cot luon bang nhau
            if(Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)){
                if(isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol,targetRow) == false){
                    return true;
                }
            }
        }
        return false;
    }
}
