import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Main game panel with basic logic of the game
 */
public class GamePanel extends JPanel implements ActionListener {

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    Direction direction = Direction.RIGHT;
    boolean running = false;
    Timer timer;
    Random random;

    /**
     * Main constructor of the game panel
     */
    public GamePanel() {
        random = new Random();
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(new Color(51, 51, 51));
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    /**
     * Methods which add the first apple and starts the timer
     */
    public void startGame() {
        addApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Main paint component method
     * @param graphics the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    /**
     * Main draw method which draws snake, apples ant score text
     * @param graphics
     */
    public void draw(Graphics graphics) {

        if(running) {
//            graphics.setColor(new Color(102, 102, 102));
//
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            // Draw an apple
            graphics.setColor(new Color(204, 0, 51));
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw a snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(new Color(60, 180, 2));
                    graphics.fillRect(x[i]-5, y[i]-5, UNIT_SIZE+10, UNIT_SIZE+10);
                } else {
                    graphics.setColor(new Color(51, 204, 0));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw a score line
            graphics.setColor(new Color(0xBA86E747, true));
            graphics.setFont(new Font("Calibri", Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH  - metrics.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());
        }
        else{
            gameOver(graphics);
        }
    }

    /**
     * Adding apple in random spot on the screen except one line from borders
     */
    public void addApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE - 2) * UNIT_SIZE + UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE - 2) * UNIT_SIZE + UNIT_SIZE;
    }

    /**
     * Move our snake
     */
    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case UP -> y[0] = y[0] - UNIT_SIZE;
            case RIGHT -> x[0] = x[0] + UNIT_SIZE;
            case DOWN -> y[0] = y[0] + UNIT_SIZE;
            case LEFT -> x[0] = x[0] - UNIT_SIZE;
        }

    }

    /**
     * Check if head coordinates match with current apple coordinates
     */
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            addApple();
        }
    }

    /**
     * Check if head coordinates match with any collisions
     */
    public void checkCollisions() {

        // Check if snake collides with itself
        for(int i = bodyParts; i>0;i--){
            if((x[0]==x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        // Check if snake collides with borders
        if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    /**
     * Shows Game Over screen
     * @param graphics
     */
    public void gameOver(Graphics graphics) {
        // Game Over text
        graphics.setColor(new Color(0xD7E04530, true));
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsGameOver = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH  - metricsGameOver.stringWidth("GameOver"))/2, (SCREEN_HEIGHT)/2);

        // Score text
        graphics.setColor(new Color(0xD7BD311E, true));
        graphics.setFont(new Font("Ink Free", Font.BOLD, 45));
        FontMetrics metricsScore = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH  - metricsScore.stringWidth("Score: " + applesEaten))/2, (SCREEN_HEIGHT)/2 + graphics.getFont().getSize());
    }

    /**
     * Makes our game "running" and shows what is going on in our game
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    /**
     * Inner class created to recognize our control buttons
     */
    public class MyKeyAdapter extends KeyAdapter {

        /**
         * Overriden method which is switching direction depending on what button was pressed
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT -> {
                    if(!direction.equals(Direction.RIGHT)){
                        direction = Direction.LEFT;
                    }
                }
                case KeyEvent.VK_UP -> {
                    if(!direction.equals(Direction.DOWN)){
                        direction = Direction.UP;
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if(!direction.equals(Direction.LEFT)){
                        direction = Direction.RIGHT;
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if(!direction.equals(Direction.UP)){
                        direction = Direction.DOWN;
                    }
                }
            }

            // If our game is over we close it by pressing any button
            if(!running){
                JFrame.getFrames()[0].setVisible(false);
                JFrame.getFrames()[0].dispose();
            }
        }
    }
}
