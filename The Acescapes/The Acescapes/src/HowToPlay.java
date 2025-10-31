import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class HowToPlay extends JPanel {
    private Image backgroundImage;
    private Font mestizoFont, pressStartFont;
    private JTextArea text;
    private JButton leftArrow, rightArrow, btnBack;
    private int currentPage = 0;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final String[] pages = {
        """
        START THE GAME
        • Begin by selecting your preferred map — each map is filled with 
          different cards.
        • Each card represents a challenge based on its suit: Spades, Diamonds, 
          Hearts, or Clubs.
        • Each player begins with 100% Health Points.

        MOVE AROUND THE MAZE
        • Use your keyboard keys (arrow keys or W, A, S, D) to move your
          character.
        • Navigate carefully to find challenge cards, avoid traps, and search 
          for the Exit Card (Ace).
        
        ANSWER THE CHALLENGES
        • When you land on a card, answer the math-related question based on  
          the card’s category:
          • Spades – Physical/Applied Math challenges
          • Diamonds – Probability and teamwork-based questions
          • Hearts – Operations such as addition, subtraction, multiplication, 
            and division
          • Clubs – Logical or abstract reasoning
        """,
        """
        USE POWER CARDS WISELY
        • Collect these Power Cards to gain strategic advantages such as hints, 
          extra time, or luck twists:
          • Jack for Time Freeze
          • Queen for Second Chance
          • King for Flashlight
          • Joker
    
        MANAGE HEALTH AND TIME
        • Every incorrect answer reduces your health. Be strategic — when 
          health hits zero, the game ends.
        • Each question has a timer. Running out of time counts as a failed 
          attempt.

        FIND THE EXIT CARD (ACE)
        • The final goal is to locate and correctly answer the Ace Card, which 
          unlocks the Exit Door and lets you escape Border Land with bonus 
          points!
        """   
    };

    public HowToPlay(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        backgroundImage = new ImageIcon("images/howtoplay_bg.png").getImage();

        try {
            mestizoFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MestizoFont.ttf")).deriveFont(80f);
            pressStartFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-Regular.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mestizoFont);
            ge.registerFont(pressStartFont);
        } catch (Exception e) {
            System.out.println("Font loading failed. Using defaults.");
            mestizoFont = new Font("Serif", Font.BOLD, 80);
            pressStartFont = new Font("Monospaced", Font.PLAIN, 15);
        }

        setLayout(null);

        // ---------- TEXT AREA ----------
        text = new JTextArea(pages[currentPage]);
        text.setFont(pressStartFont);
        text.setForeground(Color.BLACK);
        text.setOpaque(false);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBounds(140, 250, 1080, 400);
        add(text);

        // ---------- BACK BUTTON (copied style from entername) ----------
        btnBack = new JButton("<");
        btnBack.setFont(pressStartFont.deriveFont(14f));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(Color.GRAY);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setBounds(30, 30, 60, 60);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setBorder(BorderFactory.createEmptyBorder());
        btnBack.setOpaque(true);
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(Color.DARK_GRAY); }
            public void mouseExited(MouseEvent e) { btnBack.setBackground(Color.GRAY); }
        });
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "front"));
        add(btnBack);

        // ---------- ARROW BUTTONS ----------
        leftArrow = new JButton("<");
        rightArrow = new JButton(">");

        setupArrowButton(leftArrow);
        setupArrowButton(rightArrow);

        int rightSideX = 1170;  // right arrow position
        int gap = 10;           // small gap between them
        leftArrow.setBounds(rightSideX - 80 - gap, 610, 70, 45);
        rightArrow.setBounds(rightSideX, 610, 70, 45);
        add(leftArrow);
        add(rightArrow);

        leftArrow.addActionListener(e -> showPage(-1));
        rightArrow.addActionListener(e -> showPage(1));
    }

    private void setupArrowButton(JButton button) {
        button.setFont(new Font("pressStartFont", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(180, 0, 0));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            Color original = button.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 0, 0));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });
    }

    private void showPage(int direction) {
        currentPage = (currentPage + direction + pages.length) % pages.length;
        text.setText(pages[currentPage]);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(mestizoFont);
        g2.setColor(new Color(255, 215, 0));
    }
}
