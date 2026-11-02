package edu.sustech.xiangqi.model;

public class ChariotPiece extends Piece {
    public ChariotPiece(boolean isRed) {
        super(isRed ? "車" : "車", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
