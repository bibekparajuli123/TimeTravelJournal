import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TimeTravelJournal extends JFrame {
    private JTextArea journalTextArea;
    private JButton saveButton, viewEntriesButton;
    private JLabel titleLabel;

    public TimeTravelJournal() {
        setTitle("⏳ Time Travel Journal");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);  // Required for fade-in effect on Windows
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window

        // Set Background Color
        getContentPane().setBackground(new Color(30, 30, 30));

        // Title Label
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        add(titleLabel, BorderLayout.NORTH);

        // Start Typing Effect
        startTypingEffect();

        // Text Area
        journalTextArea = new JTextArea();
        journalTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        journalTextArea.setBackground(new Color(50, 50, 50));
        journalTextArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(journalTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));

        saveButton = new JButton("💾 Save Entry");
        viewEntriesButton = new JButton("📜 View Past Entries");

        // Button Styling
        styleButton(saveButton);
        styleButton(viewEntriesButton);

        buttonPanel.add(saveButton);
        buttonPanel.add(viewEntriesButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        saveButton.addActionListener(e -> saveEntry());
        viewEntriesButton.addActionListener(e -> viewEntries());

        // Show Frame
        setVisible(true);
        fadeInEffect();
    }

    private void startTypingEffect() {
        new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            for (char c : "📖 Time Travel Journal".toCharArray()) {
                sb.append(c);
                titleLabel.setText(sb.toString());
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void fadeInEffect() {
        try {
            for (float i = 0.0f; i <= 1.0f; i += 0.05f) {
                setOpacity(i);
                Thread.sleep(50);
            }
        } catch (InterruptedException ignored) {}
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        // Add Mouse Hover Glow Effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 180, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void saveEntry() {
        String text = journalTextArea.getText().trim();
        if (!text.isEmpty()) {
            try (FileWriter writer = new FileWriter("journal.txt", true)) {
                writer.write(text + "\n---\n");
                JOptionPane.showMessageDialog(this, "Entry Saved!");
                journalTextArea.setText(""); // Clear text area after saving
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Write something before saving!");
        }
    }

    private void viewEntries() {
        try (BufferedReader reader = new BufferedReader(new FileReader("journal.txt"))) {
            StringBuilder content = new StringBuilder();
            String line;
            int positiveCount = 0, negativeCount = 0;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");

                // Basic Mood Analysis
                if (line.contains("happy") || line.contains("great") || line.contains("excited")) {
                    positiveCount++;
                } else if (line.contains("sad") || line.contains("tired") || line.contains("frustrated")) {
                    negativeCount++;
                }
            }

            // Generate Prediction
            String prediction;
            if (positiveCount > negativeCount) {
                prediction = "🌟 Your future looks bright! Keep up the positivity!";
            } else if (negativeCount > positiveCount) {
                prediction = "🌧️ Tough times ahead, but stay strong and take care of yourself!";
            } else {
                prediction = "🔮 Your future is balanced. Make mindful choices!";
            }

            // Show Entries and Prediction
            JOptionPane.showMessageDialog(this, content.toString() + "\n\n🔮 Future Prediction: " + prediction,
                    "Past Entries & Prediction", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No past entries found.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimeTravelJournal::new);
    }
}
