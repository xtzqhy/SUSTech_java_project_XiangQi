package edu.sustech.xiangqi.model;

import java.util.List;

public class AIPlayer {
    private final Board board;
    private final boolean isRed; 

    public AIPlayer(Board board, boolean isRed) {
        this.board = board;
        this.isRed = isRed;
    }

    //规定红方想让得分最大，黑方想让得分最小
    public MoveRecord findBestMove(int depth) {
        Board simBoard = this.board.copy();     

        List<MoveRecord> moves = simBoard.getAllLegalMoves(isRed);
        if (moves.isEmpty()) return null; 

        MoveRecord bestMove = null;
        int bestScore = isRed ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (MoveRecord move : moves) {
            simBoard.tryMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
            int currentScore = minimax(simBoard,depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !isRed);
            simBoard.undoMove();
            if (isRed) {
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
            } else {
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    private int minimax(Board simBoard,int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || board.getAllLegalMoves(isMaximizing).isEmpty()) {
            return simBoard.evaluate();
        }

        if (isMaximizing) { 
            List<MoveRecord> moves = simBoard.getAllLegalMoves(true);
            for (MoveRecord move : moves) {
                simBoard.tryMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
                alpha = Math.max(alpha,minimax(simBoard,depth - 1, alpha, beta, false));
                simBoard.undoMove();
                if (alpha >= beta) {
                    break; 
                }
            }
            return alpha;
        } else { 
            List<MoveRecord> moves = simBoard.getAllLegalMoves(false);
            for (MoveRecord move : moves) {
                simBoard.tryMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
                beta = Math.min(beta,minimax(simBoard,depth - 1, alpha, beta, true));
                simBoard.undoMove();
                if (alpha >= beta) {
                    break; 
                }
            }
            return beta;
        }
    }
}