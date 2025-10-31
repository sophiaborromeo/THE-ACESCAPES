import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class Leaderboard extends JPanel {
    private Font mestizoFont, pressStartFont;
    private JTextPane textPane; 
    private JPanel box;
    private JScrollPane scroll;
    private Image backgroundImage;

    private static final Color INNER_GRAY = new Color(220, 220, 220);
    private static final Color MAROON = new Color(128, 0, 0);

    private CardLayout cardLayout;
    private JPanel mainPanel;
        
    public Leaderboard(CardLayout cardLayout, JPanel mainPanel) {
    	this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        backgroundImage = new ImageIcon("images/LeaderboardBg.png").getImage();

        try {
            mestizoFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MestizoFont.ttf")).deriveFont(64f);
            pressStartFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-Regular.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mestizoFont);
            ge.registerFont(pressStartFont);
        } catch (Exception e) {
            System.out.println("⚠️ Font loading failed. Using defaults.");
            mestizoFont = new Font("Serif", Font.BOLD, 64);
            pressStartFont = new Font("Monospaced", Font.PLAIN, 14);
        }

        setLayout(null);

        // box design
        box = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(INNER_GRAY);
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 20, 20);
                g2.dispose();
            }
        };
        box.setOpaque(false);
        add(box);

        textPane = new JTextPane();
        textPane.setFont(pressStartFont);
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaretColor(new Color(0, 0, 0, 0)); 
        textPane.setMargin(new Insets(30, 50, 30, 50));

        // scroll layout
        scroll = new JScrollPane(textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBounds(20, 20, 1012, 379);
        box.add(scroll);

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
//        close.addActionListener(e -> System.exit(0));
//        add(close);

        loadLeaderboard();
        
//        back
        JButton btnBack = new JButton("<");
        btnBack.setFont(pressStartFont);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(Color.GRAY);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        add(btnBack);
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "front"));


        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                int boxW = (int) (width * 0.8);
                int boxH = (int) (height * 0.6);
                int boxX = (width - boxW) / 2;
                int boxY = (int) (height * 0.29);

                box.setBounds(boxX, boxY, boxW, boxH);
                scroll.setBounds(40, 40, boxW - 80, boxH - 80);
//                close.setBounds(width - 90, 30, 60, 60);
                btnBack.setBounds(30, 30, 60, 60);
                SwingUtilities.invokeLater(() -> textPane.setCaretPosition(0));
            }
        });
    }

    private void loadLeaderboard() {
        List<String[]> entries = readLeaderboardFile();

        // sorting method
        entries.sort((a, b) -> {
            try {
                int scoreA = Integer.parseInt(a[1]);
                int scoreB = Integer.parseInt(b[1]);
                if (scoreA != scoreB) return Integer.compare(scoreB, scoreA);
                int timeA = parseTimeToSeconds(a[2]);
                int timeB = parseTimeToSeconds(b[2]);
                return Integer.compare(timeA, timeB);
            } catch (Exception e) {
                return 0;
            }
        });

        StyledDocument doc = textPane.getStyledDocument();

        // header style purposes
        SimpleAttributeSet headerStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(headerStyle, Color.BLACK);
        StyleConstants.setBold(headerStyle, true);

        // entry style purposes
        SimpleAttributeSet entryStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(entryStyle, MAROON);

        try {
            doc.insertString(doc.getLength(),
                    String.format("%-1s  %-11s %-10s %-10s%n%n", "", "NAME", "SCORE", "TIME"),
                    headerStyle);

            if (entries.isEmpty()) {
                doc.insertString(doc.getLength(), "⚠️ No entries found.\n", entryStyle);
            } else {
                for (int i = 0; i < entries.size(); i++) {
                    String[] e = entries.get(i);

                    // for top 3
                    if (i < 3) {
                        String medalFile = "images/ace" + (i + 1) + ".png";
                        ImageIcon icon = new ImageIcon(medalFile);
                        Image img = icon.getImage().getScaledInstance(45, 42, Image.SCALE_SMOOTH);
                        SimpleAttributeSet iconStyle = new SimpleAttributeSet();
                        StyleConstants.setIcon(iconStyle, new ImageIcon(img));
                        doc.insertString(doc.getLength(), " ", iconStyle); 
                        doc.insertString(doc.getLength(), " ", entryStyle); 
                    } else {
                        doc.insertString(doc.getLength(), "   ", entryStyle); // space to align with top 3
                    }

                    // insert entries
                    doc.insertString(doc.getLength(),
                            String.format("%-11s %-10s %-10s%n%n%n",
                                    e[0].toUpperCase(), e[1], e[2]),
                            entryStyle);
                }
            }

            textPane.setCaretPosition(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private int parseTimeToSeconds(String time) {
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            return hours * 3600 + minutes * 60 + seconds;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private List<String[]> readLeaderboardFile() {
        List<String[]> list = new ArrayList<>();
        Path filePath = Paths.get(System.getProperty("user.dir"), "src", "Leaderboard.txt");
        if (!Files.exists(filePath)) return list;

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 3) {
                    for (int i = 0; i < 3; i++) parts[i] = parts[i].trim();
                    list.add(new String[]{parts[0], parts[1], parts[2]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Leaderboard");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setResizable(true);
//        frame.add(new Leaderboard(CardLayout cardLayout, JPanel mainPanel));
//        frame.setVisible(true);
//    }
}
