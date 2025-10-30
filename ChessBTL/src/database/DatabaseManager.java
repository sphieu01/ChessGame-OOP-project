package database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:chess_game.db"; // đường dẫn đến file cơ sở dữ liệu SQLite.
    private Connection connection; // đối tượng kết nối đến database.
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public DatabaseManager() { // Kết nối database
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTableIfNotExists();
        } catch (SQLException e) {
            System.err.println("Loi khoi tao co so du lieu: " + e.getMessage());
        }
    }

    private void createTableIfNotExists() { // Tạo bảng nếu chưa có
        String sql = """
                CREATE TABLE IF NOT EXISTS game_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    result TEXT NOT NULL,
                    date TEXT
                );
                """;
        try (Statement stmt = connection.createStatement() ) { // gửi câu lệnh SQL tới database
            stmt.execute(sql); // Thực thi lệnh sql
        } catch (SQLException e) {
            System.err.println("Loi tao bang: " + e.getMessage());
        }
    }

    public void saveGameResult(String result) { // Lưu kết quả trận đấu
        String sql = "INSERT INTO game_history(result, date) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { //sql có thể chứa các tham số “?”.
            pstmt.setString(1, result); // Gán giá trị cho tham số thứ nhất (?) trong câu SQL.
            String now = LocalDateTime.now().format(FORMATTER);
            pstmt.setString(2, now); // Gán giá trị cho tham số thứ hai (?) trong câu SQL.
            pstmt.executeUpdate(); // Thực thi câu lệnh SQL dạng INSERT, UPDATE hoặc DELETE.
            System.out.println("Da luu ket qua: " + result + " (" + now + ")");
        } catch (SQLException e) {
            System.err.println("Loi luu ket qua: " + e.getMessage());
        }
    }

    public List<String> getAllGameHistory() { // Đọc toàn bộ lịch sử
        List<String> history = new ArrayList<>();
        String sql = "SELECT * FROM game_history ORDER BY id DESC";
        try (Statement stmt = connection.createStatement(); // gửi câu lệnh SQL tới database
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String date = rs.getString("date");
                String result = rs.getString("result");
                history.add(date + " - " + result);
            }
        } catch (SQLException e) {
            System.err.println("Loi doc lich su: " + e.getMessage());
        }
        return history;
    }

    public void clearHistory() { // Xóa toàn bộ lịch sử
        String sql = "DELETE FROM game_history";
        try (Statement stmt = connection.createStatement()) {
            int rows = stmt.executeUpdate(sql); // trả về số dòng bị ảnh hưởng
            System.out.println("Da xoa " + rows + " dong lich su dau.");
        } catch (SQLException e) {
            System.err.println("Loi xoa lich su: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Da dong ket noi co so du lieu.");
            }
        } catch (SQLException e) {
            System.err.println("Loi dong ket noi CSDL: " + e.getMessage());
        }
    }
}
