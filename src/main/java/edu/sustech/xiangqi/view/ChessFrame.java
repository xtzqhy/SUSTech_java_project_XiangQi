package edu.sustech.xiangqi.view;

import edu.sustech.xiangqi.model.Board;

import javax.swing.*;

public final class ChessFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final BoardPanel boardPanel;
    private final JMenuItem newGameItem;
    private final JMenuItem resignItem;
    private final JMenuItem drawItem;

    public ChessFrame(Board board) {
        super("中国象棋");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardPanel = new BoardPanel(board);
        
        JPanel containerPanel = new JPanel(new java.awt.GridBagLayout());
        containerPanel.setBackground(new java.awt.Color(239, 218, 179)); 
        containerPanel.add(boardPanel); 
        setContentPane(containerPanel);

        
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("选项");
        
        newGameItem = new JMenuItem("新游戏");
        resignItem = new JMenuItem("认输");
        drawItem = new JMenuItem("求和");
        
        gameMenu.add(newGameItem);
        gameMenu.add(resignItem);
        gameMenu.add(drawItem);
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
}
