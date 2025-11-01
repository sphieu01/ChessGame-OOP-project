
package main;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import piece.Pawn;
import piece.*;


public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread; // da luong
    board Board = new board();
    Mouse mouse = new Mouse();
    
    //Pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promoPieces = new ArrayList<>();
    Piece activeP;
    Piece checkingP; // piece dang check king

    public static Piece castlingP;
    
    // history play
    public static Vector<ArrayList<Piece>> historyplay = new Vector<ArrayList<Piece>>();
    public static Vector<Piece> historyCheckingP = new Vector<Piece>();
//    public static int step;
    private static GamePanel instance;
    
    //Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;
    
    //Booleans
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameover;
    boolean stalemate;
    
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH,HEIGHT)); //same as setSize() but got layout manager
        setBackground(Color.black);
        addMouseMotionListener(mouse); //gọi đến các phương thức để cập nhập tòa đọ hiện 
        addMouseListener(mouse); //gọi đến các phương thức nhấp và nhả chuột
        
        instance = this;
        setLayout(null);  // Dùng null layout để setBounds có tác dụng
        JButton undoButton = createUndoButton();
        add(undoButton);
        
        setPieces();
//        testPromotion();
//        testIllegal();
        historyplay.add(deepCopyPieces(pieces));
        copyPieces(pieces, simPieces);
        historyCheckingP.clear();
        historyCheckingP.add(null);
    }
    
     public static GamePanel getInstance() {
        return instance;
    }
    
    public void resetGame() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
            gameThread = null;
        }

        pieces.clear();
        simPieces.clear();

        activeP = null;
        checkingP = null;
        castlingP = null;
        promotion = false;
        gameover = false;
        stalemate = false;
        currentColor = WHITE;
        
        setPieces();
        copyPieces(pieces, simPieces);
        historyplay.clear();
        historyplay.add(deepCopyPieces(pieces));
        historyCheckingP.clear();
        historyCheckingP.add(null);
    }
    
    public void lauchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void setPieces(){
        
        //White team
        pieces.add(new Pawn(WHITE, 0 , 6));
        pieces.add(new Pawn(WHITE, 1 , 6));
        pieces.add(new Pawn(WHITE, 2 , 6));
        pieces.add(new Pawn(WHITE, 3 , 6));
        pieces.add(new Pawn(WHITE, 4 , 6));
        pieces.add(new Pawn(WHITE, 5 , 6));
        pieces.add(new Pawn(WHITE, 6 , 6));
        pieces.add(new Pawn(WHITE, 7 , 6));
        pieces.add(new Rook(WHITE, 0 , 7));
        pieces.add(new Rook(WHITE, 7 , 7));
        pieces.add(new Knight(WHITE, 6 , 7));
        pieces.add(new Knight(WHITE, 1 , 7));
        pieces.add(new Bishop(WHITE, 5 , 7));
        pieces.add(new Bishop(WHITE, 2 , 7));
        pieces.add(new King(WHITE, 4 , 7));
        pieces.add(new Queen(WHITE, 3 , 7));
        
        //Black team
        pieces.add(new Pawn(BLACK, 0 , 1));
        pieces.add(new Pawn(BLACK, 1 , 1));
        pieces.add(new Pawn(BLACK, 2 , 1));
        pieces.add(new Pawn(BLACK, 3 , 1));
        pieces.add(new Pawn(BLACK, 4 , 1));
        pieces.add(new Pawn(BLACK, 5 , 1));
        pieces.add(new Pawn(BLACK, 6 , 1));
        pieces.add(new Pawn(BLACK, 7 , 1));
        pieces.add(new Rook(BLACK, 0 , 0));
        pieces.add(new Rook(BLACK, 7 , 0));
        pieces.add(new Knight(BLACK, 6 , 0));
        pieces.add(new Knight(BLACK, 1 , 0));
        pieces.add(new Bishop(BLACK, 5 , 0));
        pieces.add(new Bishop(BLACK, 2 , 0));
        pieces.add(new King(BLACK, 4 , 0));
        pieces.add(new Queen(BLACK, 3 , 0));
    }

//    public void testPromotion(){
//        pieces.add(new Pawn(WHITE,0,4));
//        pieces.add(new Pawn(BLACK, 0 ,5));
//    }
    public void testIllegal(){
        pieces.add(new Pawn(WHITE, 7,6));
        pieces.add(new King(WHITE, 3,7));
        pieces.add(new King(BLACK, 0,3));
        pieces.add(new Bishop(BLACK, 1,4));
        pieces.add(new Queen(BLACK, 4,5));
    }
    public static void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target){
        
        target.clear();
        for(int i = 0; i < source.size(); i++){
            target.add(source.get(i));
        }
    }
    
    @Override
    public void run(){ //gameloop
        
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while(gameThread != null){
            currentTime = System.nanoTime();
            
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1){
                update();
                repaint(); //call cai ham paint o duoi
                delta--;
            }
        }        
    }
    
    
    private void update(){
        
        if(promotion){
            promoting();
        }
        else if (gameover == false && stalemate == false)  {
            // Mouse Button Pressed // hay noi cach khac khi nhap chuot vao
            if(mouse.pressed){
                if(activeP == null){
                    //if activeP is null, check if you can pick a piece
                    for(Piece piece: simPieces){
                        // neu mouse
                        if(piece.color == currentColor &&
                                piece.col == mouse.x/board.SQUARE_SIZE &&
                                piece.row == mouse.y/board.SQUARE_SIZE){

                            activeP = piece;
                        }
                    }
                }
                else {
                    // neu nguoi choi dang giu 1 quan co, co the mo phong the move
                    simulate();
                }
            }
            /// Mouse button released /// hay noi cach kahc la khi tha chuot
            if(mouse.pressed == false){
                if(activeP != null){
                    if(validSquare) {

                        // MOVE CONFIRMED
                        // Update the piece list in case a piece has been captured and removed during the simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if(castlingP != null){
                            castlingP.updatePosition();
                        }
                        
                        if (isKingInCheck() && isCheckmate()){
                            gameover  = true;
                            notifyGameOver("Chiếu hết! " + (currentColor == WHITE ? "Trắng thắng" : "Đen thắng"));
                        }
                        else if (isStalemate() && isKingInCheck() == false) {
                            stalemate = true;
                            notifyGameOver("Hòa cờ!");
                        }
                        else{
                            // add if can promote
                            if(canPromote()){
                                promotion = true;
                            }
                            else {
                                changePlayer();
                            }
                        }
                        
                        // them lich su
                        historyplay.add(GamePanel.deepCopyPieces(pieces));
                        if (checkingP == null){
                            historyCheckingP.add(null);
                        }
                        else historyCheckingP.add(checkingP.clone());
                    }
                    else {
                        // The move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
                }
            }
        }
    }
    private void simulate(){ // thinking phase
        canMove = false;
        validSquare = false;
        
        // Reset the piece list in every loop
        // This is basically for restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);
        //reset castling piece postion(khi nào di chuyeenr chuột ra ngoài thì trả lại vị trí cũ của con xe)
        if(castlingP != null){
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        // if a piece is being held, update its position
        activeP.x = mouse.x - board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        
        //Check if the piece is hovering over a reachable square
        if(activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            
            // If hitting a piece, remove it from the list
            if(activeP.hittingP != null) {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            checkCastling();
            if (isIllegal(activeP) == false && opponentCanCaptureKing()== false){
                validSquare = true;
            }
        }
        
    }


    private void checkCastling(){
        if(castlingP != null){
            if(castlingP.col == 0){
                castlingP.col += 3;
            }
            else if(castlingP.col ==  7){
                castlingP.col -= 2;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    
    private void changePlayer() {
        if(currentColor == WHITE) {
            currentColor = BLACK;
            for(Piece piece : pieces){
                if(piece.color == BLACK){
                    piece.twoStepped = false;
                }
            }

        }
        else {
            currentColor = WHITE;
            for(Piece piece : pieces){
                if(piece.color == WHITE){
                    piece.twoStepped = false;
                }
            }
        }
        activeP = null;
    }

    private boolean canPromote(){
        if(activeP.type == Type.PAWN){
            if(currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7){
                promoPieces.clear();
                promoPieces.add(new Rook(currentColor,9,2));
                promoPieces.add(new Knight(currentColor,9,3));
                promoPieces.add(new Bishop(currentColor,9,4));
                promoPieces.add(new Queen(currentColor,9,5));
                return true;
            }
        }
        return false;
    }
    private void promoting(){
        if(mouse.pressed){
            for(Piece piece : promoPieces){
                if(piece.col == mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE){
                    switch (piece.type){
                        case ROOK: simPieces.add(new Rook(currentColor, activeP.col, activeP.row)); break;
                        case KNIGHT: simPieces.add(new Knight(currentColor, activeP.col, activeP.row)); break;
                        case BISHOP: simPieces.add(new Bishop(currentColor, activeP.col, activeP.row)); break;
                        case QUEEN: simPieces.add(new Queen(currentColor, activeP.col, activeP.row)); break;
                        default: break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces,pieces);
                    activeP = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }
    }
    
     private boolean isIllegal(Piece king){
        if (king.type == Type.KING){
            for (Piece piece : simPieces){
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean opponentCanCaptureKing(){
        Piece king = getKing(false); // get MyKing
        
        for (Piece piece : simPieces){
            if (piece.color != king.color && piece.canMove(king.col, king.row)){
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isKingInCheck(){
        Piece  king = getKing(true); // getOpponentKing
        
        if (activeP.canMove(king.col, king.row)){
            checkingP = activeP;
            return true;    // check if activeP can check Opponent king,  if yes saving for future
        }
        else{
            checkingP = null;
        }
        return false;
    }
    
    private Piece getKing(boolean opponent){ // or getOpponentKing/getMyKing
        Piece king = null;
        
        for (Piece piece : simPieces){
            if (opponent){
                if (piece.type == Type.KING && piece.color != currentColor){
                    king = piece;
                }
            }
            else {
                if (piece.type == Type.KING && piece.color == currentColor){
                    king = piece;
                }
            }
        }
        
        return king;
    }

    private boolean isCheckmate(){
        Piece king = getKing(true); // getOpponentKing
        
        if (kingCanMove(king)){ // check king co the di chuyen thoat khoi checkmate k
            return false;
        }
        else{
            // check xem co the prevent checkmate bang quan khac chan khong (an luon/ chan loi)
            
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);
            
            if (colDiff == 0){ // checking piece is attacking vertically
                if (checkingP.row < king.row){ // checking piece is above
                    for (int row = checkingP.row ; row < king.row; row++){
                        for (Piece piece : simPieces){
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row) ){
                                return false;
                            }
                        }
                    }
                }
                if (checkingP.row > king.row){ // checking piece is below
                    for (int row = checkingP.row ; row > king.row; row--){
                        for (Piece piece : simPieces){
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row) ){
                                return false;
                            }
                        }
                    }
                }
            }
            else if (rowDiff == 0){ // checking piece is attacking horizontally
                if (checkingP.col < king.col){ // checking piece is left
                    for (int col = checkingP.col ; col < king.col; col++){
                        for (Piece piece : simPieces){
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row) ){
                                return false;
                            }
                        }
                    }
                }
                if (checkingP.col > king.col){ // checking piece is right
                    for (int col = checkingP.col ; col > king.col; col--){
                        for (Piece piece : simPieces){
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row) ){
                                return false;
                            }
                        }
                    }
                }
            }
            else if (rowDiff == colDiff){ // checking piece is attacking diagonally
                if (checkingP.row < king.row){ // checking piece is above
                    
                    if (checkingP.col < king.col){ // checking piece is top left
                        for (int col = checkingP.col,row = checkingP.row ; col < king.col ; col++,row++ ){ // change lil bit && row < king.row
                            for (Piece piece : simPieces){
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row) ){
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col){ // checking piece is top right
                        for (int col = checkingP.col,row = checkingP.row ; col > king.col ; col--,row++ ){ 
                            for (Piece piece : simPieces){
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row) ){
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (checkingP.row > king.row){ // checking piece is below
                    
                    if (checkingP.col < king.col){ // checking piece is down left
                        for (int col = checkingP.col,row = checkingP.row ; col < king.col ; col++,row-- ){ 
                            for (Piece piece : simPieces){
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row) ){
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col){ // checking piece is down right
                        for (int col = checkingP.col,row = checkingP.row ; col > king.col ; col--,row-- ){ 
                            for (Piece piece : simPieces){
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row) ){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            else{
                // checking piece is knight
            }
        }
        return true;
    }
    
    private boolean kingCanMove(Piece king){ // simulate if there is square that king can move to
        
        if (isValidMove(king, -1, -1)) return true;
        if (isValidMove(king, 0, -1)) return true;
        if (isValidMove(king, 1, -1)) return true;
        if (isValidMove(king, -1, 0)) return true;
        if (isValidMove(king, 1, 0)) return true;
        if (isValidMove(king, -1, 1)) return true;
        if (isValidMove(king, 0, 1)) return true;
        if (isValidMove(king, 1, 1)) return true;

        return false;
    }
    
    private boolean isValidMove(Piece king, int colPlus, int rowPlus){
        boolean isValidMove = false;
        
        // temporary update king's position
        king.col += colPlus;
        king.row += rowPlus;
        
        if (king.canMove(king.col, king.row)){
            if (king.hittingP != null ){ // if exist 1 piece in the king's way => eat it
                simPieces.remove(king.hittingP.getIndex());
            }
            if (isIllegal(king) == false){
                isValidMove = true;
            }
        }
        
        king.resetPosition();
        copyPieces(pieces, simPieces); // xoa ket qua mo phong vua roi
        
        return isValidMove;
    }
    
    private boolean isStalemate(){
        int cnt = 0; // so quan cua doi phuong
        
        for (Piece piece : simPieces){
            if (piece.color != currentColor){
                cnt++;
            }
        }
        
        if (cnt == 1){ // only King is left
            if (kingCanMove(getKing(true)) == false ){
                return true;
            }
        }
        
        return false;
    }

    public void paintComponent(Graphics g){ //draw objeccts on panel
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        //Board
        Board.draw(g2);
        
        //Pieces
        for(Piece p : simPieces){
            p.draw(g2);
        }
        
        if(activeP != null) {
            if(canMove){
                if (isIllegal(activeP) || opponentCanCaptureKing() ){
                    g2.setColor(Color.red);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * board.SQUARE_SIZE, activeP.row * board.SQUARE_SIZE,
                            board.SQUARE_SIZE, board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                else{
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * board.SQUARE_SIZE, activeP.row * board.SQUARE_SIZE,
                            board.SQUARE_SIZE, board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            
            //Draw the active piece in the end so it won't be hidden by the board or the colored sqare;
            activeP.draw(g2);
        }
        
        
        // STATUS MESSAGES
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.PLAIN, 40));
        g2.setColor(Color.white);

        if(promotion){
            g2.drawString("Promotion to",840,150);
            for(Piece piece : promoPieces){
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        }
        else {
            if (currentColor == WHITE) {
                g2.drawString("White's turn", 840, 550);
                if (checkingP != null && checkingP.color == BLACK){
                    g2.setColor(Color.red);
                    g2.drawString("The King", 840, 650);
                    g2.drawString("is in check!", 840, 700);
                }
            } else {
                g2.drawString("Black's turn", 840, 250);
                if (checkingP != null && checkingP.color == WHITE){
                    g2.setColor(Color.red);
                    g2.drawString("The King", 840, 100);
                    g2.drawString("is in check!", 840, 150);
                }
            }
        }
        
        if (gameover){
                String s = "";
                if (currentColor == WHITE){
                    s = "WHITE WINS";
                }
                else{
                    s = "BLACK WINS";
                }
                g2.setFont(new Font("Arial", Font.PLAIN, 90));
                g2.setColor(Color.green);
                g2.drawString(s, 200, 420);
            }
            
            if (stalemate){
                g2.setFont(new Font("Arial", Font.PLAIN, 90));
                g2.setColor(Color.green);
                g2.drawString("STALEMATE", 200, 420);
            }    
    }
    private void notifyGameOver(String message) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    message + "\nBạn có muốn chơi lại không?",
                    "Kết thúc ván đấu",
                    JOptionPane.YES_NO_OPTION
            );

            ChessMainWindow parent = (ChessMainWindow) SwingUtilities.getWindowAncestor(this);

            if (choice == JOptionPane.YES_OPTION) {
                parent.playAgain();
            } else {
                parent.backToMenu();
            }
        });
    }
    
    // 🧹 Hàm reset static để tránh bàn cờ cũ bị đè khi trở lại menu
    public static void resetStaticData() {
        if (pieces != null) {
            pieces.clear();
        }
        if (simPieces != null) {
            simPieces.clear();
        }
        castlingP = null;
    }
    public static ArrayList<Piece> deepCopyPieces(ArrayList<Piece> original) {
        ArrayList<Piece> copy = new ArrayList<>();
        for (Piece p : original) {
            copy.add(p.clone());  
        }
        return copy;
    }
    
    public JButton createUndoButton() {
        JButton undo = new JButton("Undo");
        undo.setBounds(850, 700, 100, 50);
        undo.setAlignmentX(Component.CENTER_ALIGNMENT);
        undo.setFont(new Font("SansSerif", Font.BOLD, 18));
        undo.setFocusPainted(false);
        undo.setBackground(new Color(80, 80, 80));
        undo.setForeground(Color.WHITE);
//        btn.setMaximumSize(new Dimension(200, 50));
        undo.addActionListener(e -> {
            if (historyplay.size() >= 2) {
                int step = historyplay.size() - 2;

                // Reset active piece 
                activeP = pieces.get(0);
                canMove = false;
                validSquare = false;

                // Pieces từ history
                pieces.clear();
                pieces.addAll(deepCopyPieces(historyplay.get(step)));

                simPieces.clear();
                simPieces.addAll(deepCopyPieces(historyplay.get(step)));

                // Remove last history step
                historyplay.remove(historyplay.size() - 1);
                historyCheckingP.remove(historyCheckingP.size() - 1);
                checkingP = historyCheckingP.get(historyCheckingP.size() - 1);

                // Change color
                currentColor = (currentColor == WHITE) ? BLACK : WHITE;

//                update();
//                getInstance().repaint();
            }
        });
        return undo;
    }
}
