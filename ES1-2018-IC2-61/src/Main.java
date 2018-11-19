import java.awt.EventQueue;

public class Main {

	/** 
	 * Creates the GUI and sets it visible 
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
