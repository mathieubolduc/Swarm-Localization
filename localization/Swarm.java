package localization;

import java.util.Random;

public class Swarm {
	
	private double[][] points;		// the 1st point is always at the origin
	private double[][] vectors;
	private Random rand;
	private double[] bounds;
	
	private static final double INERTIA = 0.8, PERSONAL_ACC = 2, SOCIAL_ACC = 2;
	
	public Swarm(int size, int dimension, double... bounds){
		this.points = new double[size][dimension];
		this.vectors = new double[size][dimension];
		this.rand = new Random();
		this.bounds = bounds;
		
		for(int i=1; i<points.length; i++){
			for(int j=0; j<points[i].length; j++){
				points[i][j] = rand.nextDouble() * bounds[j];
			}
		}
	}
	
	public Swarm(int size, int dimension){
		this(size, dimension, new double[]{1, 1});
	}
	
	
	
	public void localizeGD(){
		double[][] velocities = new double[points.length][points[0].length];
		while(true){
			
		}
	}
	
	
	// fuck this, i'm out
	public void localizePSO(){
		
		// initialize
		double[][] velocities = new double[points.length][points[0].length];
		double[][] bestPoints = new double[points.length][points[0].length];
		double[] globalBestPoint = new double[points[0].length];
		double[] bestCosts = new double[points.length];
		double globalBestCost = Double.POSITIVE_INFINITY;
		double cost = 0;
		
		// populate the arrays
		for(int i=1; i<points.length; i++){
			for(int j=0; j<points[i].length; j++){
				velocities[i][j] = rand.nextDouble();
				bestPoints[i][j] = points[i][j];
			}
			
			double[] vector = calculateVector(i);
			cost = 0;
			for(int j=1; j<points[i].length; j++){
				double diff = (vector[j] - vectors[i][j]);
				cost += diff * diff;
			}
			bestCosts[i] = cost;
			
			if(cost < globalBestCost){
				globalBestCost = cost;
				for(int j=1; j<points[i].length; j++){
					globalBestPoint[j] = points[i][j];
				}
			}
		}
		
		
		while(true){
			// do one iteration of gradient descent
			
			// for each point
			for(int i=1; i<points.length; i++){
				
				// update the point
				for(int j=0; j<points[i].length; j++){
					velocities[i][j] = INERTIA * velocities[i][j] + PERSONAL_ACC * rand.nextDouble() * (bestPoints[i][j] - points[i][j]) 
							+ SOCIAL_ACC * rand.nextDouble() * (globalBestPoint[j] - points[i][j]);
					points[i][j] += velocities[i][j];
				}
				
				// calculate the new vector
				double[] vector = calculateVector(i);
				
				// calculate the cost
				cost = 0;
				for(int j=1; j<points[i].length; j++){
					double diff = (vector[j] - vectors[i][j]);
					cost += diff * diff;
				}
				
				// save the personal best
				if(cost < bestCosts[i]){
					bestCosts[i] = cost;
					for(int j=1; j<points[i].length; j++){
						bestPoints[i][j] = points[i][j];
					}
				}
				
				// save the global best
				if(cost < globalBestCost){
					globalBestCost = cost;
					for(int j=1; j<points[i].length; j++){
						globalBestPoint[j] = points[i][j];
					}
				}
				
			}
			
			try{Thread.sleep(20);}catch(Exception e){}
			//Main.frame.getPoints().set(1, utils.deepCopy(points));
			Main.frame.repaint();
			
		}
	}
	
	// calculates the i'th point's vector according to the inverse square rule: |v| = 1/r^2
	private double[] calculateVector(int i){ 
		double[] vector = new double[vectors[i].length];
		double distance;
		double distanceSquared;
		for(int j=0; j<points.length; j++){
			// skip the same point
			if(j != i){
				distanceSquared = 0;
				// find the distance cubed
				for(int k=0; k<points[j].length; k++){
					distance = (points[j][k] - points[i][k]);
					distanceSquared += distance * distance;
				}
				// add the resulting vector
				for(int k=0; k<points[j].length; k++){
					vector[k] += (points[j][k] - points[i][k]) / Math.pow(distanceSquared, 1.5);
				}
			}
		}
		return vector;
	}
	
	public double[][] calculateVectors(){
		double[][] vectors = new double[this.vectors.length][this.vectors[0].length];
		for(int i=0; i<points.length; i++){
			vectors[i] = calculateVector(i);
		}
		return vectors;
	}
	
	public void shufflePoints(){
		for(int i=1; i<points.length; i++){
			for(int j=0; j<points[i].length; j++){
				points[i][j] = rand.nextDouble() * bounds[j];
			}
		}
	}
	
	public void setVectors(double[][] vectors){
		if(vectors.length != this.vectors.length)
			throw new RuntimeException("Incorrect number of points.");
		this.vectors = vectors;
	}
	
	public void setPoints(double[][] points){
		if(points.length != this.points.length)
			throw new RuntimeException("Incorrect number of points.");
		this.points = points;
	}
	
	public double[][] getPoints(){
		return points;
	}
	
	public double[][] getVectors(){
		return vectors;
	}
	
}
