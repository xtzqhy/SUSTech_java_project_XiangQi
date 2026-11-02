package edu.sustech.xiangqi.model;

public class HorsePiece extends Piece {
    public HorsePiece(boolean isRed) {
        super(isRed ? "馬" : "馬", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
