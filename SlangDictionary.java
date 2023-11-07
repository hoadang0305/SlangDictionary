import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class SlangDictionary {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private static Dictionary mainDictionary;
    Dictionary suggest = new Dictionary();
    Dictionary history = new Dictionary();
    DefaultListModel<String> listHistory = new DefaultListModel<>();

    public SlangDictionary() {
        mainDictionary = new Dictionary();
        history.readFileWord("history.txt");
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

        JPanel page4 = createAdminPage();
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
                for (Map.Entry<String, Set<String>> entry : history.getDictionary().entrySet()) {
                    listHistory.addElement(entry.getKey());
                }
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

        // ---body1
        JPanel body1 = new JPanel();
        JLabel titleInput = new JLabel("Enter Search Word", JLabel.CENTER);
        titleInput.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(300, 27));
        body1.add(titleInput, BorderLayout.WEST);
        body1.add(input, BorderLayout.EAST);
        // ---body2
        JPanel body2 = new JPanel();
        body2.setLayout(new BorderLayout());
        // ---------body2-1
        JPanel body2_1 = new JPanel();
        body2_1.setLayout(new BorderLayout());
        JLabel suggestLabel = new JLabel("Suggest", JLabel.CENTER);
        suggestLabel.setFont(new Font("Arial", Font.BOLD, 16));
        suggestLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        body2_1.add(suggestLabel, BorderLayout.NORTH);

        JList<String> suggestList = new JList<>();
        suggestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestList.setFixedCellHeight(38);
        suggestList.setFixedCellWidth(190);
        JScrollPane scrollPane = new JScrollPane(suggestList);
        DefaultListModel<String> listSuggest = new DefaultListModel<>();
        // xử lí listen của suggest
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {

                    listSuggest.clear();
                    suggest = mainDictionary.searchBySlang(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {
                    listSuggest.clear();
                    suggest = mainDictionary.searchBySlang(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {
                    listSuggest.clear();
                    suggest = mainDictionary.searchBySlang(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }
        });

        body2_1.add(scrollPane, BorderLayout.SOUTH);
        body2.add(body2_1, BorderLayout.WEST);

        // --------body2-2
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("Result", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        body2_2.add(resultLabel, BorderLayout.NORTH);
        //
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(350, 306));
        resultTextPane.setEditable(false);
        resultTextPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        body2_2.add(resultTextPane, BorderLayout.SOUTH);
        body2.add(body2_2, BorderLayout.EAST);
        // listen cho result
        suggestList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    resultTextPane.setText("");
                    int selectIndex = suggestList.getSelectedIndex();
                    if (selectIndex != -1) {
                        String key = listSuggest.getElementAt(selectIndex);
                        Set<String> define = new HashSet<>(mainDictionary.getDictionary().get(key));
                        // lưu vào lịch sử
                        history.addSlangWord(key, define);
                        history.print();
                        List<String> temp = new ArrayList<>(define);
                        // Tạm thời để vầy
                        StyledDocument doc = resultTextPane.getStyledDocument();
                        SimpleAttributeSet setKey = new SimpleAttributeSet();
                        StyleConstants.setForeground(setKey, Color.RED);
                        StyleConstants.setFontSize(setKey, 40);
                        StyleConstants.setFontFamily(setKey, "Arial");
                        StyleConstants.setBold(setKey, true);
                        StyleConstants.setItalic(setKey, true);
                        try {
                            doc.insertString(doc.getLength(), key, setKey);
                            doc.insertString(doc.getLength(), "\n\n\n", null);
                            doc.insertString(doc.getLength(), "Definition:\n", null);
                            for (String ele : temp) {
                                doc.insertString(doc.getLength(), "- ", null);
                                doc.insertString(doc.getLength(), ele, null);
                                doc.insertString(doc.getLength(), "\n", null);
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }
            }
        });
        // ----------------
        bodyPanel.add(body1, BorderLayout.NORTH);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listSuggest.clear();
                input.setText("");
                resultTextPane.setText("");
                suggest.clearDictionary();
                history.writeFileWord("history.txt");
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
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        topPanel.add(titleLabel);
        // bodyPanel

        // ---body1
        JPanel body1 = new JPanel();
        JLabel titleInput = new JLabel("Enter Search Define", JLabel.CENTER);
        titleInput.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(300, 27));
        JButton searchButton = new JButton("Search");

        body1.add(titleInput, BorderLayout.WEST);
        body1.add(input, BorderLayout.EAST);
        body1.add(searchButton, BorderLayout.LINE_END);

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

        // =======================
        JList<String> suggestList = new JList<>();
        suggestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestList.setFixedCellHeight(38);
        suggestList.setFixedCellWidth(190);
        JScrollPane scrollPane = new JScrollPane(suggestList);
        DefaultListModel<String> listSuggest = new DefaultListModel<>();
        // xử lí listen của suggest
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {

                    listSuggest.clear();
                    suggest = mainDictionary.serchByDefine(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {
                    listSuggest.clear();
                    suggest = mainDictionary.serchByDefine(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (input.getText().length() > 0) {
                    listSuggest.clear();
                    suggest = mainDictionary.serchByDefine(input.getText());
                    for (Map.Entry<String, Set<String>> entry : suggest.getDictionary().entrySet()) {
                        listSuggest.addElement(entry.getKey());
                    }
                    suggestList.setModel(listSuggest);
                } else {
                    listSuggest.clear();
                }
            }
        });

        body2_1.add(scrollPane, BorderLayout.SOUTH);
        body2.add(body2_1, BorderLayout.WEST);

        // --------body2-2

        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("Result", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        body2_2.add(resultLabel, BorderLayout.NORTH);
        //
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(350, 306));
        resultTextPane.setEditable(false);
        // resultTextPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        body2_2.add(new JScrollPane(resultTextPane), BorderLayout.SOUTH);
        body2.add(body2_2, BorderLayout.EAST);
        // -----------------
        // xử lí listen
        // ----------------
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listSuggest.clear();
                resultTextPane.setText("");
                ArrayList<String> list = new ArrayList<>(suggest.getDictionary().keySet());
                for (String ele : list) {
                    Set<String> define = new HashSet<>(mainDictionary.getDictionary().get(ele));
                    List<String> temp = new ArrayList<>(define);
                    StyledDocument doc = resultTextPane.getStyledDocument();
                    SimpleAttributeSet setKey = new SimpleAttributeSet();
                    StyleConstants.setForeground(setKey, Color.RED);
                    StyleConstants.setFontSize(setKey, 40);
                    StyleConstants.setFontFamily(setKey, "Arial");
                    StyleConstants.setBold(setKey, true);
                    StyleConstants.setItalic(setKey, true);
                    try {
                        doc.insertString(doc.getLength(), "\n", null);
                        doc.insertString(doc.getLength(), ele, setKey);
                        doc.insertString(doc.getLength(), "\n\n", null);
                        doc.insertString(doc.getLength(), "Definition:\n", null);
                        for (String element : temp) {
                            doc.insertString(doc.getLength(), "- ", null);
                            doc.insertString(doc.getLength(), element, null);
                            doc.insertString(doc.getLength(), "\n", null);
                        }
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }

            }
        });

        bodyPanel.add(body1, BorderLayout.NORTH);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listSuggest.clear();
                input.setText("");
                resultTextPane.setText("");
                suggest.clearDictionary();
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
        JList<String> historyList = new JList<>();
        historyList.setFixedCellHeight(38);
        historyList.setFixedCellWidth(100);
        JScrollPane scrollPane2 = new JScrollPane(historyList);
        historyList.setModel(listHistory);

        //
        bodyPanel.add(scrollPane2, BorderLayout.CENTER);
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(80, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(80, 0)));

        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.writeFileWord("history.txt");
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        return main;
    }

    private JPanel createAdminPage() {
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
        JLabel titleLabel = new JLabel("Admin Mode", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        topPanel.add(titleLabel);
        // bodyPanel

        // bottomPanel
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.writeFileWord("history.txt");
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        // main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);

        // --------------------------
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
