import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverGUI extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private JButton solveButton = new JButton("Solve");
    private JButton clearButton = new JButton("Clear");

    public SudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 400));

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                gridPanel.add(cells[row][col]);
            }
        }

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[][] board = new int[SIZE][SIZE];
                try {
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            String text = cells[row][col].getText();
                            if (!text.isEmpty()) {
                                board[row][col] = Integer.parseInt(text);
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    if (solveSudoku(board)) {
                        displayBoard(board);
                    } else {
                        JOptionPane.showMessageDialog(null, "No solution exists for the given Sudoku puzzle.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers (1-9).");
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < SIZE; row++) {
                    for (int col = 0; col < SIZE; col++) {
                        cells[row][col].setText("");
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void displayBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean findEmptyLocation(int[][] board, int[] emptyPos) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    emptyPos[0] = row;
                    emptyPos[1] = col;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean solveSudoku(int[][] board) {
        int[] emptyPos = new int[2];
        if (!findEmptyLocation(board, emptyPos)) {
            return true;
        }

        int row = emptyPos[0];
        int col = emptyPos[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                if (solveSudoku(board)) {
                    return true;
                }

                board[row][col] = 0;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuSolverGUI().setVisible(true);
            }
        });
    }
}
