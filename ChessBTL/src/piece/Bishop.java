/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package piece;

import main.GamePanel;


public class Bishop extends Piece {
       public Bishop(int color, int col, int row){
        super(color, col, row);
        
        if(color == GamePanel.WHITE){
            image = getImage("w-bishop");
        }
        else{
            image = getImage("b-bishop");
        }
    }
}
