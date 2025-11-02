/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;
import main.Type;

public class King extends Piece {

    public King(int color, int col, int row) {
        super(color, col, row);

        type = Type.KING;

        if (color == GamePanel.WHITE) {
            image = getImage("/pieces/w-king");
        } else {
            image = getImage("/pieces/b-king");
        }

    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1 || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1) {
                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
            if(moved == false){
                //Right castling
                if(targetCol == preCol+2 && targetRow == preRow && pieceIsOnStraightLine(targetCol,targetRow) == false){
                    //Xét xem có con nào ở vị tri thứ 3 bên phải không
                    for (Piece piece:GamePanel.simPieces){
                        if(piece.col == preCol + 3 && piece.row == preRow && piece.moved == false){
                            GamePanel.castlingP = piece;
                            return true;
                        }
                    }
                }
                //Left castling
                if(targetCol == preCol - 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol,targetRow) == false){
                    Piece p[] = new Piece[2];
                    for(Piece piece:GamePanel.simPieces){
                        if(piece.col == preCol - 3 && piece.row == targetRow){
                            p[0] = piece;
                        }
                        if(piece.col == preCol-4 && piece.row == targetRow){
                            p[1] = piece;
                        }
                        if(p[0] == null && p[1]!=null &&p[1].moved == false){
                            GamePanel.castlingP = p[1];
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public King clone() { return new King(this); }
    public King(King other) { super(other); }
}
