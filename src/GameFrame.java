import javax.swing.*;

public class GameFrame extends JFrame {

    /**
     * My specific game frame
     */
    public GameFrame() {

        add(new GamePanel());
        setTitle("Snake Game by Yakodan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}