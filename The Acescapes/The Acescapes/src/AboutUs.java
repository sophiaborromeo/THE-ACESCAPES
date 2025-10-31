import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AboutUs extends JPanel {
    private Image backgroundImage;
    private Image aceA, aceC, aceE;
    private Font mestizoFont, pressStartFont;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AboutUs(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        backgroundImage = new ImageIcon("images/AboutUsBg.png").getImage();
        aceA = new ImageIcon("images/AboutUsA.png").getImage();
        aceC = new ImageIcon("images/AboutUsC.png").getImage();
        aceE = new ImageIcon("images/AboutUsE.png").getImage();

        // fonts from ttf files (nasa fonts folder)
        try {
            mestizoFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MestizoFont.ttf")).deriveFont(80f);
            pressStartFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-Regular.ttf")).deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mestizoFont);
            ge.registerFont(pressStartFont);
        } catch (Exception e) {
            System.out.println("Font loading failed. Using defaults.");
            mestizoFont = new Font("Serif", Font.BOLD, 80);
            pressStartFont = new Font("Monospaced", Font.PLAIN, 14);
        }

        setLayout(null);

        JLabel ace1 = new JLabel(new ImageIcon(aceA));
        JLabel ace2 = new JLabel(new ImageIcon(aceC));
        JLabel ace3 = new JLabel(new ImageIcon(aceE));

        add(ace1);
        add(ace2);
        add(ace3);

        JPanel descriptionBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(220, 220, 220));
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);
            }
        };
        descriptionBox.setLayout(new BorderLayout());
        descriptionBox.setOpaque(false);
        add(descriptionBox);

        JTextArea text = new JTextArea(
            "We are Arvie Sinocruz, Consuelo Mercado, and Elisha Sophia Borromeo — third-year BS Applied Mathematics " +
            "students with an Information Technology track.\n\n" +
            "This project, “Ace in Borderland,” was developed as part of our Introduction to Java Programming course. " +
            "Combining our passion for mathematics, programming, and creative design, we created a game that transforms " +
            "logical and mathematical challenges into an exciting survival experience.\n\n" +
            "Inspired by Alice in Borderland, our version adds an educational twist — where every question and decision " +
            "tests both intellect and strategy. Through this project, we aim to show how programming and mathematics can " +
            "come together to build immersive learning experiences that are both fun and intellectually rewarding."
        );
        text.setFont(pressStartFont);
        text.setForeground(Color.BLACK);
        text.setOpaque(false);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setMargin(new Insets(20, 40, 20, 40));

        JScrollPane scroll = new JScrollPane(text,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        descriptionBox.add(scroll, BorderLayout.CENTER);

        // close
//        JButton close = new JButton("X") {
//            @Override
//            protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(Color.RED);
//                g2.fillOval(0, 0, getWidth(), getHeight());
//                g2.setColor(Color.WHITE);
//                g2.setFont(new Font("Arial", Font.BOLD, 24));
//                FontMetrics fm = g2.getFontMetrics();
//                int x = (getWidth() - fm.stringWidth("X")) / 2;
//                int y = (getHeight() + fm.getAscent()) / 2 - 3;
//                g2.drawString("X", x, y);
//                g2.dispose();
//            }
//        };
//        close.setOpaque(false);
//        close.setContentAreaFilled(false);
//        close.setFocusPainted(false);
//        close.setBorderPainted(false);
//        add(close);
//        close.addActionListener(e -> System.exit(0));

        // back
        JButton btnBack = new JButton("<");
        btnBack.setFont(pressStartFont);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(Color.GRAY);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        add(btnBack);
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "front"));

        // resizing (dynamic)
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // cards
                int cardW = width / 8;
                int cardH = (int) (cardW * 1.4);
                int spacing = cardW / 3;
                int totalWidth = (cardW * 3) + (spacing * 2);
                int startX = (width - totalWidth) / 2;
                int yCards = height / 4;

                ace1.setBounds(startX, yCards, cardW, cardH);
                ace2.setBounds(startX + cardW + spacing, yCards, cardW, cardH);
                ace3.setBounds(startX + (cardW + spacing) * 2, yCards, cardW, cardH);

                ace1.setIcon(new ImageIcon(aceA.getScaledInstance(cardW, cardH, Image.SCALE_SMOOTH)));
                ace2.setIcon(new ImageIcon(aceC.getScaledInstance(cardW, cardH, Image.SCALE_SMOOTH)));
                ace3.setIcon(new ImageIcon(aceE.getScaledInstance(cardW, cardH, Image.SCALE_SMOOTH)));

                // descrip
                int descW = (int) (width * 0.8);
                int descH = (int) (height * 0.32);
                int descX = (width - descW) / 2;
                int descY = (int) (height * 0.63);
                descriptionBox.setBounds(descX, descY, descW, descH);

                // close
//                close.setBounds(width - 90, 30, 60, 60);

                // back button
                btnBack.setBounds(30, 30, 60, 60);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
