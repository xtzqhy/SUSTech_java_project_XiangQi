package edu.sustech.xiangqi.model;

// 红仕：每次斜走一格，不能离开红方九宫

public class AdvisorPiece extends Piece {
    public AdvisorPiece(boolean isRed) {
        super(isRed ? "仕" : "士", isRed);
    }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：只能九宫格内斜走一格

        //斜走一格
        if (Math.abs(toRow - fromRow) != 1 || Math.abs(toCol - fromCol) != 1) {
            return false;
        }
        
        //九宫格
        if (toCol < 3 || toCol > 5) {
            return false;
        }
        if (isRed()) {
            return toRow >= 7 && toRow <= 9;//红
        } else {
            return toRow >= 0 && toRow <= 2;//黑
        }
    }
}
