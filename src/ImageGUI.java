import javax.swing.*;
import java.awt.*;

class ImageGUI extends JFrame {
    private JPanel mainPanel;

    ImageGUI() {
        super();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);
        setSize(new Dimension(800,800));
    }

    void displayImage(Image img, String imageName) {

        var imgIcon = new ImageIcon(img);
        var mLabel = new JLabel();
        mLabel.setIcon(imgIcon);
        mainPanel.removeAll();
        mainPanel.add(mLabel, BorderLayout.CENTER);
        mLabel.setVerticalTextPosition(SwingConstants.CENTER);
        mLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        mLabel.setText("Processing " + imageName);
        mLabel.setVisible(true);
        this.repaint();
    }
}