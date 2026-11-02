package edu.sustech.xiangqi.model;

public class ChariotPiece extends Piece {
    public ChariotPiece(boolean isRed) {
        super(isRed ? "車" : "車", isRed);
    }
     @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        //规则：只能走同行或者同列，且中间不能有其他子，不限格数
        
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        if (fromRow == toRow) {
            int minCol = Math.min(fromCol, toCol);
            int maxCol = Math.max(fromCol, toCol);
            for (int c = minCol + 1; c < maxCol; c++) {
                if (board.getPieceAt(fromRow, c) != null) {
                    return false;
                }
            }
        } else {
            int minRow = Math.min(fromRow, toRow);
            int maxRow = Math.max(fromRow, toRow);
            for (int r = minRow + 1; r < maxRow; r++) {
                if (board.getPieceAt(r, fromCol) != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
