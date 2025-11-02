package main;

public class ChessClock {
    private int initialMinutes;
    private int whiteTime; // giây
    private int blackTime; // giây
    private boolean whiteTurn = true; // true = bên trắng đang đi

    public ChessClock(int minutes) {
        this.initialMinutes = minutes;
        this.whiteTime = minutes;
        this.blackTime = minutes;
    }

    public void tick() {
        if (whiteTurn && whiteTime > 0) whiteTime--;
        else if (!whiteTurn && blackTime > 0) blackTime--;
    }

    public void switchTurn() {
        whiteTurn = !whiteTurn;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public int getWhiteTime() {
        return whiteTime;
    }

    public int getBlackTime() {
        return blackTime;
    }

    public boolean isTimeOver() {
        return whiteTime <= 0 || blackTime <= 0;
    }

    public void reset() {
        whiteTime = initialMinutes; // quay lại phút ban đầu
        blackTime = initialMinutes ;
        whiteTurn = true;                // đặt lại bên trắng đi trước
    }


}