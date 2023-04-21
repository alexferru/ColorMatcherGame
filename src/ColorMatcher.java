import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ColorMatcher {

    private JFrame frame;
    private JPanel colorPanel;
    private JPanel targetPanel;
    private JPanel currentPanel;
    private Color targetColor;
    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;
    private Timer timer;
    private int timeLeft;
    private JLabel timerLabel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                ColorMatcher window = new ColorMatcher();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ColorMatcher() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Color Matcher");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));

        JLabel titleLabel = new JLabel("Color Matcher");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        timerLabel = new JLabel("01:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(timerLabel, BorderLayout.EAST);

        colorPanel = new JPanel();
        frame.getContentPane().add(colorPanel, BorderLayout.CENTER);
        colorPanel.setLayout(new GridLayout(2, 1, 0, 0));

        targetPanel = new JPanel();
        colorPanel.add(targetPanel);
        targetPanel.setLayout(new BorderLayout());
        JLabel targetLabel = new JLabel("Target Color", SwingConstants.CENTER);
        targetPanel.add(targetLabel, BorderLayout.NORTH);

        currentPanel = new JPanel();
        colorPanel.add(currentPanel);
        currentPanel.setLayout(new BorderLayout());
        JLabel currentLabel = new JLabel("Current Color", SwingConstants.CENTER);
        currentPanel.add(currentLabel, BorderLayout.NORTH);

        JPanel controlsPanel = new JPanel();
        frame.getContentPane().add(controlsPanel, BorderLayout.SOUTH);
        controlsPanel.setLayout(new GridLayout(3, 2, 0, 0));

        redSlider = createColorSlider(controlsPanel, "Red");
        greenSlider = createColorSlider(controlsPanel, "Green");
        blueSlider = createColorSlider(controlsPanel, "Blue");

        generateRandomTargetColor();
        updateCurrentColor();

        timeLeft = 60;
        timer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerLabel();
            if (timeLeft == 0) {
                timer.stop();
                double score = calculateScore();
                JOptionPane.showMessageDialog(frame, "Time's up! Your score: " + String.format("%.2f", score) + "%");
            }
        });
        timer.start();
    }

    private JSlider createColorSlider(JPanel parent, String label) {
        JLabel sliderLabel = new JLabel(label);
        parent.add(sliderLabel);

        JSlider slider = new JSlider();
        slider.setMaximum(255);
        parent.add(slider);
        slider.addChangeListener(e -> updateCurrentColor());

        return slider;
    }

    private void generateRandomTargetColor() {
        Random random = new Random();
        targetColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        targetPanel.setBackground(targetColor);
    }

    private void updateCurrentColor() {
        Color currentColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
        currentPanel.setBackground(currentColor);
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private double calculateScore() {
        Color currentColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
        double maxDifference = 255.0 * 3;
        double actualDifference = Math.abs(currentColor.getRed() - targetColor.getRed()) +
                Math.abs(currentColor.getGreen() - targetColor.getGreen()) +
                Math.abs(currentColor.getBlue() - targetColor.getBlue());
        double score = 100.0 - ((actualDifference / maxDifference) * 100);
        return score;
    }
}
