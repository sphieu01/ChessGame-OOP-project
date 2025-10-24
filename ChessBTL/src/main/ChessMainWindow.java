package main;

import javax.swing.*;
import java.awt.*;

public class ChessMainWindow extends JFrame {

    private GamePanel gamePanel; 

    public ChessMainWindow() {
        super("Simple Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 🔹 Khởi tạo GamePanel
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);
        pack(); // điều chỉnh kích thước của cửa sổ sao cho vừa khít GamePanel

        setLocationRelativeTo(null); // căn giữa màn hình
        setVisible(true);

        gamePanel.lauchGame(); // khởi động game
    }

    public void playAgain() {
        Component c = getContentPane().getComponent(0);
        if (c instanceof GamePanel) {
            GamePanel gp = (GamePanel) c;
            gp.resetGame();  
            gp.lauchGame();  
        }
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
