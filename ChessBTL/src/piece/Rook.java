/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;

public class Rook extends Piece{
    public Rook(int color, int col, int row) {
        super(color, col, row);

        type = Type.ROOK;

        if (color == GamePanel.WHITE) {
            image = getImage("w-rook");
        } else {
            image = getImage("b-rook");
        }
    }
    
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
            // Rook can move as long as either its col or row is the same
            if (targetCol == preCol || targetRow == preRow  && isSameSquare(targetCol, targetRow) == false) {
                if (isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public Rook clone() { return new Rook(this); }
    public Rook(Rook other) { super(other); }
}
