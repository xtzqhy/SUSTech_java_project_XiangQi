package edu.sustech.xiangqi.model;

public class CannonPiece extends Piece {
    public CannonPiece(boolean isRed) {
        super(isRed ? "炮" : "砲", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
