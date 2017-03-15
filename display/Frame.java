package display;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	private Map map;
	
	public Frame(){
		super("Swarm");
		
		map = new Map();
		
		setContentPane(map);
		
		setPreferredSize(new Dimension(800, 500));
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public ArrayList<double[][]> getPoints(){
		return map.getPoints();
	}
	
	public ArrayList<double[][]> getVectors(){
		return map.getVectors();
	}
	
}
