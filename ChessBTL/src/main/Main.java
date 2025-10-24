/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater đảm bảo rằng toàn bộ giao diện Swing
        // được tạo và hiển thị trong Event Dispatch Thread (EDT) — 
        // luồng chuyên xử lý các sự kiện giao diện, giúp chương trình mượt và ổn định.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MenuFrame().setVisible(true);
        });      
    }
}
