package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuFrame extends JFrame {

    public MenuFrame() {
        setTitle("Chess Game Menu");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // căn giữa màn hình
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("♔ CHESS GAME ♚", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        JButton playBtn = createButton("Play");
        playBtn.addActionListener(e -> {
            dispose(); // đóng menu
            new ChessMainWindow(); // mở game
        });

        JButton loadBtn = createButton("Information");
        loadBtn.addActionListener(e -> {
            String message = "Nhóm DHHA – Java Swing Chess\n\n"
                    + "Thành viên:\n"
                    + "- Phạm Gia Thế Định\n"
                    + "- Đào Đăng Hoàng\n"
                    + "- Đào Trung Hiếu\n"
                    + "- Nguyễn Đức Anh";

            JOptionPane.showMessageDialog(this, message, "Thông tin nhóm", JOptionPane.INFORMATION_MESSAGE);
        });



        JButton exitBtn = createButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(playBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(loadBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(exitBtn);

        add(panel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setMaximumSize(new Dimension(200, 50));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(120, 120, 120));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(80, 80, 80));
            }
        });
        return btn;
    }
}
