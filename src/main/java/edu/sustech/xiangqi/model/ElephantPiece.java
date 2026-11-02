package edu.sustech.xiangqi.model;

public class ElephantPiece extends Piece {
    public ElephantPiece(boolean isRed) {
        super(isRed ? "相" : "象", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
