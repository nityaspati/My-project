package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class Board extends JPanel implements ActionListener {
    private Image apple;
    private Image dot;
    private Image head;

    private final int All_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;
    private final int DELAY = 140;

    private int apple_x;
    private int apple_y;
    private int score = 0;
    private int highScore = 0;  // High score variable

    private final int x[] = new int[All_DOTS];
    private final int y[] = new int[All_DOTS];

    private int dots;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    Board() {
        setBackground(Color.BLACK);
        setFocusable(true);

        loadImage();
        initGame();
        addKeyListener(new TAdapter());
    }

    public void loadImage() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head = i3.getImage();
    }

    public void initGame() {
        dots = 3;
        score = 0;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
        inGame = true;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
    }

    public void locateApple() {
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            draw(g);
        } else {
            gameOver(g);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(apple, apple_x, apple_y, this);

        for (int i = 0; i < dots; i++) {
            if (i == 0) {
                g.drawImage(head, x[i], y[i], this);
            } else {
                g.drawImage(dot, x[i], y[i], this);
            }
        }

        drawScore(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void drawScore(Graphics g) {
        String scoreText = "Score: " + score + "  High Score: " + highScore;
        Font font = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(scoreText, 5, 10);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score += 10;
            locateApple();
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= getHeight() || y[0] < 0 || x[0] >= getWidth() || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
            if (score > highScore) {
                highScore = score;  // Update high score if current score is higher
            }
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over";
        String restartMsg = "Press R to Restart";
        String finalScore = "Final Score: " + score;
        String highScoreMsg = "High Score: " + highScore;

        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, getHeight() / 2 - 40);
        g.drawString(finalScore, (getWidth() - metr.stringWidth(finalScore)) / 2, getHeight() / 2);
        g.drawString(highScoreMsg, (getWidth() - metr.stringWidth(highScoreMsg)) / 2, getHeight() / 2 + 20);
        g.drawString(restartMsg, (getWidth() - metr.stringWidth(restartMsg)) / 2, getHeight() / 2 + 60);
    }

    public void restartGame() {
        dots = 3;
        score = 0;
        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();
        timer.start();
        inGame = true;

        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_R && !inGame) {
                restartGame();
            }

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
