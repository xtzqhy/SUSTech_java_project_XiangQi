package edu.sustech.xiangqi.model;
import java.util.ArrayList;
import java.util.List;

// 保存棋盘数据，并统一检查和执行移动

public class Board {
    // 定义棋盘大小
    public static final int ROWS = 10;
    public static final int COLS = 9;

    // 存储棋子位置
    private final Piece[][] pieces = new Piece[ROWS][COLS];

    //当前回合是谁
    private boolean isRedTurn = true; 

    //历史记录
    private final List<MoveRecord> history = new ArrayList<>();

    public Board() {
        initializePieces();
    }
    
    public void initializePieces() {
        //初始化
         for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                pieces[r][c] = null;
            }
        }
        //黑方
        pieces[0][0] = new ChariotPiece(false);
        pieces[0][1] = new HorsePiece(false);
        pieces[0][2] = new ElephantPiece(false);
        pieces[0][3] = new AdvisorPiece(false);
        pieces[0][4] = new GeneralPiece(false);
        pieces[0][5] = new AdvisorPiece(false);
        pieces[0][6] = new ElephantPiece(false);
        pieces[0][7] = new HorsePiece(false);
        pieces[0][8] = new ChariotPiece(false);
        pieces[2][1] = new CannonPiece(false);
        pieces[2][7] = new CannonPiece(false);
        pieces[3][0] = new SoldierPiece(false);
        pieces[3][2] = new SoldierPiece(false);
        pieces[3][4] = new SoldierPiece(false);
        pieces[3][6] = new SoldierPiece(false);
        pieces[3][8] = new SoldierPiece(false);

        //红方
        pieces[9][0] = new ChariotPiece(true);
        pieces[9][1] = new HorsePiece(true);
        pieces[9][2] = new ElephantPiece(true);
        pieces[9][3] = new AdvisorPiece(true);
        pieces[9][4] = new GeneralPiece(true);
        pieces[9][5] = new AdvisorPiece(true);
        pieces[9][6] = new ElephantPiece(true);
        pieces[9][7] = new HorsePiece(true);
        pieces[9][8] = new ChariotPiece(true);
        pieces[7][1] = new CannonPiece(true);
        pieces[7][7] = new CannonPiece(true);
        pieces[6][0] = new SoldierPiece(true);
        pieces[6][2] = new SoldierPiece(true);
        pieces[6][4] = new SoldierPiece(true);
        pieces[6][6] = new SoldierPiece(true);
        pieces[6][8] = new SoldierPiece(true);

        //重置回合
        isRedTurn = true; 

        //记录清空
        history.clear();
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public Piece getPieceAt(int row, int col) {
        if (!isInside(row, col)) {
            return null;
        }
        return pieces[row][col];
    }

    public boolean isRedTurn() {
        return isRedTurn;
    }

    public List<MoveRecord> getHistory() {
        return history;
    }

    // 检查某一方的将帅是否安全
    private boolean isGeneralSafe(boolean isRedSide) {
        int generalRow = -1, generalCol = -1;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece p = pieces[r][c];
                if (p != null && p instanceof GeneralPiece && p.isRed() == isRedSide) {
                    generalRow = r;
                    generalCol = c;
                    break;
                }
            }
        }

        //找不到将帅，不可能发生，抛异常
        if (generalRow == -1){
            throw new IllegalStateException("严重错误：在棋盘上找不到" + (isRedSide ? "红方" : "黑方") + "的将帅！");
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece p = pieces[r][c];
                if (p != null && p.isRed() != isRedSide) {
                    if (p.canMove(this, r, c, generalRow, generalCol)) {
                        return false; 
                    }
                }
            }
        }

        //飞将规则
        int enemyGeneralRow = -1, enemyGeneralCol = -1;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece p = pieces[r][c];
                if (p != null && p instanceof GeneralPiece && p.isRed() != isRedSide) {
                    enemyGeneralRow = r;
                    enemyGeneralCol = c;
                    break;
                }
            }
        }
        if (generalCol == enemyGeneralCol) {
            int minRow = Math.min(generalRow, enemyGeneralRow);
            int maxRow = Math.max(generalRow, enemyGeneralRow);
            boolean hasPieceBetween = false;
            for (int r = minRow + 1; r < maxRow; r++) {
                if (pieces[r][generalCol] != null) {
                    hasPieceBetween = true;
                    break;
                }
            }
            if (!hasPieceBetween) {
                return false; 
            }
        }
        return true;
    }

    public boolean isInCheck(boolean isRedSide) {
        return !isGeneralSafe(isRedSide);
    }

    //棋子、坐标、所属方合法性检查
    public boolean isPseudoLegalMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isInside(fromRow, fromCol) || !isInside(toRow, toCol)) {
            return false;
        }
        if (fromRow == toRow && fromCol == toCol) {
            return false;
        }

        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) {
            return false;
        }
        if (!piece.canMove(this, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        Piece targetPiece = getPieceAt(toRow, toCol);
        if (targetPiece != null && targetPiece.isRed() == piece.isRed()) {
            return false;
        }   
        return true;
    }

    //模拟移动
    private boolean wouldBeSafe(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = pieces[fromRow][fromCol];
        Piece targetPiece = pieces[toRow][toCol];

        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece;

        boolean safe = isGeneralSafe(piece.isRed());

        pieces[fromRow][fromCol] = piece;
        pieces[toRow][toCol] = targetPiece;

        return safe;
    }

    //检查某一方是否有合法走法
    public boolean hasAnyLegalMove(boolean isRedSide) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece piece = pieces[r][c];
                if (piece != null && piece.isRed() == isRedSide) {
                    //尝试走到棋盘上的每一个格子
                    for (int tr = 0; tr < ROWS; tr++) {
                        for (int tc = 0; tc < COLS; tc++) {
                            //检查局部走法是否合法
                            if (isPseudoLegalMove(r, c, tr, tc)) {
                                if(wouldBeSafe(r, c, tr, tc)) return true;
                            }
                        }
                    }
                }
            }
        }
        return false; 
    }

    public boolean tryMove(int fromRow, int fromCol, int toRow, int toCol) {    
        //回合合法性检查
        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece.isRed() != isRedTurn()) {
            return false;
        }

        //棋子合法性检查
        if (!isPseudoLegalMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }   

        //移动合法性检查
        if (!wouldBeSafe(fromRow, fromCol, toRow, toCol)) return false;

        Piece targetPiece = getPieceAt(toRow, toCol);
        history.add(new MoveRecord(fromRow, fromCol, toRow, toCol, targetPiece));

        //执行
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece;
        isRedTurn = !isRedTurn; 

        return true;
    }

    //悔棋
    public boolean undoMove() {
        if (history.isEmpty()) {
            return false; 
        }
        MoveRecord last = history.remove(history.size() - 1);
        pieces[last.fromRow][last.fromCol] = pieces[last.toRow][last.toCol];
        pieces[last.toRow][last.toCol] = last.capturedPiece;
        isRedTurn = !isRedTurn;
        return true;
    }

    public List<MoveRecord> getAllLegalMoves(boolean isRedSide) {
        List<MoveRecord> legalMoves = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece piece = pieces[r][c];
                if (piece != null && piece.isRed() == isRedSide) {
                    for (int tr = 0; tr < ROWS; tr++) {
                        for (int tc = 0; tc < COLS; tc++) {
                            if (isPseudoLegalMove(r, c, tr, tc) && wouldBeSafe(r, c, tr, tc)) {
                                legalMoves.add(new MoveRecord(r, c, tr, tc, pieces[tr][tc]));
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    //评估当前局面得分
    public int evaluate() {
        int score = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece p = pieces[r][c];
                if (p != null) {
                    int value = 0;
                    if (p instanceof GeneralPiece) value = 10000;
                    else if (p instanceof ChariotPiece) value = 900;
                    else if (p instanceof CannonPiece) value = 450;
                    else if (p instanceof HorsePiece) value = 400;
                    else if (p instanceof AdvisorPiece) value = 200;
                    else if (p instanceof ElephantPiece) value = 200;
                    else if (p instanceof SoldierPiece) value = 100;
                    
                    score += p.isRed() ? value : -value;
                }
            }
        }
        return score;
    }

    public Board copy() {
        Board newBoard = new Board();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                newBoard.pieces[r][c] = this.pieces[r][c]; 
            }
        }
        newBoard.isRedTurn = this.isRedTurn;
        return newBoard;
    }
}
