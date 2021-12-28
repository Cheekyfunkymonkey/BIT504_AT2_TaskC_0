import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
 
public class PongPanel extends JPanel implements ActionListener, KeyListener {
	private static final Color BACKGROUND_COLOUR = Color.BLACK;
	private final static int TIMER_DELAY = 5;
	private GameState gameState = GameState.INITIALISING;
	private Paddle paddle1, paddle2;
	Ball ball;
	private final static int BALL_MOVEMENT_SPEED = 2;
	private final static int POINTS_TO_WIN = 3;
	int player1Score = 0, player2Score = 0;
	Player gameWinner;
    private static final int X_PADDING = 100;
    private static final int Y_PADDING = 100;
    private static final int FONT_SIZE = 50; 
    private final static int WINNER_TEXT_X = 200;
    private final static int WINNER_TEXT_Y = 200;
    private final static int WINNER_FONT_SIZE = 40;
    private final static String WINNER_FONT_FAMILY = "Serif";
    private final static String WINNER_TEXT = "WIN!";
	
	public PongPanel() {
		setBackground(BACKGROUND_COLOUR);
		Timer timer = new Timer(TIMER_DELAY, this);
        timer.start();
    	addKeyListener(this);
    	setFocusable(true);
	}

	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_W) {
            paddle1.setYVelocity(-1);
        } else if(event.getKeyCode() == KeyEvent.VK_S) {
            paddle1.setYVelocity(1);
        }
		
		if(event.getKeyCode() == KeyEvent.VK_UP) {
            paddle2.setYVelocity(-1);
        } else if(event.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2.setYVelocity(1);
        }
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_S) {
            paddle1.setYVelocity(0);
        }
		
		if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2.setYVelocity(0);
        }
	}
	
	public void createObjects() {
        ball = new Ball(getWidth(), getHeight());
        paddle1 = new Paddle(Player.ONE, getWidth(), getHeight());
        paddle2 = new Paddle(Player.TWO, getWidth(), getHeight());
	}
	
	private void update() {
		switch(gameState) {
	        case INITIALISING: {
	            createObjects();
	            gameState = GameState.PLAYING;
	            ball.setXVelocity(BALL_MOVEMENT_SPEED);
	            ball.setYVelocity(BALL_MOVEMENT_SPEED);
	            break;
	        }
	        case PLAYING: {
	        	moveObject(paddle1);
	        	moveObject(paddle2);
	            moveObject(ball);
	            checkWallBounce();
	            checkPaddleBounce();
	            checkWin();
	            break;
	        }
	        case GAMEOVER: {
	            break;
	        }
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		update();
		repaint();
	}
	
	@Override
	 public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     paintDottedLine(g);
	     if(gameState != GameState.INITIALISING) {
	          paintSprite(g, ball);
	          paintSprite(g, paddle1);
	          paintSprite(g, paddle2);
	          paintScores(g);
	          paintWin(g);
	      }
	 }
	
	 private void paintDottedLine(Graphics g) {
	      Graphics2D g2d = (Graphics2D) g.create();
	         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	         g2d.setStroke(dashed);
	         g2d.setPaint(Color.WHITE);
	         g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	         g2d.dispose();
	 }
	 
	 private void paintSprite(Graphics g, Sprite sprite) {
	      g.setColor(sprite.getColour());
	      g.fillRect(sprite.getXPosition(), sprite.getYPosition(), sprite.getWidth(), sprite.getHeight());
	 }
	 
	 private void moveObject(Sprite s) {
		 s.setXPosition(s.getXPosition() + s.getXVelocity(), getWidth());
		 s.setYPosition(s.getYPosition() + s.getYVelocity(), getHeight());
	 }
	 
	 private void checkWallBounce() {
		 if(ball.getXPosition() <= 0 ) {
			 ball.setXVelocity(- ball.getXVelocity());
			 resetBall();
			 addScore(Player.TWO);
		 } else if (ball.getXPosition() >= getWidth() - ball.getWidth()) {
			 ball.setXVelocity(- ball.getXVelocity());
			 resetBall();
			 addScore(Player.ONE);
		 }
		 
		 if(ball.getYPosition() <= 0 || ball.getYPosition() >= getHeight() - ball.getHeight() ) {
			 ball.setYVelocity(- ball.getYVelocity());
		 }
	 }
	 
	 private void resetBall() {
		 ball.resetToInitialPosition();
	 }
	 
	 private void checkPaddleBounce() {
		 if(ball.getXVelocity() < 0 && ball.getRectangle().intersects(paddle1.getRectangle())) {
	          ball.setXVelocity(BALL_MOVEMENT_SPEED);
	      }
	      if(ball.getXVelocity() > 0 && ball.getRectangle().intersects(paddle2.getRectangle())) {
	          ball.setXVelocity(-BALL_MOVEMENT_SPEED);
	      }
	 }
	 
	 private void addScore(Player p) {
		 if( p == Player.ONE) {
			 player1Score++;
		 } else {
			 player2Score++;
		 }
	 }
	 
	 private void checkWin() {
		 if(player1Score >= POINTS_TO_WIN) {
			 gameWinner = Player.ONE;
			 gameState = GameState.GAMEOVER;
		 } else if(player2Score>= POINTS_TO_WIN) {
			 gameWinner = Player.TWO;
			 gameState = GameState.GAMEOVER;
		 }
	 }
	 
	 private void paintScores(Graphics g) {
         Font scoreFont = new Font("Serif", Font.BOLD, FONT_SIZE);
         String leftScore = Integer.toString(player1Score);
         String rightScore = Integer.toString(player2Score);
         g.setFont(scoreFont);
         g.drawString(leftScore, X_PADDING, Y_PADDING);
        g.drawString(rightScore, getWidth()-X_PADDING, Y_PADDING);
    }
	 
	 private void paintWin(Graphics g) {
		 if(gameWinner != null) {
             Font winnerFont = new Font(WINNER_FONT_FAMILY, Font.BOLD, WINNER_FONT_SIZE);
            g.setFont(winnerFont);
            int xPosition = getWidth() / 2;
            if(gameWinner == Player.ONE) {
                xPosition -= WINNER_TEXT_X;
            } else if(gameWinner == Player.TWO) {
                xPosition += WINNER_TEXT_X;
            }
            g.drawString(WINNER_TEXT, xPosition, WINNER_TEXT_Y);
        }
    }
}