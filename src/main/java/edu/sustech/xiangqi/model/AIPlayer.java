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
        MoveRecord globalBestMove = null;

        for (int currentDepth = 1; currentDepth <= depth; currentDepth++) {
            Board simBoard = this.board.copy();     
            List<MoveRecord> moves = simBoard.getAllLegalMoves(isRed);
            if (moves.isEmpty()) return null; 

            sortMoves(simBoard,moves);

            if (globalBestMove != null) {
                int bestIdx = -1;
                for (int i = 0; i < moves.size(); i++) {
                    MoveRecord m = moves.get(i);
                    if (m.fromRow == globalBestMove.fromRow && m.fromCol == globalBestMove.fromCol && 
                        m.toRow == globalBestMove.toRow && m.toCol == globalBestMove.toCol) {
                        bestIdx = i;
                        break;
                    }
                }
                if (bestIdx!=-1) {
                    MoveRecord temp = moves.remove(bestIdx);
                    moves.add(0, temp);
                }
            }

            MoveRecord currentBestMove = null;
            int bestScore = isRed ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;

            for (MoveRecord move : moves) {
                simBoard.tryMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
                
                int currentScore = minimax(simBoard, currentDepth - 1, alpha, beta, !isRed);
                
                simBoard.undoMove();
                
                if (isRed) {
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        currentBestMove = move;
                    }
                    alpha = Math.max(alpha, bestScore); 
                } else {
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        currentBestMove = move;
                    }
                    beta = Math.min(beta, bestScore);   
                }
            }
            
            globalBestMove = currentBestMove;
        }
        return globalBestMove;
    }

    private int minimax(Board simBoard,int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || simBoard.getAllLegalMoves(isMaximizing).isEmpty()) {
            return simBoard.evaluate();
        }

         
        if (isMaximizing) { 
            List<MoveRecord> moves = simBoard.getAllLegalMoves(true);
            sortMoves(simBoard,moves);
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
            sortMoves(simBoard,moves);
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

    //排序剪枝
    private void sortMoves(Board simBoard, List<MoveRecord> moves) {
        moves.sort((m1, m2) -> {
            int score1 = 0, score2 = 0;
            
            if (m1.capturedPiece != null) {
                // 被吃子价值 * 10，减去 攻击者价值（越小越好）
                int victimValue = Board.getBaseValue(m1.capturedPiece);
                int attackerValue = Board.getBaseValue(simBoard.getPieceAt(m1.fromRow, m1.fromCol));
                score1 = victimValue * 10 - attackerValue;
            }
            
            if (m2.capturedPiece != null) {
                int victimValue = Board.getBaseValue(m2.capturedPiece);
                int attackerValue = Board.getBaseValue(simBoard.getPieceAt(m2.fromRow, m2.fromCol));
                score2 = victimValue * 10 - attackerValue;
            }
            return score2 - score1; // 降序
        });
    }
}