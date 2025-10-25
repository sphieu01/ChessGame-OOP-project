/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;
import main.Type;
import main.board;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import main.GamePanel;

public class Piece {

    public Type type;
    public BufferedImage image;
    public int x,y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP;
    public boolean moved,twoStepped;
    
    public Piece(int color, int col, int row){ //khoi tao o co
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }
    
    public BufferedImage getImage(String imagePath) {
        try {
            BufferedImage original = ImageIO.read(getClass().getResourceAsStream("/pieces/" + imagePath + ".png"));
            BufferedImage resized = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resized.createGraphics();

            int size = 80; // quan co nho di con 80px
            int offset = (100 - size) / 2; // center

            g.drawImage(original, offset, offset, size, size, null);
            g.dispose();
            return resized;
        } catch (Exception e) {
            throw new RuntimeException("Không thể đọc ảnh: " + imagePath, e);
        }
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

        if(type == Type.PAWN){
            if(Math.abs(row - preRow) == 2){
                twoStepped = true;
            }
        }

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

        if (targetRow < preRow) {
            // Up left
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            // Up right
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        if (targetRow > preRow) {
            // Down left
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow + diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }

            // Down right
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == c && piece.row == preRow + diff) {
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
