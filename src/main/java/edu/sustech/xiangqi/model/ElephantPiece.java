package edu.sustech.xiangqi.model;

public class ElephantPiece extends Piece {
    public ElephantPiece(boolean isRed) {
        super(isRed ? "相" : "象", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
       //规则：只能走田字格，不能过河，田字中心有子不能走
       
       //田字
        if (Math.abs(toRow - fromRow) != 2 || Math.abs(toCol - fromCol) != 2) return false;

        //不能过河
        if (isRed() && toRow < 5) return false;
        if (!isRed() && toRow > 4) return false;
        
        //中心不能有子
        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;
        if (board.getPieceAt(midRow, midCol) != null) return false;
        return true;
    }
}
