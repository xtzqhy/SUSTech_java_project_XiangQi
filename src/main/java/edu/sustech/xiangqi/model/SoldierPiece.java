package edu.sustech.xiangqi.model;

public class SoldierPiece extends Piece {
    public SoldierPiece(boolean isRed) {
        super(isRed ? "兵" : "卒", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：一次只能一格，过河前只能往前走，过河后可以左右走，但是永远不能后退

        //注意红黑方方向
        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);

        //不能后退
        if (isRed()) {
            if (rowDiff > 0) return false; //红
        } else {
            if (rowDiff < 0) return false; //黑
        }
        
        //只能一格+不能过河
        if (colDiff > 1) return false;
        if (colDiff == 0 && Math.abs(rowDiff) != 1) return false;
        if (colDiff == 1) {
            if (rowDiff != 0) return false;
            if (isRed() && fromRow > 4) return false; 
            if (!isRed() && fromRow < 5) return false; 
        }
        return true;
    }
}
