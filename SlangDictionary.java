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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.List;

public class SlangDictionary {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private static Dictionary mainDictionary;
    Dictionary suggest = new Dictionary();
    Dictionary history = new Dictionary();
    DefaultListModel<String> listHistory = new DefaultListModel<>();
    TreeMap<String, Set<String>> randomWord = new TreeMap<>();
    TreeMap<String, List<String>> quizSlang = new TreeMap<>();
    TreeMap<String, List<String>> quizDefine = new TreeMap<>();
    String realAnswer;
    int index = 0;

    public SlangDictionary() {
        mainDictionary = new Dictionary();
        history.readFileWord("history.txt");
        mainDictionary.readFileWord("slangWordBackup.txt");
        frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Trang Menu
        JPanel menuPage = createMenuPage();
        mainPanel.add(menuPage, "menu");

        // Trang Chức năng 1
        JPanel slangPage = createSlangSearchPage();
        mainPanel.add(slangPage, "page1");

        // Trang Chức năng 2
        JPanel definePage = createDefineSearchPage();
        mainPanel.add(definePage, "page2");

        // Thêm 4 trang chức năng khác ở đây
        JPanel historyPage = createHistoryPage();
        mainPanel.add(historyPage, "page3");

        JPanel adminPage = createAdminPage();
        mainPanel.add(adminPage, "page4");

        JPanel addWordPage = addNewWordPage();
        mainPanel.add(addWordPage, "page4_1");

        JPanel editWordPage = editWordPage();
        mainPanel.add(editWordPage, "page4_2");

        JPanel deleteWordPage = deleteWordPage();
        mainPanel.add(deleteWordPage, "page4_3");

        JPanel resetPage = recoveryDictionary();
        mainPanel.add(resetPage, "page5");

        JPanel gamePage = createMiniGamePage();
        mainPanel.add(gamePage, "page6");

        JPanel randomWordGame = randomWordToDay();
        mainPanel.add(randomWordGame, "page6_1");

        JPanel quizWord = quizSlang();
        mainPanel.add(quizWord, "page6_2");

        JPanel quizDefine = quizDefine();
        mainPanel.add(quizDefine, "page6_3");

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private JPanel createMenuPage() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Slang Dictionary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 45));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
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
        JButton searchByDefineButton = new JButton("Search by Definition");
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
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : entry.getValue()) {
                        stringBuilder.append(item).append(", ");
                    }
                    String temp = stringBuilder.toString();

                    if (temp.endsWith(", ")) {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    listHistory.addElement(entry.getKey() + " : " + temp);

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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));
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
        //
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
                        //
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
                            doc.insertString(doc.getLength(), "  Definition:\n", null);
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
        JLabel titleLabel = new JLabel("Search by Definition", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        topPanel.add(titleLabel);
        // bodyPanel

        // ---body1
        JPanel body1 = new JPanel();
        JLabel titleInput = new JLabel("Enter Search Definition", JLabel.CENTER);
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
        JTextPane resultTextPane = new JTextPane();
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
                    resultTextPane.setText("");
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
                    resultTextPane.setText("");
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
                    resultTextPane.setText("");
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

    // thêm button reset
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
        JButton resetButton = new JButton("Reset History");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.writeFileWord("history.txt");
                cardLayout.show(mainPanel, "menu");
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (history.getDictionary().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Can't reset the empty list", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    listHistory.clear();
                    history.clearDictionary();
                    history.writeFileWord("history.txt");
                }
            }
        });
        bottomPanel.add(resetButton);
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
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // bodyPanel
        JButton addWordButton = new JButton("Add New Word");
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page4_1");
            }
        });
        JButton editWordButton = new JButton("Edit A Word");
        editWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page4_2");
            }
        });
        JButton deleteWordButton = new JButton("Delete A Word");
        deleteWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page4_3");
            }
        });
        JPanel adminPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        adminPanel.add(addWordButton);
        adminPanel.add(editWordButton);
        adminPanel.add(deleteWordButton);
        adminPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        bodyPanel.add(adminPanel, BorderLayout.CENTER);
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
        main.add(bodyPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);

        // --------------------------
        return main;
    }

    private JPanel addNewWordPage() {
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
        JLabel titleLabel = new JLabel("Add New Slang Word", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(titleLabel);
        // bodyPanel
        JPanel body1 = new JPanel();
        body1.setLayout(new BorderLayout());
        JPanel body2 = new JPanel();
        body2.setLayout(new BorderLayout());
        // body1
        JLabel titleKey = new JLabel("Enter Key Word: ", JLabel.CENTER);
        titleKey.setFont(new Font("Arial", Font.BOLD, 12));
        titleKey.setBorder(BorderFactory.createEmptyBorder(20, 10, 8, 0));
        JTextField inputKey = new JTextField();
        inputKey.setPreferredSize(new Dimension(330, 8));
        // inputKey.setBorder(BorderFactory.createEmptyBorder(16, 0, 20, 0));
        body1.add(titleKey, BorderLayout.WEST);
        body1.add(inputKey, BorderLayout.EAST);
        bodyPanel.add(body1, BorderLayout.NORTH);
        // body2
        JPanel body2_1 = new JPanel();
        body2_1.setLayout(new BorderLayout());
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel titleDef = new JLabel("Enter Definition: ", JLabel.CENTER);
        titleDef.setFont(new Font("Arial", Font.BOLD, 12));
        titleDef.setBorder(BorderFactory.createEmptyBorder(0, 10, 160, 0));
        JTextArea inputDef = new JTextArea();
        inputDef.setPreferredSize(new Dimension(330, 64));
        inputDef.setBorder(BorderFactory.createLineBorder(Color.gray));
        inputDef.setLineWrap(true);
        body2_1.add(titleDef, BorderLayout.CENTER);
        body2_2.add(inputDef, BorderLayout.NORTH);
        JLabel titleExtra = new JLabel("Note: Insert ',' between two definition");
        body2_2.add(titleExtra, BorderLayout.SOUTH);
        titleExtra.setBorder(BorderFactory.createEmptyBorder(5, 30, 120, 0));
        //
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(80, 40));
        body2.add(body2_1, BorderLayout.WEST);
        body2.add(body2_2, BorderLayout.EAST);

        bodyPanel.add(body2, BorderLayout.SOUTH);

        // bottomPanel
        JButton backButton = new JButton("Return");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page4");
            }
        });
        backButton.setPreferredSize(new Dimension(80, 40));
        bottomPanel.add(submitButton);
        bottomPanel.add(backButton);
        //
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(110, 0)));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        //

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = inputKey.getText();
                String def = inputDef.getText();
                String[] definition = def.split("\\,");
                Set<String> list = new HashSet<>(Arrays.asList(definition));
                if (mainDictionary.getDictionary().containsKey(key)) {
                    String[] options = { "Duplicate", "Overwrite" };
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "This word already exists!",
                            "Warning",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (choice == 0) {
                        key = key + " ";
                        mainDictionary.addSlangWord(key, list);
                        JOptionPane.showMessageDialog(null, "Added new words successfully", "Notification",
                                JOptionPane.INFORMATION_MESSAGE);
                        inputKey.setText("");
                        inputDef.setText("");
                        mainDictionary.writeFileWord("slangWordBackUp.txt");
                        cardLayout.show(mainPanel, "page4");
                    } else if (choice == 1) {
                        mainDictionary.addSlangWord(key, list);
                        JOptionPane.showMessageDialog(null, "Added new words successfully", "Notification",
                                JOptionPane.INFORMATION_MESSAGE);
                        inputKey.setText("");
                        inputDef.setText("");
                        mainDictionary.writeFileWord("slangWordBackUp.txt");
                        cardLayout.show(mainPanel, "page4");
                    }

                } else {
                    mainDictionary.addSlangWord(key, list);
                    JOptionPane.showMessageDialog(null, "Added new words successfully", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                    inputKey.setText("");
                    inputDef.setText("");
                    mainDictionary.writeFileWord("slangWordBackUp.txt");
                    cardLayout.show(mainPanel, "page4");
                }
            }
        });
        return main;
    }

    private JPanel editWordPage() {
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
        JLabel titleLabel = new JLabel("Edit Slang Word", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // bodyPanel
        // ---body1
        JPanel body1 = new JPanel();
        JLabel titleInput = new JLabel("Enter Edit Word", JLabel.CENTER);
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
        JLabel suggestLabel = new JLabel("Click to Edit", JLabel.CENTER);
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
        body2_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        body2.add(body2_1, BorderLayout.WEST);
        bodyPanel.add(body1, BorderLayout.NORTH);

        // --------body2-2
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel editLabel = new JLabel("Word Information", JLabel.CENTER);
        editLabel.setFont(new Font("Arial", Font.BOLD, 16));
        editLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        body2_2.add(editLabel, BorderLayout.NORTH);
        //
        JPanel body2_2_1 = new JPanel();
        body2_2_1.setLayout(new BorderLayout());
        //
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(70, 120));
        resultTextPane.setEditable(false);
        JPanel body2_2_1_a = new JPanel();
        body2_2_1_a.setLayout(new BorderLayout());
        body2_2_1_a.add(resultTextPane, BorderLayout.CENTER);
        body2_2_1_a.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));
        //
        JLabel keyLabel = new JLabel("Change Definition: ", JLabel.LEFT);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        keyLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 5));
        JTextArea inputDef = new JTextArea();
        inputDef.setPreferredSize(new Dimension(20, 3));
        inputDef.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        inputDef.setLineWrap(true);// để xuống dòng
        JPanel body2_2_1_b = new JPanel();
        body2_2_1_b.setBorder(BorderFactory.createEmptyBorder(30, 10, 80, 40));
        body2_2_1_b.setLayout(new BorderLayout());
        body2_2_1_b.add(keyLabel, BorderLayout.WEST);
        body2_2_1_b.add(inputDef, BorderLayout.CENTER);
        //
        body2_2_1.add(body2_2_1_a, BorderLayout.NORTH);
        body2_2_1.add(body2_2_1_b, BorderLayout.CENTER);
        //
        body2_2.add(body2_2_1, BorderLayout.CENTER);
        //
        body2.add(body2_2, BorderLayout.CENTER);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resultTextPane.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Can't edit the empty word!!!", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    input.setText("");
                    listSuggest.clear();
                    resultTextPane.setText("");
                } else {
                    String newDef = inputDef.getText();
                    String[] newDefStrings = newDef.split("\\,");
                    Set<String> list = new HashSet<>(Arrays.asList(newDefStrings));
                    int selectIndex = suggestList.getSelectedIndex();
                    String key = listSuggest.getElementAt(selectIndex);
                    mainDictionary.getDictionary().put(key, list);
                    mainDictionary.writeFileWord("slangWordBackUp.txt");
                    JOptionPane.showMessageDialog(null, "Update complete!", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                    input.setText("");
                    listSuggest.clear();
                    resultTextPane.setText("");
                    inputDef.setText("");
                }
            }
        });
        JButton backButton = new JButton("Return");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText("");
                listSuggest.clear();
                resultTextPane.setText("");
                cardLayout.show(mainPanel, "page4");
            }
        });
        bottomPanel.add(submitButton);
        bottomPanel.add(backButton);
        //
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        //
        suggestList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    resultTextPane.setText("");
                    int selectIndex = suggestList.getSelectedIndex();
                    if (selectIndex != -1) {
                        String key = listSuggest.getElementAt(selectIndex);
                        Set<String> define = new HashSet<>(mainDictionary.getDictionary().get(key));
                        //
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
                            doc.insertString(doc.getLength(), "  Definition:\n", null);
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
        //
        return main;
    }

    private JPanel deleteWordPage() {
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
        JLabel titleLabel = new JLabel("Delete Slang Word", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // bodyPanel
        // ---body1
        JPanel body1 = new JPanel();
        JLabel titleInput = new JLabel("Enter Slang Word", JLabel.CENTER);
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
        JLabel suggestLabel = new JLabel("Choose Word to Delete", JLabel.CENTER);
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
        body2_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        body2.add(body2_1, BorderLayout.WEST);
        bodyPanel.add(body1, BorderLayout.NORTH);

        // --------body2-2
        JPanel body2_2 = new JPanel();
        body2_2.setLayout(new BorderLayout());
        JLabel editLabel = new JLabel("Word Information", JLabel.CENTER);
        editLabel.setFont(new Font("Arial", Font.BOLD, 16));
        editLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        body2_2.add(editLabel, BorderLayout.NORTH);
        //
        JPanel body2_2_1 = new JPanel();
        body2_2_1.setLayout(new BorderLayout());
        //
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(70, 120));
        resultTextPane.setEditable(false);
        JPanel body2_2_1_a = new JPanel();
        body2_2_1_a.setLayout(new BorderLayout());
        body2_2_1_a.add(resultTextPane, BorderLayout.CENTER);
        body2_2_1_a.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));
        //
        body2_2_1.add(body2_2_1_a, BorderLayout.CENTER);

        //
        body2_2.add(body2_2_1, BorderLayout.CENTER);
        //
        body2.add(body2_2, BorderLayout.CENTER);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton submitButton = new JButton("Delete");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resultTextPane.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Can't delete the empty word!!!", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    input.setText("");
                    listSuggest.clear();
                    resultTextPane.setText("");
                } else {
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Do you want to delete this slang word?",
                            "verification",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        int selectIndex = suggestList.getSelectedIndex();
                        String key = listSuggest.getElementAt(selectIndex);
                        mainDictionary.getDictionary().remove(key);
                        mainDictionary.writeFileWord("slangWordBackUp.txt");
                        JOptionPane.showMessageDialog(null, "Delete complete!", "Notification",
                                JOptionPane.INFORMATION_MESSAGE);
                        input.setText("");
                        listSuggest.clear();
                        resultTextPane.setText("");
                    }
                }
            }
        });
        JButton backButton = new JButton("Return");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText("");
                listSuggest.clear();
                resultTextPane.setText("");
                cardLayout.show(mainPanel, "page4");
            }
        });
        bottomPanel.add(submitButton);
        bottomPanel.add(backButton);
        //
        JPanel ezBody = new JPanel();
        ezBody.setLayout(new BoxLayout(ezBody, BoxLayout.LINE_AXIS));
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        ezBody.add(bodyPanel);
        ezBody.add(Box.createRigidArea(new Dimension(10, 0)));
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(ezBody, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        //
        suggestList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    resultTextPane.setText("");
                    int selectIndex = suggestList.getSelectedIndex();
                    if (selectIndex != -1) {
                        String key = listSuggest.getElementAt(selectIndex);
                        Set<String> define = new HashSet<>(mainDictionary.getDictionary().get(key));
                        //
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
                            doc.insertString(doc.getLength(), "  Definition:\n", null);
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
        //
        return main;
    }

    private JPanel recoveryDictionary() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        //
        // topPanel
        JLabel titleLabel = new JLabel("Reset Slang Dictionary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // body Panel
        JButton resetButton = new JButton("Reset Dictionary");
        bodyPanel.add(resetButton, BorderLayout.CENTER);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(150, 150, 150, 150));
        // bottom Panel
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to reset dictionary?",
                        "verification",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    mainDictionary.readFileWord("slang.txt");
                    mainDictionary.writeFileWord("slangWordBackUp.txt");
                    JOptionPane.showMessageDialog(null, "Reset complete!", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "menu");
                }
            }
        });
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "menu");
            }
        });
        bottomPanel.add(backButton);

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

    private JPanel createMiniGamePage() {
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
        JLabel titleLabel = new JLabel("Mini Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // bodyPanel
        JButton addWordButton = new JButton("On This Day Slang Word");
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6_1");
            }
        });
        JButton editWordButton = new JButton("Slang Word Quiz( Word Mode)");
        editWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6_2");
            }
        });
        JButton deleteWordButton = new JButton("Slang Word Quiz( Definition Mode)");
        deleteWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6_3");
            }
        });
        JPanel adminPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        adminPanel.add(addWordButton);
        adminPanel.add(editWordButton);
        adminPanel.add(deleteWordButton);
        adminPanel.setBorder(BorderFactory.createEmptyBorder(100, 180, 100, 180));
        bodyPanel.add(adminPanel, BorderLayout.CENTER);
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
        main.add(bodyPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);

        // --------------------------
        return main;
    }

    private JPanel randomWordToDay() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        //
        // topPanel
        JLabel titleLabel = new JLabel("Random Word For Day", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        //
        // bodyPanel
        JTextPane resultTextPane = new JTextPane();
        resultTextPane.setPreferredSize(new Dimension(70, 120));
        resultTextPane.setEditable(false);
        bodyPanel.add(resultTextPane, BorderLayout.CENTER);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(25, 60, 40, 60));
        JButton resetButton = new JButton("Reset");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(resetButton);
        bodyPanel.add(buttonPanel, BorderLayout.SOUTH);
        //

        // bottomPanel
        JButton backButton = new JButton("Return");
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(bodyPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);

        // --------------------------
        if (randomWord.size() == 0) {
            randomWord = mainDictionary.randomWord();
        }
        String key = randomWord.firstKey();
        Set<String> temp = randomWord.get(key);
        List<String> def = new ArrayList<>(temp);
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
            doc.insertString(doc.getLength(), "  Definition:\n", null);
            for (String ele : def) {
                doc.insertString(doc.getLength(), "- ", null);
                doc.insertString(doc.getLength(), ele, null);
                doc.insertString(doc.getLength(), "\n", null);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        //
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultTextPane.setText("");
                randomWord = mainDictionary.randomWord();
                String key = randomWord.firstKey();
                Set<String> temp = randomWord.get(key);
                List<String> def = new ArrayList<>(temp);
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
                    doc.insertString(doc.getLength(), "  Definition:\n", null);
                    for (String ele : def) {
                        doc.insertString(doc.getLength(), "- ", null);
                        doc.insertString(doc.getLength(), ele, null);
                        doc.insertString(doc.getLength(), "\n", null);
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6");
            }
        });
        return main;
    }

    private JPanel quizSlang() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JTextPane resultTextPane = new JTextPane();
        //
        // Top Panel
        JLabel titleLabel = new JLabel("Slang Quiz", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // body
        JPanel body1 = new JPanel(new BorderLayout());
        JPanel body2 = new JPanel(new GridLayout(2, 2));
        //
        quizSlang = mainDictionary.quizSlang();
        String questioString = quizSlang.firstKey();
        Set<String> correctAns = new HashSet<>(mainDictionary.getDictionary().get(questioString));
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : correctAns) {
            stringBuilder.append(item).append(", ");
        }
        realAnswer = stringBuilder.toString();
        realAnswer = realAnswer.substring(0, realAnswer.length() - 2);

        List<String> answeStrings = quizSlang.get(questioString);
        JButton answerButtons1 = new JButton(answeStrings.get(0));
        JButton answerButtons2 = new JButton(answeStrings.get(1));
        JButton answerButtons3 = new JButton(answeStrings.get(2));
        JButton answerButtons4 = new JButton(answeStrings.get(3));
        body2.add(answerButtons1);
        body2.add(answerButtons2);
        body2.add(answerButtons3);
        body2.add(answerButtons4);
        //
        answerButtons1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (realAnswer.compareTo(answerButtons1.getText()) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizSlang = mainDictionary.quizSlang();
                String questioString = quizSlang.firstKey();
                Set<String> correctAns = new HashSet<>(mainDictionary.getDictionary().get(questioString));
                StringBuilder stringBuilder = new StringBuilder();
                for (String item : correctAns) {
                    stringBuilder.append(item).append(", ");
                }
                realAnswer = stringBuilder.toString();
                realAnswer = realAnswer.substring(0, realAnswer.length() - 2);
                resultTextPane.setText("What are definition of " + questioString);
                List<String> answeStrings = quizSlang.get(questioString);
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        answerButtons2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (realAnswer.compareTo(answerButtons2.getText()) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizSlang = mainDictionary.quizSlang();
                String questioString = quizSlang.firstKey();
                Set<String> correctAns = new HashSet<>(mainDictionary.getDictionary().get(questioString));
                StringBuilder stringBuilder = new StringBuilder();
                for (String item : correctAns) {
                    stringBuilder.append(item).append(", ");
                }
                realAnswer = stringBuilder.toString();
                realAnswer = realAnswer.substring(0, realAnswer.length() - 2);
                resultTextPane.setText("What are definition of " + questioString);
                List<String> answeStrings = quizSlang.get(questioString);
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        answerButtons3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (realAnswer.compareTo(answerButtons3.getText()) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizSlang = mainDictionary.quizSlang();
                String questioString = quizSlang.firstKey();
                Set<String> correctAns = new HashSet<>(mainDictionary.getDictionary().get(questioString));
                StringBuilder stringBuilder = new StringBuilder();
                for (String item : correctAns) {
                    stringBuilder.append(item).append(", ");
                }
                realAnswer = stringBuilder.toString();
                realAnswer = realAnswer.substring(0, realAnswer.length() - 2);
                resultTextPane.setText("What are definition of " + questioString);
                List<String> answeStrings = quizSlang.get(questioString);
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        answerButtons4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (realAnswer.compareTo(answerButtons4.getText()) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizSlang = mainDictionary.quizSlang();
                String questioString = quizSlang.firstKey();
                Set<String> correctAns = new HashSet<>(mainDictionary.getDictionary().get(questioString));
                StringBuilder stringBuilder = new StringBuilder();
                for (String item : correctAns) {
                    stringBuilder.append(item).append(", ");
                }
                realAnswer = stringBuilder.toString();
                realAnswer = realAnswer.substring(0, realAnswer.length() - 2);
                resultTextPane.setText("What are definition of " + questioString);
                List<String> answeStrings = quizSlang.get(questioString);
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        //

        resultTextPane.setPreferredSize(new Dimension());
        resultTextPane.setEditable(false);
        body1.add(resultTextPane, BorderLayout.CENTER);
        body1.setBorder(BorderFactory.createEmptyBorder(25, 110, 250, 110));
        //
        resultTextPane.setText("What are definition of " + questioString);
        //
        bodyPanel.add(body1, BorderLayout.CENTER);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Return");
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(bodyPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        //
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6");
            }
        });
        return main;
    }

    private JPanel quizDefine() {
        JPanel main = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JTextPane resultTextPane = new JTextPane();
        //
        // Top Panel
        JLabel titleLabel = new JLabel("Definition Quiz", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.add(titleLabel);
        // body
        JPanel body1 = new JPanel(new BorderLayout());
        JPanel body2 = new JPanel(new GridLayout(2, 2));
        //
        quizDefine = mainDictionary.quizDefine();
        String questioString = quizDefine.firstKey();

        List<String> answeStrings = quizDefine.get(questioString);
        for (String ele : answeStrings) {
            Set<String> def = mainDictionary.getDictionary().get(ele);
            StringBuilder stringBuilder = new StringBuilder();
            for (String item : def) {
                stringBuilder.append(item).append(", ");
            }
            String temp = stringBuilder.toString();

            if (temp.endsWith(", ")) {
                temp = temp.substring(0, temp.length() - 2);
            }
            if (questioString.compareTo(temp) == 0)
                realAnswer = ele;
        }
        JButton answerButtons1 = new JButton(answeStrings.get(0));
        JButton answerButtons2 = new JButton(answeStrings.get(1));
        JButton answerButtons3 = new JButton(answeStrings.get(2));
        JButton answerButtons4 = new JButton(answeStrings.get(3));
        body2.add(answerButtons1);
        body2.add(answerButtons2);
        body2.add(answerButtons3);
        body2.add(answerButtons4);
        //
        answerButtons1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (answerButtons1.getText().compareTo(realAnswer) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizDefine = mainDictionary.quizDefine();
                String questioString = quizDefine.firstKey();
                resultTextPane.setText("What is the Slang Word for '" + questioString + "'");
                List<String> answeStrings = quizDefine.get(questioString);
                for (String ele : answeStrings) {
                    Set<String> def = mainDictionary.getDictionary().get(ele);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : def) {
                        stringBuilder.append(item).append(", ");
                    }
                    String temp = stringBuilder.toString();

                    if (temp.endsWith(", ")) {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    if (questioString.compareTo(temp) == 0)
                        realAnswer = ele;
                }
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        //
        answerButtons2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (answerButtons2.getText().compareTo(realAnswer) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizDefine = mainDictionary.quizDefine();
                String questioString = quizDefine.firstKey();
                resultTextPane.setText("What is the Slang Word for '" + questioString + "'");
                List<String> answeStrings = quizDefine.get(questioString);
                for (String ele : answeStrings) {
                    Set<String> def = mainDictionary.getDictionary().get(ele);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : def) {
                        stringBuilder.append(item).append(", ");
                    }
                    String temp = stringBuilder.toString();

                    if (temp.endsWith(", ")) {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    if (questioString.compareTo(temp) == 0)
                        realAnswer = ele;
                }
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        //
        answerButtons3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (answerButtons3.getText().compareTo(realAnswer) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizDefine = mainDictionary.quizDefine();
                String questioString = quizDefine.firstKey();
                resultTextPane.setText("What is the Slang Word for '" + questioString + "'");
                List<String> answeStrings = quizDefine.get(questioString);
                for (String ele : answeStrings) {
                    Set<String> def = mainDictionary.getDictionary().get(ele);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : def) {
                        stringBuilder.append(item).append(", ");
                    }
                    String temp = stringBuilder.toString();

                    if (temp.endsWith(", ")) {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    if (questioString.compareTo(temp) == 0)
                        realAnswer = ele;
                }
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });
        //
        answerButtons4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (answerButtons4.getText().compareTo(realAnswer) == 0) {
                    JOptionPane.showMessageDialog(null, "Correct Answer", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong answer, correct answer is " + realAnswer,
                            "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                resultTextPane.setText("");
                answerButtons1.setText("");
                answerButtons2.setText("");
                answerButtons3.setText("");
                answerButtons4.setText("");
                quizDefine = mainDictionary.quizDefine();
                String questioString = quizDefine.firstKey();
                resultTextPane.setText("What is the Slang Word for '" + questioString + "'");
                List<String> answeStrings = quizDefine.get(questioString);
                for (String ele : answeStrings) {
                    Set<String> def = mainDictionary.getDictionary().get(ele);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : def) {
                        stringBuilder.append(item).append(", ");
                    }
                    String temp = stringBuilder.toString();

                    if (temp.endsWith(", ")) {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    if (questioString.compareTo(temp) == 0)
                        realAnswer = ele;
                }
                answerButtons1.setText(answeStrings.get(0));
                answerButtons2.setText(answeStrings.get(1));
                answerButtons3.setText(answeStrings.get(2));
                answerButtons4.setText(answeStrings.get(3));
            }
        });

        resultTextPane.setPreferredSize(new Dimension());
        resultTextPane.setEditable(false);
        body1.add(resultTextPane, BorderLayout.CENTER);
        body1.setBorder(BorderFactory.createEmptyBorder(25, 110, 250, 110));
        //
        resultTextPane.setText("What is the Slang Word for '" + questioString + "'");
        //
        bodyPanel.add(body1, BorderLayout.CENTER);
        bodyPanel.add(body2, BorderLayout.SOUTH);
        // bottomPanel
        JButton backButton = new JButton("Return");
        bottomPanel.add(backButton);
        //
        main.add(topPanel, BorderLayout.PAGE_START);
        main.add(bodyPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.PAGE_END);
        //
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page6");
            }
        });
        return main;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlangDictionary());
    }
}
