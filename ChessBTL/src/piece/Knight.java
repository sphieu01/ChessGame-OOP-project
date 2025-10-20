/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;

public class Knight extends Piece{
       public Knight(int color, int col, int row){
        super(color, col, row);
        
        type = Type.KNIGHT;
        
        if(color == GamePanel.WHITE){
            image = getImage("w-knight");
        }
        else{
            image = getImage("b-knight");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol, targetRow)){
            // quan ma di chuyebn trong pham vi co ti le hang cot la 2 : 1 or 1 : 2
            if(Math.abs(targetCol - preCol) * (Math.abs(targetRow - preRow)) == 2){
                if(isValidSquare(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
