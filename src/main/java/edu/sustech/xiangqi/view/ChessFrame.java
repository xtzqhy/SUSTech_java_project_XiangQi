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
        // 2. 新建一个带有居中布局的容器面板
        JPanel containerPanel = new JPanel(new java.awt.GridBagLayout());
        containerPanel.setBackground(new java.awt.Color(239, 218, 179)); // 让底色和棋盘一致
        containerPanel.add(boardPanel); // 把棋盘放进正中间
        setContentPane(containerPanel);

        // 3. 建立顶部菜单栏 (这会自动在左上角生成)
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("游戏");
        
        newGameItem = new JMenuItem("新游戏");
        resignItem = new JMenuItem("认输");
        drawItem = new JMenuItem("求和");
        
        gameMenu.add(newGameItem);
        gameMenu.add(resignItem);
        gameMenu.add(drawItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar); // 把菜单栏设置到窗口上

        // 4. 窗口设置
        setSize(1000, 1000);
        // setResizable(false); // 如果你想让用户能拉伸窗口，可以删掉这行
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
