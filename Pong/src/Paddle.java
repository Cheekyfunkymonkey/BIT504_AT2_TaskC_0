import java.awt.Color;

public class Paddle extends Sprite{
	final static int PADDLE_WIDTH = 10;
	final static int PADDLE_HEIGHT = 100;
	final static Color COLOUR = Color.WHITE;
	final static int DISTANCE_FROM_EDGE = 40;
	
	public Paddle(Player player, int panelWidth, int panelHeight) {
		setWidth(PADDLE_WIDTH);
		setHeight(PADDLE_HEIGHT);
		setColour(COLOUR);
		if (player == Player.ONE) {
			setInitialPosition(DISTANCE_FROM_EDGE, panelHeight / 2 - (getHeight() / 2));
			
		} else {
			setInitialPosition(panelWidth - DISTANCE_FROM_EDGE - getWidth(), panelHeight / 2 - (getHeight() / 2));
		}
		resetToInitialPosition();
	}
}
