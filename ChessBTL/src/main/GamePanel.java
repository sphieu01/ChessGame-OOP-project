/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread; // da luong
    board Board = new board();
       
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH,HEIGHT)); //same as setSize() but got layout manager
        setBackground(Color.black);
    }
    
    public void lauchGame(){
        gameThread = new Thread(this);
        gameThread.start();
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
        
    }
    public void paintComponent(Graphics g){ //draw objeccts on panel
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        
        Board.draw(g2);
    }
}
