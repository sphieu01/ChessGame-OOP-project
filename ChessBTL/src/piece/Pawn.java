/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece{
    public Pawn(int color, int col, int row){
        super(color, col, row);
        
        type = Type.PAWN;
        
        if(color == GamePanel.WHITE){
            image = getImage("w-pawn");
        }
        else{
            image = getImage("b-pawn");
        }
    }
    public boolean canMove(int targetCol,int targetRow){
        if(isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false){
            //dinh nghia lai movevalue vi con den chi di xuong quan trang chi di len
            int moveValue;
            if(color == GamePanel.WHITE){
                moveValue = -1;
            }
            else{
                moveValue = 1;
            }
            
            hittingP = getHittingP(targetCol, targetRow);
            
            //buoc 1 o
            if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null){
                return true;
            }
            
            // buoc 2 o
            if(targetCol == preCol && targetRow == preRow + moveValue * 2 && hittingP == null && moved == false
                && pieceIsOnStraightLine(targetCol, targetRow) == false){
                return true;
            }
            // quan tot an cheo phia truoc
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue 
               && hittingP != null && hittingP.color != color){
                return true;
            }
            
            //En Passant
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue){
                for(Piece piece : GamePanel.simPieces){
                    if(piece.col == targetCol && piece.row == preRow && piece.twoStepped == true){
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
