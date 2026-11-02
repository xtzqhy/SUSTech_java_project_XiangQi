package edu.sustech.xiangqi.model;

public class HorsePiece extends Piece {
    public HorsePiece(boolean isRed) {
        super(isRed ? "馬" : "馬", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：只能走日，不能蹩马腿

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        int absRow = Math.abs(rowDiff);
        int absCol = Math.abs(colDiff);

        //日字
        if (!((absRow == 2 && absCol == 1) || (absRow == 1 && absCol == 2))) return false;
        
        //蹩马腿检查
        if (absRow == 2) {
            // 竖着走日，马腿在正前方
            int legRow = fromRow + (rowDiff > 0 ? 1 : -1);
            if (board.getPieceAt(legRow, fromCol) != null) return false;
        } else {
            // 横着走日，马腿在正左/右方
            int legCol = fromCol + (colDiff > 0 ? 1 : -1);
            if (board.getPieceAt(fromRow, legCol) != null) return false;
        }   
        return true;
    }
}
