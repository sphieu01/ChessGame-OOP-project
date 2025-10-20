/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;

public class Rook extends Piece{
       public Rook(int color, int col, int row){
        super(color, col, row);
        
           type = Type.ROOK;
        
        if(color == GamePanel.WHITE){
            image = getImage("w-rook");
        }
        else{
            image = getImage("b-rook");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow) == false){
            //quan xe di trong cung 1 hang hoac cot
            if(targetCol == preCol || targetRow == preRow){
                if(isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol,targetRow) == false){
                    return true;
                }
            }
        }
        return false;
    }
}
