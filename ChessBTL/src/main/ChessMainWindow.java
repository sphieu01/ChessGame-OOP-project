package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessMainWindow extends JFrame {

    private GamePanel gamePanel;

    public ChessMainWindow() {
        super("Simple Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 🔹 Khởi tạo GamePanel
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        pack(); // điều chỉnh kích thước của cửa sổ
        setLocationRelativeTo(null); // căn giữa màn hình
        setVisible(true);

        gamePanel.lauchGame(); // khởi động game
    }



    public void backToMenu() {
        
        GamePanel.resetStaticData();

        
        getContentPane().removeAll();
        revalidate();
        repaint();

        // Đóng cửa sổ cũ
        dispose();

        // Mở lại menu
        SwingUtilities.invokeLater(() -> new MenuFrame().setVisible(true));
    }

}
