package edu.sustech.xiangqi.controller;

import edu.sustech.xiangqi.model.Board;
import edu.sustech.xiangqi.model.Piece;
import edu.sustech.xiangqi.view.BoardPanel;
import edu.sustech.xiangqi.view.ChessFrame;
import edu.sustech.xiangqi.model.AIPlayer;
import edu.sustech.xiangqi.model.MoveRecord;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 处理点击和选中状态
public class ChessController extends MouseAdapter {
    private final Board board;
    private final BoardPanel boardPanel;
    private final ChessFrame frame;     

    // 默认 -1 没有选中任何棋子
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isGameOver = false;

    private int maxUndoCount = 3;
    private int redUndoCount = 3;    
    private int blackUndoCount = 3;  

    private boolean isAIThinking = false;
    private AIPlayer aiPlayer;      
    private boolean isAIMode = false; 
    private boolean isAIRed = false; 

    public ChessController(Board board, BoardPanel boardPanel, ChessFrame frame) {
        this.board = board;
        this.boardPanel = boardPanel;
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (isGameOver || isAIThinking) return;
        if (!SwingUtilities.isLeftMouseButton(event)) {
            return;
        }

        Point position = boardPanel.getIntersectionAt(event.getX(), event.getY());
        if (position != null) {
            handleClick(position.y, position.x);
        }
    }

    private void handleClick(int row, int col) {
        if (isGameOver || isAIThinking) return;
        if (row == selectedRow && col == selectedCol) {
            clearSelection();
            return;
        }

        Piece clickedPiece = board.getPieceAt(row, col);
        boolean isRedTurn = board.isRedTurn();

        if (isAIMode && isRedTurn == isAIRed) {
            return;
        }

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
            boolean nextSide = board.isRedTurn();
            String sideName = nextSide ? "红方" : "黑方";
            String undoInfo = " (剩余悔棋次数 红:" + redUndoCount + " 黑:" + blackUndoCount + ")";
            
            if (board.isInCheck(nextSide)) {
                frame.updateStatus("当前回合：" + sideName + "正被将军！" + undoInfo, true); 
            } else {
                frame.updateStatus("当前回合：" + sideName + undoInfo, false); 
            }
            
            checkGameOver();

            if (isAIMode && board.isRedTurn() == isAIRed && !isGameOver) {
                triggerAIMove();
            }
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

        String winner = sideToMove ? "黑方" : "红方";

        JOptionPane.showMessageDialog(
                boardPanel,
                winner + "胜！",
                "游戏结束",
                JOptionPane.INFORMATION_MESSAGE);
        isGameOver = true;
        frame.updateStatus("游戏结束！", true);
    }

    //新游戏
    public void resetGame() {
        while(true){
            int modeChoice = JOptionPane.showConfirmDialog(null, 
                    "是否开启人机对战模式？", 
                    "游戏模式选择", 
                    JOptionPane.YES_NO_OPTION);
            if (modeChoice == JOptionPane.CLOSED_OPTION) return; 
            isAIMode = (modeChoice == JOptionPane.YES_OPTION);
            if (isAIMode) {
                int colorChoice = JOptionPane.showConfirmDialog(null, 
                        "你想执红（先手）还是执黑（后手）？\n选“是”执红，选“否”执黑。", 
                        "选择阵营", JOptionPane.YES_NO_OPTION);
                
                if (colorChoice == JOptionPane.CLOSED_OPTION) continue; 
                isAIRed = (colorChoice == JOptionPane.NO_OPTION); 
                aiPlayer = new AIPlayer(board, isAIRed);
            } else {
                aiPlayer = null; 
            }
            break;
        }

        while (true) {
            String input = JOptionPane.showInputDialog(null, 
                    "请输入双方各自允许的最大悔棋次数（必须是非负整数）：", 
                    "游戏设置", 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (input == null) {
                return; 
            }
            
            try {
                int parsedValue = Integer.parseInt(input.trim());
                if (parsedValue < 0) {
                    JOptionPane.showMessageDialog(null, 
                            "输入不合法！悔棋次数必须是非负整数。", 
                            "输入错误", 
                            JOptionPane.ERROR_MESSAGE);
                    continue; 
                }
                maxUndoCount = parsedValue;
                break; 
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "输入不合法！悔棋次数必须是非负整数。", 
                        "输入错误", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        redUndoCount = maxUndoCount;
        blackUndoCount = maxUndoCount;
        board.initializePieces();            
        boardPanel.clearLastMove();     
        clearSelection();               
        isGameOver = false;      
        frame.updateStatus("当前回合：红方 (剩余悔棋次数 红:" + redUndoCount + " 黑:" + blackUndoCount + ")", false);
        boardPanel.repaint();  

        if (isAIMode && isAIRed) {
            triggerAIMove();
        }
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

    //悔棋
    public void undo() {
        if (isGameOver || isAIThinking) return;

        boolean isLastMoverRed = !board.isRedTurn(); 
        boolean playerIsRed = !isAIRed;
        if (isAIMode) {
            if(board.getHistory().size()<=1){
                JOptionPane.showMessageDialog(null, "已经没有棋可以悔了！");
                return;
            }
            if (playerIsRed && redUndoCount <= 0) {
                JOptionPane.showMessageDialog(null, "你的悔棋次数已用完！");
                return;
            } else if (!playerIsRed && blackUndoCount <= 0) {
                JOptionPane.showMessageDialog(null, "你的悔棋次数已用完！");
                return;
            }
            if (playerIsRed) redUndoCount--;
            else blackUndoCount--;
            board.undoMove();
            board.undoMove();
        } else {
            if (board.getHistory().isEmpty()) {
                JOptionPane.showMessageDialog(null, "已经没有棋可以悔了！");
                return;
            }
            if (isLastMoverRed && redUndoCount <= 0) {
                JOptionPane.showMessageDialog(null, "红方悔棋次数已用完，无法撤回红方的棋！");
                return;
            } else if (!isLastMoverRed && blackUndoCount <= 0) {
                JOptionPane.showMessageDialog(null, "黑方悔棋次数已用完，无法撤回黑方的棋！");
                return;
            }
            if (isLastMoverRed) redUndoCount--;
            else blackUndoCount--;
            board.undoMove();
        }
        clearSelection();
        boardPanel.clearLastMove();
        boardPanel.repaint();
        
        if(isAIMode){
            frame.updateStatus("已撤回你的棋。当前回合：" + (playerIsRed ? "红方" : "黑方") + 
                            " (剩余悔棋次数 红:" + redUndoCount + " 黑:" + blackUndoCount + ")", false);
        }
        else{
            boolean currentSide = board.isRedTurn(); 
            String lastMoverName = isLastMoverRed ? "红方" : "黑方";
            
            frame.updateStatus("已撤回" + lastMoverName + "的棋。当前回合：" + (currentSide ? "红方" : "黑方") + 
                            " (剩余悔棋次数 红:" + redUndoCount + " 黑:" + blackUndoCount + ")", false);
        }
    }

    private void triggerAIMove() {
        isAIThinking = true;
        frame.updateStatus("AI 正在思考中，请稍候...", false);

        new Thread(() -> {
            MoveRecord aiMove = aiPlayer.findBestMove( 4); 

            javax.swing.SwingUtilities.invokeLater(() -> {
                if (aiMove != null && !isGameOver&& isAIThinking) { 
                    board.tryMove(aiMove.fromRow, aiMove.fromCol, aiMove.toRow, aiMove.toCol);
                    boardPanel.showLastMove(aiMove.fromRow, aiMove.fromCol, aiMove.toRow, aiMove.toCol);
                    boardPanel.repaint();
                    
                    boolean nextTurnIsRed = board.isRedTurn();
                    String nextSideName = nextTurnIsRed ? "红方" : "黑方";
                    String undoInfo = "(剩余悔棋次数 红:" + redUndoCount + " 黑:" + blackUndoCount + ")";
                    if (board.isInCheck(nextTurnIsRed)) {
                        frame.updateStatus("当前回合：" + nextSideName + "正被将军！" + undoInfo, true);
                    } else {
                        frame.updateStatus("当前回合：" + nextSideName + undoInfo, false);
                    }
                    
                    checkGameOver();
                }
                
                isAIThinking = false; 
            });
        }).start();
    }
}
