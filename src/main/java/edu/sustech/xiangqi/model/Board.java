package edu.sustech.xiangqi.model;

// 保存棋盘数据，并统一检查和执行移动

public class Board {
    // 定义棋盘大小
    public static final int ROWS = 10;
    public static final int COLS = 9;

    // 存储棋子位置
    private final Piece[][] pieces = new Piece[ROWS][COLS];

    //当前回合是谁
    private boolean isRedTurn = true; 

    public Board() {
        initializePieces();
    }

    public boolean isRedTurn() {
        return isRedTurn;
    }
    
    private void initializePieces() {
        
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

    public boolean tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        //棋子、坐标、所属方合法性检查
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

        //回合合法性检查
        if (piece.isRed() != isRedTurn()) {
            return false;
        }

        //实现移动和吃子
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece;

        //仅在成功移动后切换回合，并判断将军、胜负等状态
        isRedTurn = !isRedTurn;

        // TODO: 检查走完后己方将帅是否安全
        return true;
    }
}
