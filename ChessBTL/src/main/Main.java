/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Simple Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); //co dinh kich thuoc(rong,cao)
        
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();// tu dieu chinh kich thuoc cua so
        
        window.setLocationRelativeTo(null); // can giua
        window.setVisible(true);
        
        gp.lauchGame();
    }
}
