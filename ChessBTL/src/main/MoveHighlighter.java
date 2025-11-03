package main;

import java.awt.*;
import java.util.ArrayList;
import piece.Piece;

public class MoveHighlighter {
    private ArrayList<Point> highlightedSquares = new ArrayList<>();

    public void clear() {
        highlightedSquares.clear();
    }

    public void setHighlights(Piece piece, ArrayList<Piece> allPieces) {
        clear();
        if (piece == null) return;

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (piece.canMove(col, row)) {
                    highlightedSquares.add(new Point(col, row));
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 0, 100)); // vàng trong suốt
        for (Point p : highlightedSquares) {
            g2.fillRect(p.x * board.SQUARE_SIZE, p.y * board.SQUARE_SIZE, board.SQUARE_SIZE, board.SQUARE_SIZE);
        }
    }
    public void drawhint(Graphics2D g2) {
        // ví dụ vàng nhạt, alpha vừa phải
        g2.setColor(new Color(255, 165, 0, 150)); 
        for (Point p : GamePanel.hintList) {
            g2.fillRect(p.x * board.SQUARE_SIZE, p.y * board.SQUARE_SIZE, board.SQUARE_SIZE, board.SQUARE_SIZE);
        }
    }
}
