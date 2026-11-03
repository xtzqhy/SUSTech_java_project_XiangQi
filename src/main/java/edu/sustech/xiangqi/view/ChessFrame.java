package edu.sustech.xiangqi.view;

import edu.sustech.xiangqi.model.Board;

import javax.swing.*;

public final class ChessFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final BoardPanel boardPanel;
    private final JMenuItem newGameItem;
    private final JMenuItem resignItem;
    private final JMenuItem drawItem;
    private final JMenuItem undoItem; 
    private JLabel statusLabel; 

    public ChessFrame(Board board) {
        super("中国象棋");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardPanel = new BoardPanel(board);

        setLayout(new java.awt.BorderLayout());

        JPanel centerPanel = new JPanel(new java.awt.GridBagLayout());
        centerPanel.setBackground(new java.awt.Color(239, 218, 179));
        centerPanel.add(boardPanel);
        add(centerPanel, java.awt.BorderLayout.CENTER);

        statusLabel = new JLabel("当前回合：红方", JLabel.CENTER);
        statusLabel.setFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 20));
        statusLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(statusLabel, java.awt.BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("选项");
        
        newGameItem = new JMenuItem("新游戏");
        resignItem = new JMenuItem("认输");
        drawItem = new JMenuItem("求和");
        undoItem = new JMenuItem("悔棋");
        
        gameMenu.add(newGameItem);
        gameMenu.add(resignItem);
        gameMenu.add(drawItem);
        gameMenu.add(undoItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar); 

        
        setSize(1000, 1000);
        setLocationRelativeTo(null);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }
    public JMenuItem getResignItem() {
        return resignItem;
    }
    public JMenuItem getDrawItem() {
        return drawItem;
    }
    public JMenuItem getUndoItem() { 
        return undoItem; 
    } 

    public void updateStatus(String text, boolean isWarning) {
        statusLabel.setText(text);
        statusLabel.setForeground(isWarning ? java.awt.Color.RED : java.awt.Color.BLACK);
    }
}
