package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Map extends JPanel{
	
	private static final Color[] COLORS = {Color.BLACK, Color.RED, Color.PINK, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE};
	
	private ArrayList<double[][]> points;
	private ArrayList<double[][]> vectors;
	private Object lock;
	
	public Map(){
		points = new ArrayList<double[][]>();
		vectors = new ArrayList<double[][]>();
		lock = new Object();
	}

	public void paintComponent(Graphics g){
		
		synchronized (lock) {
			// do the lame stuff
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
			// background
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			// get scale
			double xScale = 0, yScale = 0;	// in pixel per unit
			double xMax = 0, yMax = 0;
			for(double[][] pts : points){
				for(double[] pt : pts){
					if(pt[0] > xMax)
						xMax = pt[0];
					if(pt[1] > yMax){
						yMax = pt[1];
					}
				}
			}
			xScale = 0.8 * getWidth() / xMax;
			yScale = 0.8 * getHeight() / yMax;
			
			// points and vectors
			for(int i=0; i<points.size(); i++){
				g2.setColor(COLORS[i % COLORS.length]);
				double[][] pts = points.get(i);
				double[][] v = null;
				try{v = vectors.get(i);}catch(Exception e){}
				for(int j=0; j<pts.length; j++){
					int x = (int)(pts[j][0] * xScale + 0.1 * getWidth());
					int y = (int)(0.9 * getHeight() - pts[j][1] * yScale);
					g2.fillOval(x, y, 10, 10);
					if(v != null)
						g2.drawLine(x, y, x + (int)(v[j][0] * xScale * 10000), y - (int)(v[j][1] * yScale * 10000));
				}
			}
			
		}
	}
	
	public ArrayList<double[][]> getPoints(){
		synchronized (lock) {
			return points;
		}
	}
	
	public ArrayList<double[][]> getVectors(){
		synchronized (lock) {
			return vectors;
		}
	}
	
}
