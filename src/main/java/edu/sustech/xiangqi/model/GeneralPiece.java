package edu.sustech.xiangqi.model;

// 红帅

public class GeneralPiece extends Piece {
    public GeneralPiece(boolean isRed) {
        super(isRed ? "帥" : "將", isRed);
    }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：只能九宫格内直走一格
        
        //直走一格
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        if (rowDiff + colDiff != 1) return false;

        //九宫格
        if (toCol < 3 || toCol > 5) return false;
        if (isRed()) {
            return toRow >= 7 && toRow <= 9;
        } else {
            return toRow >= 0 && toRow <= 2;
        }
    }
}
