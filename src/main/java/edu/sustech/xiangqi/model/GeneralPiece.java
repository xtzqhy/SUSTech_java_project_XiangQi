package edu.sustech.xiangqi.model;

// 红帅

public class GeneralPiece extends Piece {
    public GeneralPiece(boolean isRed) {
        super(isRed ? "帥" : "將", isRed);
    }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
