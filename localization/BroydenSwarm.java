package localization;

import java.util.Scanner;

import utils.Matrix;

public class BroydenSwarm {
	
	private double[] x;		// positions in [x1, x2, x3, . . . xn, y1, y2, y3, . . . yn]
	private double[] v;		// vectors in [Vx1, Vx2, Vx3, . . . Vxn, Vy1, Vy2, Vy3, . . . Vyn]
	private double bound;
	
	private final double H = 0.0001;
	
	public BroydenSwarm(int n, double bound){
		x = new double[2*n];
		v = new double[2*n];
		this.bound = bound;
		
		for(int i=0; i<x.length; i++){
			x[i] = Math.random() * bound;
		}
		
		calculateVectors();
	}
	
	
	
	public void localize(){
		
		// init
		shufflePoints();
		
		Matrix x = new Matrix(this.x, this.x.length, 1);
		Matrix v = new Matrix(this.v, this.v.length, 1);
		Matrix f = new Matrix(this.x.length, 1);
		double[] jFlatArray = Jacobian(x).toFlatArray();
		double[][] jArray = new double[this.x.length][this.x.length];
		for(int i=0; i<jFlatArray.length; i++){
			jArray[i / jArray.length][i % jArray.length] = jFlatArray[i];
		}
		Jama.Matrix jacobian = new Jama.Matrix(jArray);
		jArray = jacobian.inverse().getArray();
		for(int i=0; i<jFlatArray.length; i++){
			jFlatArray[i] = jArray[i / jArray.length][i % jArray.length];
		}
		Matrix J = new Matrix(jFlatArray, this.x.length, this.x.length);

		System.out.println(J);
		
		
		Matrix Δx = new Matrix(this.x.length, 1);
		Matrix Δf = new Matrix(this.x.length, 1);
		Matrix intermediate = new Matrix(1, this.x.length);
		Matrix oldX = new Matrix(this.x.length, 1);
		Matrix oldF = new Matrix(this.x.length, 1);
		
		Scanner scan = new Scanner(System.in);
		this.x = x.toFlatArray();
		Main.frame.getPoints().add(this.getPoints());
		Main.frame.repaint();
		
		while(true){
			
			// calculate the new values          xn+1 = xn - Jn * f(xn)
			oldX.clone(x);
			x.subtractSelf(J.multiply(oldF));
			Matrix.subtract(x, oldX, Δx);
			f.clone(f(x));
			Matrix.subtract(f, oldF, Δf);
			oldF.clone(f);
			
			
			// calculate the inverse jacobian      Jn   =   Jn-1   +  ( Δxn  -  Jn-1 * Δfn ) / ( Δxn^T * Jn-1 * Δfn ) * Δxn^T * Jn-1
			
			Matrix.multiplyTransposeA(Δx, J, intermediate);
			J.addSelf((Δx.subtract(J.multiply(Δf)).multiply(intermediate.multiply(Δf).getEntry(0, 0))).multiply(intermediate));
			
			scan.nextLine();
			System.out.println(J);
			this.x = x.toFlatArray();
			Main.frame.getPoints().set(1, this.getPoints());
			Main.frame.repaint();
		}
	}
	
	
	private Matrix f(Matrix x){
		// calculate f(x)
		Matrix f = new Matrix(this.v.length, 1);
		double foo;
		for(int i=0; i<this.v.length; i++){
			foo = -this.v[i];
			if(i<this.v.length/2){
				// do the x
				for(int j=0; j<this.v.length/2; j++){
					if(i == j)		// skip the same point
						continue;
					foo += (x.getEntry(j, 0) - x.getEntry(i, 0)) / Math.pow(Math.pow(x.getEntry(j, 0) - x.getEntry(i, 0), 2) + Math.pow(x.getEntry(j + this.x.length/2, 0) - x.getEntry(i + this.x.length/2, 0), 2), 1.5);
				}
			}
			else {
				// do the y
				for(int j=this.x.length/2; j<this.x.length; j++){
					if(i == j)		// skip the same point
						continue;
					foo += (x.getEntry(j, 0) - x.getEntry(i, 0)) / Math.pow(Math.pow(x.getEntry(j, 0) - x.getEntry(i, 0), 2) + Math.pow(x.getEntry(j - this.x.length/2, 0) - x.getEntry(i - this.x.length/2, 0), 2), 1.5);
				}
			}
			f.setEntry(i, 0, foo);
		}
		return f;
	}
	
	
	private Matrix Jacobian(Matrix x){
		
		Matrix newX = new Matrix(this.x.length, 1);
		Matrix J = new Matrix(this.x.length, this.x.length);
		Matrix fBase = f(x);
		Matrix f;
		for(int i=0; i<this.x.length; i++){
			newX.clone(x);
			newX.setEntry(i, 0, newX.getEntry(i, 0) + H);
			f = f(newX);
			f.subtractSelf(fBase);
			f.multiply(1/H);
			for(int j=0; j<this.x.length; j++){
				J.setEntry(j, i, f.getEntry(j, 0));
			}
		}
		
		return J;
	}
	
	
	
	public void setVectors(double[][] vectors){
		for(int i=0; i<v.length; i++){
			v[i] = vectors[i % vectors.length][i / vectors.length];
		}
	}
	
	public void setPoints(double[][] points){
		for(int i=0; i<x.length; i++){
			x[i] = points[i % points.length][i / points.length];
		}
	}
	
	public double[][] getPoints(){
		double[][] points = new double[x.length/2][2];
		for(int i=0; i<x.length; i++){
			points[i % points.length][i / points.length] = x[i];
		}
		return points;
	}
	
	public double[][] getVectors(){
		double[][] vectors = new double[v.length/2][2];
		for(int i=0; i<v.length; i++){
			vectors[i % vectors.length][i / vectors.length] = v[i];
		}
		return vectors;
	}
	
	private void shufflePoints(){
		for(int i=0; i<x.length; i++){
			x[i] = Math.random() * bound;
		}
	}
	
	private void calculateVectors(){
		double foo;
		for(int i=0; i<x.length; i++){
			foo = 0;
			if(i<this.v.length/2){
				for(int j=0; j<x.length/2; j++){
					if(i == j)
						continue;
					foo += (x[j] - x[i]) / Math.pow(Math.pow(x[j] - x[i], 2) + Math.pow(x[j + this.x.length/2] - x[i + this.x.length/2], 2), 1.5);
				}
			}
			else {
				for(int j=x.length/2; j<x.length; j++){
					if(i == j)
						continue;
					foo += (x[j] - x[i]) / Math.pow(Math.pow(x[j] - x[i], 2) + Math.pow(x[j - this.x.length/2] - x[i - this.x.length/2], 2), 1.5);
				}
			}
			v[i] = foo;
		}
	}
	
}
