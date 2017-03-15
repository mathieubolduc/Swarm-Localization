package utils;

import java.lang.reflect.Array;
import java.util.Random;

public class utils {
	// returns a String representation of the contents an n-dimensional array.
	public static String arrayToString(Object array){
		String s = "";
		if(array.getClass().isArray()){
			// recursion case
			s += "{";
			for(int i=0; i<Array.getLength(array); i++){
				s += arrayToString(Array.get(array, i)) + ", ";
			}
			if(s.endsWith(", "))
				s = s.substring(0, s.length()-2);
			s += "}";
		}
		else{
			// base case
			s += array.toString();
		}
		return s;
	}
	
	
	// Copies every element of an n-dimensional array in a new n-dimensional array.
	@SuppressWarnings("unchecked")
	public static <T> T[] deepCopy(T[] original){
		return (T[]) deepCopyObject(original);
	}
	
	private static Object deepCopyObject(Object original){
		int length = Array.getLength(original);
		Object copy;
		Object type = Array.get(original, 0);
		// base case
		if(!type.getClass().isArray()){
			if(type instanceof Integer)
				copy = new int[length];
			else if(type instanceof Double)
				copy = new double[length];
			else if(type instanceof Long)
				copy = new long[length];
			else if(type instanceof Boolean)
				copy = new boolean[length];
			else if(type instanceof Character)
				copy = new char[length];
			else if(type instanceof Float)
				copy = new float[length];
			else if(type instanceof Byte)
				copy = new byte[length];
			else if(type instanceof Short)
				copy = new short[length];
			else
				copy = Array.newInstance(type.getClass(), length);
		}
		else{
			copy = Array.newInstance(type.getClass(), length);
		}
		// recursion case
		for(int i=0; i<length; i++){
			Object element = Array.get(original, i);
			if(element.getClass().isArray()){
				Array.set(copy, i, deepCopyObject(element));
			}
			else{
				Array.set(copy, i, element);
			}
		}
		return copy;
	}
	
	// shuffles an array in O(n) time.
	public static void shuffle(double[] array){
		Random r = new Random();
		for(int i=array.length-1; i>=0; i--){
			int j = r.nextInt(i+1);
			double foo = array[i];
			array[i] = array[j];
			array[j] = foo;
		}
	}
}
