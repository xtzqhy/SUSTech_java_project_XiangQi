package edu.sustech.xiangqi.model;

public class MoveRecord {
    public final int fromRow, fromCol;
    public final int toRow, toCol;
    public final Piece capturedPiece; 

    public MoveRecord(int fromRow, int fromCol, int toRow, int toCol, Piece capturedPiece) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.capturedPiece = capturedPiece;
    }
}