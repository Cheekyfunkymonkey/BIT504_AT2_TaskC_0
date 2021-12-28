import javax.swing.JFrame;

public class Pong extends JFrame{
	private static final String TITLE = "Pong";
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 800;
	
	public Pong() {
    	setTitle(TITLE);
    	setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	setResizable(false);
    	add(new PongPanel());
    	setVisible(true);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Pong();
			}
		});
		
	}

}
