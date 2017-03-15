package localization;

import display.Frame;
import utils.utils;

public class Main {
	
	private static final int SIZE = 5;
	public static Swarm swarm;
	public static Frame frame;

	public static void main(String[] args) {
		swarm = new Swarm(SIZE, 2, 100, 100);
		frame = new Frame();
		
		swarm.setVectors(swarm.calculateVectors());
		
		frame.getPoints().add(utils.deepCopy(swarm.getPoints()));
		frame.getVectors().add(utils.deepCopy(swarm.getVectors()));
		frame.repaint();
		
		swarm.shufflePoints();
		
		frame.getPoints().add(swarm.getPoints());
		frame.repaint();
		
		swarm.localizePSO();
	}

}
