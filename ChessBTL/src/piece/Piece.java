/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;
import main.board;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import main.GamePanel;

public class Piece {
    public BufferedImage image;
    public int x,y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP;
    public boolean moved;
    
    public Piece(int color, int col, int row){ //khoi tao o co
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }
    
    public BufferedImage getImage(String imagePath){ //doc hinh anh tu source folder
        BufferedImage image = null;
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/pieces/" + imagePath + ".png")); //khac 1 chut so voi yt
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
    
    public int getX(int col){
        return col * board.SQUARE_SIZE;
    }
    public int getY(int row){
        return row * board.SQUARE_SIZE;
    }

    public int getCol(int x){
        return (x + board.HALF_SQUARE_SIZE)/board.SQUARE_SIZE;
    }
    
    public int getRow(int y){
        return (y + board.HALF_SQUARE_SIZE) / board.SQUARE_SIZE;
    }
    
    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public void updatePosition(){
        
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true;
    }
    
    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }
    
    public boolean canMove(int targetCol, int targetRow){
        return false;
    }
    
    
    public boolean isWithinBoard(int targetCol, int targetRow) {
        if(targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7) {
            return true;
        }
        return false;
    }
    
    public boolean isSameSquare(int targetCol, int targetRow) {
        if (targetCol == preCol && targetRow == preRow) {
            return true;
        }
        return false;
    }

    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }
    
    public boolean isValidSquare(int targetCol, int targetRow) {
        hittingP = getHittingP(targetCol, targetRow);

        if (hittingP == null) { // This square is VACANT
            return true;
        } else { // This square is OCCUPIED
            if (hittingP.color != this.color) { // If the color is different, it can be captured
                return true;
            } else {
                hittingP = null;
            }
        }
        return false;
    }

    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {
        // Khi quân cờ di chuyển sang trái
        for (int c = preCol - 1; c > targetCol; c--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // Khi quân cờ di chuyển sang phải
        for (int c = preCol + 1; c < targetCol; c++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // Khi quân cờ di chuyển lên trên
        for (int r = preRow - 1; r > targetRow; r--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        // Khi quân cờ di chuyển xuống dưới
        for (int r = preRow + 1; r < targetRow; r++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {

        // Đi lên trên (targetRow < preRow)
        if (targetRow < preRow) {

            // Lên trái
            for (int c = preCol - 1, r = preRow - 1; c > targetCol && r > targetRow; c--, r--) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == r) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            // Lên phải
            for (int c = preCol + 1, r = preRow - 1; c < targetCol && r > targetRow; c++, r--) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == r) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
        } // Đi xuống (targetRow > preRow)
        else if (targetRow > preRow) {

            // Xuống trái
            for (int c = preCol - 1, r = preRow + 1; c > targetCol && r < targetRow; c--, r++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == r) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            // Xuống phải
            for (int c = preCol + 1, r = preRow + 1; c < targetCol && r < targetRow; c++, r++) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == r) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public void draw(Graphics2D g2){
        g2.drawImage(image, x, y, board.SQUARE_SIZE, board.SQUARE_SIZE, null);
    }
}
