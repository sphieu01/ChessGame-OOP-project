
package main;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.util.*;
import java.io.*;
import piece.Pawn;
import piece.*;


public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread; // da luong
    board Board = new board();
    Mouse mouse = new Mouse();
    
    //Pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP;
    
    //Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;
    
    //Booleans
    boolean canMove;
    boolean validSquare;
    
    
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH,HEIGHT)); //same as setSize() but got layout manager
        setBackground(Color.black);
        addMouseMotionListener(mouse); //gọi đến các phương thức để cập nhập tòa đọ hiện 
        addMouseListener(mouse); //gọi đến các phương thức nhấp và nhả chuột
        
        setPieces();
        copyPieces(pieces, simPieces);
    }
    
    public void lauchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void setPieces(){
        
        //White team
        pieces.add(new Pawn(WHITE, 0 , 6));
        pieces.add(new Pawn(WHITE, 1 , 6));
        pieces.add(new Pawn(WHITE, 2 , 6));
        pieces.add(new Pawn(WHITE, 3 , 6));
        pieces.add(new Pawn(WHITE, 4 , 6));
        pieces.add(new Pawn(WHITE, 5 , 6));
        pieces.add(new Pawn(WHITE, 6 , 6));
        pieces.add(new Pawn(WHITE, 7 , 6));
        pieces.add(new Rook(WHITE, 0 , 7));
        pieces.add(new Rook(WHITE, 7 , 7));
        pieces.add(new Knight(WHITE, 6 , 7));
        pieces.add(new Knight(WHITE, 1 , 7));
        pieces.add(new Bishop(WHITE, 5 , 7));
        pieces.add(new Bishop(WHITE, 2 , 7));
        pieces.add(new King(WHITE, 4 , 7));
        pieces.add(new Queen(WHITE, 3 , 7));
        
        //Black team
        pieces.add(new Pawn(BLACK, 0 , 1));
        pieces.add(new Pawn(BLACK, 1 , 1));
        pieces.add(new Pawn(BLACK, 2 , 1));
        pieces.add(new Pawn(BLACK, 3 , 1));
        pieces.add(new Pawn(BLACK, 4 , 1));
        pieces.add(new Pawn(BLACK, 5 , 1));
        pieces.add(new Pawn(BLACK, 6 , 1));
        pieces.add(new Pawn(BLACK, 7 , 1));
        pieces.add(new Rook(BLACK, 0 , 0));
        pieces.add(new Rook(BLACK, 7 , 0));
        pieces.add(new Knight(BLACK, 6 , 0));
        pieces.add(new Knight(BLACK, 1 , 0));
        pieces.add(new Bishop(BLACK, 5 , 0));
        pieces.add(new Bishop(BLACK, 2 , 0));
        pieces.add(new King(BLACK, 4 , 0));
        pieces.add(new Queen(BLACK, 3 , 0));
    }
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target){
        
        target.clear();
        for(int i = 0; i < source.size(); i++){
            target.add(source.get(i));
        }
    }
    
    @Override
    public void run(){ //gameloop
        
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while(gameThread != null){
            currentTime = System.nanoTime();
            
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1){
                update();
                repaint(); //call cai ham paint o duoi
                delta--;
            }
        }        
    }
    
    
    private void update(){
        // Mouse Button Pressed // hay noi cach khac khi nhap chuot vao
        if(mouse.pressed){
            if(activeP == null){
                //if activeP is null, check if you can pick a piece
                for(Piece piece: simPieces){
                    // neu mouse 
                    if(piece.color == currentColor && 
                            piece.col == mouse.x/board.SQUARE_SIZE &&
                            piece.row == mouse.y/board.SQUARE_SIZE){

                        activeP = piece;
                    }
                }
            }
            else {
                // neu nguoi choi dang giu 1 quan co, co the mo phong the move
                simulate();
            }
        }
        /// Mouse button released /// hay noi cach kahc la khi tha chuot
        if(mouse.pressed == false){
            if(activeP != null){
                if(validSquare) {
                    
                    // MOVE CONFIRMED
                    // Update the piece list in case a piece has been captured and removed during the simulation
                    copyPieces(simPieces, pieces);
                    activeP.updatePosition();
                }
                else {
                    // The move is not valid so reset everything
                    copyPieces(pieces, simPieces);
                    activeP.resetPosition();
                    activeP = null;
                }
            }
        }
    }
    private void simulate(){ // thinking phase
        canMove = false;
        validSquare = false;
        
        // Reset the piece list in every loop
        // This is basically for restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);
        // if a piece is being held, update its position
        activeP.x = mouse.x - board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        
        //Check if the piece is hovering over a reachable square
        if(activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            
            // If hitting a piece, remove it from the list
            if(activeP.hittingP != null) {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            validSquare = true;
        }
        
    }
     
    public void paintComponent(Graphics g){ //draw objeccts on panel
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        //Board
        Board.draw(g2);
        
        //Pieces
        for(Piece p : simPieces){
            p.draw(g2);
        }
        
        if(activeP != null) {
            if(canMove){
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * board.SQUARE_SIZE, activeP.row * board.SQUARE_SIZE,
                        board.SQUARE_SIZE, board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            
            //Draw the active piece in the end so it won't be hidden by the board or the colored sqare;
            activeP.draw(g2);
            
        }
    }
}
