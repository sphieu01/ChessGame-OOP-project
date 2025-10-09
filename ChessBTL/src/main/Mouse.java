/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter{ // ke thua lop MouseAdapter co san
    public int x, y;
    public boolean pressed;
    
    @Override
    public void mousePressed(MouseEvent e){
        pressed = true;
    }
    @Override
    public void mouseReleased(MouseEvent e){
        pressed = false;
    }
    @Override
    public void mouseDragged(MouseEvent e){
        x = e.getX();
        y = e.getY();
    }
    @Override
    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
    }
    
}
