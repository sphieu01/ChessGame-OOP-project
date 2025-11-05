package main;

public class TestStockfish {
    public static void main(String[] args) {
        StockfishEngine sf = new StockfishEngine(); // ✅ tạo đối tượng riêng

        if (sf.startEngine()) {
            System.out.println("Đã khởi động Stockfish thành công!");

            // Gửi lệnh và lấy kết quả
            String bestMove = sf.getBestMove("e2e4 e7e5", 3);
            System.out.println(" Nước đi tốt nhất: " + bestMove);

            sf.stopEngine();
        } else {
            System.out.println("Không thể khởi động Stockfish.");
        }
    }
}
