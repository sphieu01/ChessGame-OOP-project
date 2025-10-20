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
import main.Type;
        
public class Piece {
    
    public BufferedImage image;
    public int x,y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP; // luu lai vi tri o tiep theo dinh di (co the la quan co nao do hoac o trong)
    public boolean moved, twoStepped;
     public Type type; // mot enum liet ke
    
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
        for(int index = 0; index < GamePanel.simPieces.size(); index++){
            if(GamePanel.simPieces.get(index) == this){
                return index;
            }
        }
        return 0;
    }
    
    public void updatePosition(){
        
        if(type == type.PAWN){
            if(Math.abs(row - preRow) == 2){
                twoStepped = true;
            }
        }
        
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true; // kiem tra xem quan co di lan nao chua
    }
    public void resetPosition(){
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);      
    }
    public boolean canMove(int targetCol, int targetRow){
        return false;
    }
    public boolean isWithinBoard(int targetCol, int targetRow){
        if(targetCol >= 0 && targetCol <= 7 && targetRow <= 7 && targetRow >= 0){
            return true;
        }
        return false;
    }
    
    public Piece getHittingP(int targetCol, int targetRow) { // kiem tra nuoc di tiep theo dinh di co quan co khong
        for(Piece piece : GamePanel.simPieces){
            if(piece.col == targetCol && piece.row == targetRow && piece != this){
                return piece;
            }
        }
        return null;
    }
    public boolean isSameSquare(int targetCol, int targetRow){
        if(targetCol == preCol && targetRow == preRow){
            return true;
        }
        return false;
    }
    public boolean isValidSquare(int targetCol, int targetRow){
        hittingP = getHittingP(targetCol, targetRow);
        
        if(hittingP == null){ // o trong
            return true;
        }
        else{ // co quan co in sqare
            if(hittingP.color != this.color){
                return true;
            }
            else{
                hittingP = null;
            }
        }
        
        return false;
    }
    
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow){
        // muon di qua trai
        for(int c = preCol - 1 ; c > targetCol; c--){
            for(Piece piece : GamePanel.simPieces){
                if(piece.col == c && piece.row == targetRow){
                    return true;
                }
            }
        }
        // muon di qua phai
        for(int c = preCol + 1 ; c < targetCol; c++){
            for(Piece piece : GamePanel.simPieces){
                if(piece.col == c && piece.row == targetRow){
                    return true;
                }
            }
        }
        // muon di len tren
        for(int r = preRow - 1 ; r > targetRow; r--){
            for(Piece piece : GamePanel.simPieces){
                if(piece.col == targetCol && piece.row == r){
                    return true;
                }
            }
        }
        // muon di xuong
                for(int r = preRow + 1 ; r < targetRow; r++){
            for(Piece piece : GamePanel.simPieces){
                if(piece.col == targetCol && piece.row == r){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow){
        if(targetRow < preRow){
            //Up left
            for(int c = preCol - 1; c > targetCol; c--){
                int diff = Math.abs(c - preCol);
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow - diff){
                        return true;
                    }
                }
            }
            //Up right
            for(int c = preCol + 1; c < targetCol; c++){
                int diff = Math.abs(c - preCol);
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow - diff){
                        return true;
                    }
                }
            }
        }
        if(targetRow > preRow){
            //Down left 
            for(int c = preCol - 1; c > targetCol; c--){
                int diff = Math.abs(c - preCol);
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow + diff){
                        return true;
                    }
                }
            }
            //Down right
            for(int c = preCol + 1; c < targetCol; c++){
                int diff = Math.abs(c - preCol);
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == c && piece.row == preRow + diff){
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
