import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class entername extends JPanel {

    private JTextField txtName;
    private boolean isCapsOn = false;
    private final Map<String, JButton> keyButtons = new HashMap<>();

    public entername(Font pixelFont, CardLayout cardLayout, JPanel mainPanel) {

        // -------- Layout setup --------
        setLayout(null);
        setBackground(Color.BLACK);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // -------- Background image --------
        Image backgroundImage = new ImageIcon("images/entername.png").getImage();
        JLabel background = new JLabel(new ImageIcon(backgroundImage.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, screenWidth, screenHeight);
        background.setLayout(null);
        add(background);

        // -------- Text field --------
        txtName = new JTextField();
        txtName.setFont(pixelFont.deriveFont(22f));
        txtName.setHorizontalAlignment(JTextField.CENTER);
        txtName.setBounds(screenWidth / 2 - 200, screenHeight / 2 - 280, 600, 60);
        txtName.setOpaque(false);
        txtName.setForeground(Color.BLACK);
        txtName.setCaretColor(Color.RED);
        txtName.setBorder(null);
        background.add(txtName);

        // -------- Keyboard container --------
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        keyboardPanel.setOpaque(false);
        keyboardPanel.setBounds(screenWidth / 2 - 600, screenHeight / 2 - 95, 1200, 400);
        background.add(keyboardPanel);

        // Add all rows
        keyboardPanel.add(createRow(pixelFont, new String[]{"1","2","3","4","5","6","7","8","9","0","BACK"}));
        keyboardPanel.add(createRow(pixelFont, new String[]{"A","B","C","D","E","F","G","H","I","J","CAPS"}));
        keyboardPanel.add(createRow(pixelFont, new String[]{"K","L","M","N","O","P","Q","R","S","T","CLEAR"}));
        keyboardPanel.add(createRow(pixelFont, new String[]{"U","V","W","X","Y","Z","SPACE"}));
        keyboardPanel.add(createRow(pixelFont, new String[]{"OK"}));

        // -------- Back button --------
        JButton btnBack = new JButton("<");
        btnBack.setFont(pixelFont.deriveFont(20f));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(Color.GRAY);
        btnBack.setFocusPainted(false);
        btnBack.setBounds(30, 30, 60, 60);
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(Color.DARK_GRAY); }
            public void mouseExited(MouseEvent e) { btnBack.setBackground(Color.GRAY); }
        });
        btnBack.addActionListener(e -> {
            txtName.setText(""); // clear field when going back
            cardLayout.show(mainPanel, "front");
        });
        background.add(btnBack);

        // -------- Physical keyboard handling --------
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                    isCapsOn = !isCapsOn;
                    JButton capsBtn = keyButtons.get("CAPS");
                    if (capsBtn != null) {
                        capsBtn.setBackground(isCapsOn ? Color.GRAY : Color.LIGHT_GRAY);
                        capsBtn.setText(isCapsOn ? "CAPS↑" : "CAPS");
                    }
                    e.consume();
                    return;
                }

                String raw = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
                String mapped = switch (raw) {
                    case "SPACE" -> "SPACE";
                    case "BACK SPACE", "BACKSPACE" -> "BACK";
                    case "DELETE" -> "CLEAR";
                    case "ENTER" -> "OK";
                    default -> raw;
                };

                final String pressedKey = mapped;
                JButton btn = keyButtons.get(pressedKey);
                if (btn != null) {
                    Color oldColor = btn.getBackground();
                    btn.setBackground(Color.GRAY);

                    handleKeyPress(pressedKey, isCapsOn);

                    Timer timer = new Timer(150, ev -> {
                        if (pressedKey.equals("CAPS")) {
                            btn.setBackground(isCapsOn ? Color.GRAY : Color.LIGHT_GRAY);
                        } else {
                            btn.setBackground(oldColor);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) { e.consume(); }
        });
    }

    // -------- Create one row of buttons --------
    private JPanel createRow(Font font, String[] keys) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        row.setOpaque(false);

        for (String key : keys) {
            JButton btn = new JButton(key);
            float fontSize = (key.equals("CAPS") || key.equals("SPACE") || key.equals("CLEAR") || key.equals("BACK")) ? 10f : 16f;
            btn.setFont(font.deriveFont(fontSize));
            btn.setFocusPainted(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(90, 50));

            if (key.equals("CAPS")) {
                btn.addActionListener(e -> {
                    isCapsOn = !isCapsOn;
                    btn.setBackground(isCapsOn ? Color.GRAY : Color.LIGHT_GRAY);
                    btn.setText(isCapsOn ? "CAPS↑" : "CAPS");
                });
            } else {
                btn.addActionListener(e -> handleKeyPress(key, isCapsOn));
            }

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(Color.GRAY); }
                public void mouseExited(MouseEvent e) {
                    if (key.equals("CAPS"))
                        btn.setBackground(isCapsOn ? Color.GRAY : Color.LIGHT_GRAY);
                    else
                        btn.setBackground(Color.LIGHT_GRAY);
                }
            });

            keyButtons.put(key, btn);
            row.add(btn);
        }

        return row;
    }

    // -------- Handle key press --------
    private void handleKeyPress(String key, boolean isCapsOn) {
        switch (key) {
            case "SPACE" -> txtName.setText(txtName.getText() + " ");
            case "BACK" -> {
                String current = txtName.getText();
                if (!current.isEmpty())
                    txtName.setText(current.substring(0, current.length() - 1));
            }
            case "CLEAR" -> txtName.setText("");
            case "OK" -> {
                String name = txtName.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your name!");
                } else {
                    JOptionPane.showMessageDialog(this, "Welcome, " + name + "!");
                }
            }
            default -> {
                if (key.length() == 1) {
                    txtName.setText(txtName.getText() + (isCapsOn ? key.toUpperCase() : key.toLowerCase()));
                }
            }
        }
    }
}
