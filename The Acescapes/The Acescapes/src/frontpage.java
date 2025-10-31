import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class frontpage extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Font pixelFont;

    public frontpage() {
        // ---------------- Load pixel font from external fonts folder ----------------
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/PressStart2P-Regular.ttf")).deriveFont(28f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            System.out.println("Font loading failed. Using default.");
            pixelFont = new Font("Arial", Font.BOLD, 28);
        }

        // ---------------- Frame setup ----------------
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // ---------------- Add screens ----------------
        JPanel frontPanel = createFrontPanel();
        entername enterNamePanel = new entername(pixelFont, cardLayout, mainPanel);
        HowToPlay howToPlayPanel = new HowToPlay(mainPanel, cardLayout);
        Leaderboard LeaderboardPanel = new Leaderboard(cardLayout, mainPanel); 
        AboutUs aboutUsPanel = new AboutUs(cardLayout, mainPanel); 

        mainPanel.add(frontPanel, "front");
        mainPanel.add(enterNamePanel, "entername");
        mainPanel.add(howToPlayPanel, "HowToPlay");
        mainPanel.add(LeaderboardPanel, "Leaderboard");
        mainPanel.add(aboutUsPanel, "AboutUs");  
        
        setVisible(true);
    }

    private JPanel createFrontPanel() {
        JPanel background = new JPanel(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // ---------------- Background ----------------
        Image backgroundImage = new ImageIcon("images/backgroundimage.png").getImage();
        JLabel bgLabel = new JLabel(new ImageIcon(backgroundImage.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH)));
        bgLabel.setBounds(0, 0, screenWidth, screenHeight);
        bgLabel.setLayout(null);
        background.add(bgLabel);

        // ---------------- Close Button ----------------
        JButton btnClose = new JButton("X");
        btnClose.setFont(pixelFont.deriveFont(18f));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(Color.RED);
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        int btnSize = 60;
        btnClose.setBounds(screenWidth - btnSize - 20, 20, btnSize, btnSize);
        btnClose.addActionListener(e -> System.exit(0));
        btnClose.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnClose.setBackground(Color.DARK_GRAY); }
            public void mouseExited(MouseEvent e) { btnClose.setBackground(Color.RED); }
        });
        bgLabel.add(btnClose);

        // ---------------- Buttons ----------------
        String[] labels = {"PLAY", "HOW TO PLAY", "LEADERBOARD", "ABOUT US"};
        int buttonWidth = 400, buttonHeight = 70;

        Image btnImage = new ImageIcon("images/btns.png").getImage();
        Image btnHoverImage = new ImageIcon("images/btnshov.png").getImage();
        ImageIcon btnIcon = new ImageIcon(btnImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
        ImageIcon btnHoverIcon = new ImageIcon(btnHoverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));

        int startY = screenHeight / 2 + 50;
        int gap = buttonHeight + 20;

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i], btnIcon);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            btn.setFont(pixelFont);
            btn.setForeground(Color.decode("#8B0000"));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);

            int x = screenWidth / 2 - buttonWidth / 2;
            int y = startY + i * gap;
            btn.setBounds(x, y, buttonWidth, buttonHeight);

            int index = i;
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setIcon(btnHoverIcon); btn.setForeground(Color.WHITE); }
                public void mouseExited(MouseEvent e) { btn.setIcon(btnIcon); btn.setForeground(Color.decode("#8B0000")); }
            });

            btn.addActionListener(e -> {
                switch (index) {
                    case 0 -> cardLayout.show(mainPanel, "entername"); 
                    case 1 -> cardLayout.show(mainPanel, "HowToPlay");
                    case 2 -> cardLayout.show(mainPanel, "Leaderboard");
                    case 3 -> cardLayout.show(mainPanel, "AboutUs");
                }
            });

            bgLabel.add(btn);
        }

        return background;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(frontpage::new);
    }
}
