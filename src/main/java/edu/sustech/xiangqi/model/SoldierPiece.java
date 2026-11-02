package edu.sustech.xiangqi.model;

public class SoldierPiece extends Piece {
    public SoldierPiece(boolean isRed) {
        super(isRed ? "兵" : "卒", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO：实现帅的移动规则。
        return false;
    }
}
