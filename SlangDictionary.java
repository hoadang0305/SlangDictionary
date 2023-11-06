import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SlangDictionary {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Dictionary mainDictionary;

    public SlangDictionary() {
        mainDictionary = new Dictionary();
        mainDictionary.readFileWord("slang.txt");
        frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Trang Menu
        JPanel menuPage = createMenuPage();
        mainPanel.add(menuPage, "menu");

        // Trang Chức năng 1
        JPanel page1 = createSlangSearchPage();
        mainPanel.add(page1, "page1");

        // Trang Chức năng 2
        JPanel page2 = createDefineSearchPage();
        mainPanel.add(page2, "page2");

        // Thêm 4 trang chức năng khác ở đây
        JPanel page3 = createHistoryPage();
        mainPanel.add(page3, "page3");

        JPanel page4 = createFunctionPage("Chức năng 4");
        mainPanel.add(page4, "page4");

        JPanel page5 = createFunctionPage("Chức năng 5");
        mainPanel.add(page5, "page5");

        JPanel page6 = createFunctionPage("Chức năng 6");
        mainPanel.add(page6, "page6");

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private JPanel createMenuPage() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Slang Dictionary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        menuPanel.add(titleLabel, BorderLayout.NORTH);
        //
        JPanel searchPanel = new JPanel(new GridLayout(1, 2, 8, 8));

        JButton searchBySlangButton = new JButton("Search by SlangWord");
        searchBySlangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page1");
            }
        });
        //
        JButton searchByDefineButton = new JButton("Search by Define");
        searchByDefineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page2");
            }
        });
        searchPanel.add(searchBySlangButton);
        searchPanel.add(searchByDefineButton);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 8, 8));
        //
        JButton historyButton = new JButton("History");
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page3");
            }
        });
        //
        JButton adminButton = new JButton("Admin mode");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page4");
            }
        });
        //
        JButton resetButton = new JButton("Dictionary Recovery");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page5");
            }
        });
        JButton miniGameButton = new JButton("Mini Game");
        miniGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6");
            }
        });

        buttonPanel.add(searchPanel);
        buttonPanel.add(historyButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(miniGameButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    private JPanel createSlangSearchPage() {
        JPanel main = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bodyPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        //
        main.setLayout(new BorderLayout());
        topPanel.setLayout(new FlowLayout());
        bodyPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new FlowLayout());
        // topPanel
        JLabel titleLabel = new JLabel("Search by SlangWord", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        topPanel.add(titleLabel);
        // bodyPanel
        Dictionary suggest;
        // ---body1
        JPanel body1 = new JPanel();
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(300, 27));
        JButton search = new JButton("Search");
        body1.add(input, BorderLayout.WEST);
        body1.add(search, BorderLayout.EAST);
        //
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (input.getText().length() > 0) {
                    // suggest = mainDictionary.searchBySlang(input.getText());
                }
            }
        });
        // ---body2
        JPanel body2 = new JPanel();
        body2.setLayout(new BorderLayout());
        // ---------body2-1
        // sau này sử dụng Jtextarea để có thể cuộn xuống
        JPanel body2_1 = new JPanel();
        body2_1.setLayout(new BorderLayout());
        JLabel suggestLabel = new JLabel("Suggest", JLabel.CENTER);
        suggestLabel.setFont(new Font("Arial", Font.BOLD, 16));
        suggestLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        body2_1.add(suggestLabel, BorderLayout.NORTH);
        JTextPane suggesTextPane = new JTextPane();
        suggesTextPane.setPreferredSize(new Dimension(200, 300));
        suggesTextPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        body2_1.add(suggesTextPane, BorderLayout.SOUTH);
        body2.add(body2_1, BorderLayout.WEST);
        // --------body2-2
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("Result", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        body2_2.add(resultLabel, BorderLayout.NORTH);
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(350, 300));
        resultTextPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        body2_2.add(resultTextPane, BorderLayout.SOUTH);
        body2.add(body2_2, BorderLayout.EAST);
        // ----------------
        bodyPanel.add(body1, BorderLayout.NORTH);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);
        // chèn khoảng trống viền ở 2 bên
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        return main;
    }

    private JPanel createDefineSearchPage() {
        JPanel main = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bodyPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        //
        main.setLayout(new BorderLayout());
        topPanel.setLayout(new FlowLayout());
        bodyPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new FlowLayout());
        // topPanel
        JLabel titleLabel = new JLabel("Search by Define", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        topPanel.add(titleLabel);
        // bodyPanel
        // ---body1
        JPanel body1 = new JPanel();
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(300, 27));
        JButton search = new JButton("Search");
        body1.add(input, BorderLayout.WEST);
        body1.add(search, BorderLayout.EAST);

        // ---body2
        JPanel body2 = new JPanel();
        body2.setLayout(new BorderLayout());
        // ---------body2-1
        JPanel body2_1 = new JPanel();
        body2_1.setLayout(new BorderLayout());
        JLabel suggestLabel = new JLabel("Suggest", JLabel.CENTER);
        suggestLabel.setFont(new Font("Arial", Font.BOLD, 16));
        suggestLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        body2_1.add(suggestLabel, BorderLayout.NORTH);
        // sau này sử dụng Jtextarea để có thể cuộn xuống
        JTextPane suggesTextPane = new JTextPane();
        suggesTextPane.setPreferredSize(new Dimension(200, 300));
        suggesTextPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        body2_1.add(suggesTextPane, BorderLayout.SOUTH);
        body2.add(body2_1, BorderLayout.WEST);
        // --------body2-2
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("Result", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        body2_2.add(resultLabel, BorderLayout.NORTH);
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(350, 300));
        resultTextPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        body2_2.add(resultTextPane, BorderLayout.SOUTH);
        body2.add(body2_2, BorderLayout.EAST);
        // ----------------
        bodyPanel.add(body1, BorderLayout.NORTH);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);
        // chèn khoảng trống viền ở 2 bên
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        return main;
    }

    private JPanel createHistoryPage() {
        JPanel main = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bodyPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        //
        main.setLayout(new BorderLayout());
        topPanel.setLayout(new FlowLayout());
        bodyPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new FlowLayout());
        // topPanel
        JLabel titleLabel = new JLabel("History", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        topPanel.add(titleLabel);
        // bodyPanel

        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        return main;
    }

    private JPanel createFunctionPage(String functionTitle) {
        JPanel functionPanel = new JPanel();
        JButton backButton = new JButton("Quay lại Menu");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "menu");
            }
        });

        functionPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(functionTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        functionPanel.add(titleLabel, BorderLayout.NORTH);
        functionPanel.add(backButton, BorderLayout.SOUTH);

        return functionPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlangDictionary());
    }
}
