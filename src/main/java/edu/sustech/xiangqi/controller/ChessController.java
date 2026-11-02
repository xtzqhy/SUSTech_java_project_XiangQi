package edu.sustech.xiangqi.controller;

import edu.sustech.xiangqi.model.Board;
import edu.sustech.xiangqi.model.Piece;
import edu.sustech.xiangqi.view.BoardPanel;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 处理点击和选中状态
public class ChessController extends MouseAdapter {
    private final Board board;
    private final BoardPanel boardPanel;

    // 默认 -1 没有选中任何棋子
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isGameOver = false;

    public ChessController(Board board, BoardPanel boardPanel) {
        this.board = board;
        this.boardPanel = boardPanel;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (isGameOver) return;
        if (!SwingUtilities.isLeftMouseButton(event)) {
            return;
        }

        Point position = boardPanel.getIntersectionAt(event.getX(), event.getY());
        if (position != null) {
            handleClick(position.y, position.x);
        }
    }

    private void handleClick(int row, int col) {
        if (row == selectedRow && col == selectedCol) {
            clearSelection();
            return;
        }

        Piece clickedPiece = board.getPieceAt(row, col);
        boolean isRedTurn = board.isRedTurn();

        if (selectedRow == -1) {
            if (clickedPiece != null) {
                //判断是不是选了对方的棋子
                if (clickedPiece.isRed() != isRedTurn) {
                    return;
                }
                select(row, col);
            }
            return;
        }

        //切换选中
        Piece selectedPiece = board.getPieceAt(selectedRow, selectedCol);
        if (clickedPiece != null && clickedPiece.isRed() == selectedPiece.isRed()) {
            select(row, col);
            return;
        }

        boolean moveSuccess = board.tryMove(selectedRow, selectedCol, row, col);
        if (moveSuccess) {
            boardPanel.showLastMove(selectedRow, selectedCol, row, col);
            clearSelection();
            checkGameOver();
        } else {
            boolean isCurrentSideInCheck = board.isInCheck(isRedTurn);
            if (isCurrentSideInCheck) {
                JOptionPane.showMessageDialog(boardPanel, 
                    "你正在被将军，请应将", 
                    "将军警报", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(boardPanel, 
                    "操作不合法", 
                    "违规操作", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void select(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        boardPanel.showSelection(row, col);
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        boardPanel.showSelection(-1, -1);
    }

    private void checkGameOver() {
        boolean sideToMove = board.isRedTurn();

        if (board.hasAnyLegalMove(sideToMove)) {
            return;
        }

        boolean inCheck = board.isInCheck(sideToMove);
        String winner = sideToMove ? "黑方" : "红方";
        String reason = inCheck ? "将死" : "困毙";

        JOptionPane.showMessageDialog(
                boardPanel,
                winner + "胜！（对方" + reason + "）",
                "游戏结束",
                JOptionPane.INFORMATION_MESSAGE);
        isGameOver = true;
    }

    //新游戏
    public void resetGame() {
        board.initializePieces();            
        boardPanel.clearLastMove();     
        clearSelection();               
        isGameOver = false;             
    }

    //认输
    public void resign() {
        if (isGameOver) return;         
        
        boolean isRedTurn = board.isRedTurn();
        String winner = isRedTurn ? "黑方" : "红方"; 
        
        JOptionPane.showMessageDialog(
                boardPanel, 
                winner + "胜！（对方认输）", 
                "游戏结束", 
                JOptionPane.INFORMATION_MESSAGE);
                
        isGameOver = true;
    }

    //求和
    public void agreeDraw() {
        if (isGameOver) return;

        int choice = JOptionPane.showConfirmDialog(
                null, 
                "对方同意和棋吗？", 
                "求和", 
                JOptionPane.YES_NO_OPTION);
                
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                    null, 
                    "游戏结束，双方和棋！", 
                    "和棋", 
                    JOptionPane.INFORMATION_MESSAGE);
            isGameOver = true;
        }
    }
}
