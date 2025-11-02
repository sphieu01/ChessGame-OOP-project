package main;
import java.io.*;
public class StockfishEngine {
    private Process engine;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final String path = "C:\\Users\\Admin\\OneDrive\\Desktop\\BTLOOP2\\ChessGame-OOP-project\\ChessBTL\\stockfish.exe";

    public boolean startEngine() {
        try {
            engine = new ProcessBuilder(path).start();
            reader = new BufferedReader(new InputStreamReader(engine.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));

            // Khởi tạo giao thức UCI
            sendCommand("uci");
            waitFor("uciok");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendCommand(String cmd) throws IOException {
        writer.write(cmd + "\n");
        writer.flush();
    }

    private void waitFor(String keyword) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(keyword)) break;
        }
    }

    // 🧠 Hàm chính bạn sẽ gọi từ class khác
    public String getBestMove(String moves, int depth) {
        try {
            // Nếu chưa có nước nào → dùng startpos
            if (moves == null || moves.isEmpty()) {
                sendCommand("position startpos");
            } else {
                sendCommand("position startpos moves " + moves);
            }

            // Bảo engine tính nước đi
            sendCommand("go depth " + depth);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("bestmove")) {
                    String[] parts = line.split(" ");
                    return parts[1]; // e2e4, e7e5, v.v.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "none";
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            reader.close();
            writer.close();
            engine.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
