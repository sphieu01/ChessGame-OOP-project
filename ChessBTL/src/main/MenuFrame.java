package main;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private final DatabaseManager dbManager = new DatabaseManager(); 
    private final SoundManager soundManager = new SoundManager();
    private boolean musicOn = true; // trạng thái nhạc (đang bật)

    
    public MenuFrame() {
        setTitle("Chess Game Menu");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // căn giữa màn hình
        setResizable(false); // không thể thay đổi kích thước cửa sổ
        
        soundManager.playLoop("res/sounds/menu_music.wav");
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Thiết lập bố cục cho panel theo hướng dọc (các thành phần được xếp từ trên xuống dưới).
        panel.setBackground(new Color(30, 30, 30)); // màu nền: xám đậm

        JLabel title = new JLabel("♔ CHESS GAME ♚", SwingConstants.CENTER); // SwingConstants.CENTER: căn giữa nội dung của nhãn theo chiều ngang
        title.setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); // phông chữ
        title.setForeground(Color.WHITE); //màu chữ
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa thành phần title theo trục ngang trong bố cục
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); // thêm padding 40px trên và dưới

        JButton playBtn = createButton("2 Player");
        playBtn.addActionListener(e -> {
            soundManager.stop();
            dispose(); // đóng menu
            new ChessMainWindow(); // mở game
        });

        // Choi voi Stockfish
        JButton playWithStockfis = createButton("1 Player");
        playWithStockfis.addActionListener(e -> {
            soundManager.stop();
            dispose(); // đóng menu
            GamePanel.modeAI = true;
            new ChessMainWindow(); // mở game
        });

        JButton historyBtn = createButton("History");
        historyBtn.addActionListener(e -> showHistoryWindow());

        JButton infoBtn = createButton("Information");
        infoBtn.addActionListener(e -> showInfoDialog());

        JButton exitBtn = createButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        
        // Nút bật/tắt nhạc
        JButton musicBtn = new JButton("Music: ON");
        musicBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa nút theo trục ngang trong bố cục
        musicBtn.setFont(new Font("SansSerif", Font.BOLD, 14)); // Đặt phông chữ cho nút là SansSerif, kiểu đậm (BOLD), cỡ chữ 14
        musicBtn.setFocusPainted(false); // Tắt viền sáng khi nút được chọn hoặc bấm
        musicBtn.setBackground(new Color(60, 60, 60)); // màu nền
        musicBtn.setForeground(Color.WHITE); // màu chữ
        musicBtn.setMaximumSize(new Dimension(180, 40)); // Giới hạn kích thước tối đa của nút
        musicBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thay đổi con trỏ chuột thành hình bàn tay khi di chuột qua nút

        musicBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                musicBtn.setBackground(new Color(90, 90, 90));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                musicBtn.setBackground(new Color(60, 60, 60));
            }
        }); // đổi màu khi rê chuột vào và trở lại màu cũ khi rời chuột ra

        // Xử lý bật/tắt nhạc
        musicBtn.addActionListener(e -> {
            if (musicOn) {
                soundManager.stop();
                musicBtn.setText("Music: OFF");
                musicOn = false;
            } else {
                soundManager.playLoop("res/sounds/menu_music.wav");
                musicBtn.setText("Music: ON");
                musicOn = true;
            }
        });

        panel.add(title);
        panel.add(playWithStockfis);
        panel.add(Box.createVerticalStrut(20)); //thêm một khoảng trống dọc (vertical space) có chiều cao 20 pixel
        panel.add(playBtn);
        panel.add(Box.createVerticalStrut(20)); 
        panel.add(historyBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(infoBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(exitBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(musicBtn);

        add(panel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa nút theo trục ngang trong bố cục
        btn.setFont(new Font("SansSerif", Font.BOLD, 20)); // phông chữ
        btn.setFocusPainted(false); // Tắt viền sáng khi nút được chọn hoặc bấm
        btn.setBackground(new Color(80, 80, 80)); // màu nền
        btn.setForeground(Color.WHITE); // màu chữ
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // đổi con trỏ chuột (mouse cursor) thành hình bàn tay

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(120, 120, 120));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(80, 80, 80));
            }
        }); // đổi màu khi di chuột vào và trở lại màu cũ khi rời đi.

        return btn;
    }

    private void showInfoDialog() {
        String message = """
                Nhóm DHHA – Java Swing Chess

                Thành viên:
                - Phạm Gia Thế Định
                - Đào Đăng Hoàng
                - Đào Trung Hiếu
                - Nguyễn Đức Anh
                """;
        JOptionPane.showMessageDialog(this, message, "Thông tin nhóm", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHistoryWindow() {
        List<String> history = dbManager.getAllGameHistory();

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // read-only
        textArea.setFont(new Font("monospaced", Font.PLAIN, 13));

        if (history.isEmpty()) {
            textArea.setText("Chưa có ván đấu nào được lưu!");
        } else {
            StringBuilder sb = new StringBuilder("Lịch sử các ván đấu:\n\n");
            for (String record : history) {
                sb.append(record).append("\n");
            }
            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea); // Gắn thanh cuộn cho textArea
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JButton clearBtn = new JButton("Clear History");
        clearBtn.setFont(new Font("SansSerif", Font.BOLD, 13)); // Kiểu chữ
        clearBtn.setBackground(new Color(200, 70, 70)); // Màu nền
        clearBtn.setForeground(Color.WHITE); // Màu chữ
        clearBtn.setFocusPainted(false); // Ẩn viền focus để nút đẹp hơn
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Khi di chuột qua clearBtn, con trỏ sẽ biến thành bàn tay
        clearBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // createEmptyBorder(top, left, bottom, right)

        clearBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearBtn.setBackground(new Color(230, 90, 90));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                clearBtn.setBackground(new Color(200, 70, 70));
            }
        }); // thay đổi màu khi rê chuột vào và rời chuột ra.

        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa toàn bộ lịch sử không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dbManager.clearHistory();
                textArea.setText("Đã xóa toàn bộ lịch sử đấu!");
            }
        });

        //Gộp ScrollPane và nút Clear vào 1 panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(clearBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Lịch sử đấu", JOptionPane.INFORMATION_MESSAGE);
    }
}
