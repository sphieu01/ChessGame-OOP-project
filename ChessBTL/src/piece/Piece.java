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

public class Piece {
    public BufferedImage image;
    public int x,y;
    public int col, row, preCol, preRow;
    public int color;
    
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
            image = ImageIO.read(getClass().getResourceAsStream("/pieces/" + imagePath + ".png"));
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
    public void draw(Graphics2D g2){
        g2.drawImage(image, x, y, board.SQUARE_SIZE, board.SQUARE_SIZE, null);
    }
}
