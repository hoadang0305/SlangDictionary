import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SlangDictionary {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SlangDictionary() {
        frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Trang Menu
        JPanel menuPage = createMenuPage();
        mainPanel.add(menuPage, "menu");

        // Trang Chức năng 1
        JPanel page1 = createFunctionPage("Chức năng 1", "Page 1");
        mainPanel.add(page1, "page1");

        // Trang Chức năng 2
        JPanel page2 = createFunctionPage("Chức năng 2", "Page 2");
        mainPanel.add(page2, "page2");

        // Thêm 4 trang chức năng khác ở đây
        JPanel page3 = createFunctionPage("Chức năng 3", "Page 3");
        mainPanel.add(page3, "page3");

        JPanel page4 = createFunctionPage("Chức năng 4", "Page 4");
        mainPanel.add(page4, "page4");

        JPanel page5 = createFunctionPage("Chức năng 5", "Page 5");
        mainPanel.add(page5, "page5");

        JPanel page6 = createFunctionPage("Chức năng 6", "Page 6");
        mainPanel.add(page6, "page6");

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private JPanel createMenuPage() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Slang Dictionary", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        menuPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        //
        JButton searchBySlangButton = new JButton("Tìm kiếm bằng SlangWord");
        searchBySlangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page1");
            }
        });
        //
        JButton searchByDefineButton = new JButton("Tìm kiếm bằng định nghĩa");
        searchByDefineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "page2");
            }
        });
        //
        JButton historyButton = new JButton("Lịch sử");
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
        JButton resetButton = new JButton("Khôi phục từ điển");
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
        buttonPanel.add(searchBySlangButton);
        buttonPanel.add(searchByDefineButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(miniGameButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    private JPanel createFunctionPage(String functionTitle, String pageTitle) {
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
