package org.vast.math;

import javax.vecmath.Vector3d;

public class BilinearInterpolation {
	//  Can also turn this into a process later
	Vector3d c11, c12, c21, c22;
	
	public BilinearInterpolation(){
		
	}
	
	public void setCorners(Vector3d c11, Vector3d c12, Vector3d c21, Vector3d c22){
		this.c11 = c11;
		this.c12 = c12;
		this.c21 = c21;
		this.c22 = c22;
	}
	
	public double interpolate(double x, double y ){
		double result=0.0;
		double delX = c21.x - c11.x;
		double delY = c12.y - c11.y;
		double delXY = delX * delY;
		
		double term1 = c11.z * (c21.x - x) * (c22.y - y)/delXY;
		double term2 = c21.z * (x - c11.x) * (c22.y - y)/delXY;
		double term3 = c12.z * (c21.x - x) * (y - c11.y)/delXY;
		double term4 = c22.z * (x - c11.x) * (y - c11.y)/delXY;
		
		result = term1 + term2 + term3 + term4;
		
		return result;
	}
}

