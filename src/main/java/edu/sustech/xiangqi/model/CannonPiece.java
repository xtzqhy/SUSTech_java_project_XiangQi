package edu.sustech.xiangqi.model;

public class CannonPiece extends Piece {
    public CannonPiece(boolean isRed) {
        super(isRed ? "炮" : "砲", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：只能走同行或者同列，要么路上没子，要么中间必须恰好隔一个子，且落点必须有敌方子

        //直线
        if (fromRow != toRow && fromCol != toCol) return false;

        //路上棋子数量
        int pieceCountBetween = 0;
        if (fromRow == toRow) {
            int minCol = Math.min(fromCol, toCol);
            int maxCol = Math.max(fromCol, toCol);
            for (int c = minCol + 1; c < maxCol; c++) {
                if (board.getPieceAt(fromRow, c) != null) pieceCountBetween++;
            }
        } else {
            int minRow = Math.min(fromRow, toRow);
            int maxRow = Math.max(fromRow, toRow);
            for (int r = minRow + 1; r < maxRow; r++) {
                if (board.getPieceAt(r, fromCol) != null) pieceCountBetween++;
            }
        }

        //敌方子
        Piece target = board.getPieceAt(toRow, toCol);
        if (target == null) {
            return pieceCountBetween == 0;
        } else {
            return pieceCountBetween == 1;//trymove里已检查过
        }
    }
}
