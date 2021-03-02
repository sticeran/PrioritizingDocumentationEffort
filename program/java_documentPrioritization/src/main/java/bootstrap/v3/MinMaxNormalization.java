package bootstrap.v3;

import java.util.Arrays;

import Jama.Matrix;
import weka.core.Instances;

/** Minimum maximum normalization is also called deviation normalization
 * The original data can be transformed linearly.
 * Assuming that Min and Max are minimum and maximum,
 * V is a value in the interval and maps it to a new interval [newMin, newMax] of V'
 * There are:
 * v' = (v-Min)/(Max-Min)*(newMax-newMin)+newMin
 * One drawback of this approach is that when new data is added, it may lead to changes in Max and Min, which need to be redefined.
 */
public class MinMaxNormalization {
	
	public static void main(String[] args) {
		MinMaxNormalization mmn = new MinMaxNormalization();
		double newV = mmn.numberNormalization(1500, 1000, 2000, 0, 1);
		System.out.println("The result is " + newV);
		double[] test = {1500,1200,1300};
		double[] newV2 = mmn.arrayNormalization(test, 1000, 2000, 0, 1);
		for(int i=0;i<newV2.length;i++)
		{
			System.out.println("The result is " + newV2[i]);
		}
	}
	/**
	 * @author 
	 * @param v Standardized sample data
	 * @param Min Minimum Sample Data
	 * @param Max Maximum Sample Data
	 * @param newMin New Minimum Value of Mapping Interval
	 * @param newMax New Maximum Value of Mapping Interval
	 * @return
	 */
	public double numberNormalization(double v, double Min, double Max, double newMin, double newMax) {
		return (v-Min)/(Max-Min)*(newMax-newMin)+newMin;
	}
	public double[] arrayNormalization(double[] v, double Min, double Max, double newMin, double newMax) {
		if(Min==Max && Max==0)
		{
			for(int i=0;i<v.length;i++)
			{
				v[i]=newMin;
			}	
		}
		else if(Min==Max && Max>0)
		{
			for(int i=0;i<v.length;i++)
			{
				v[i]=v[i]/Max*(newMax-newMin)+newMin;
			}			
		}
		else
		{
			for(int i=0;i<v.length;i++)
			{
				v[i]=(v[i]-Min)/(Max-Min)*(newMax-newMin)+newMin;
			}
		}
		return v;
	}
	public double[][] normalization(Instances data,int inputNeuronsCount){
		double newMin=0;
		double newMax=1;
		
		int lenInstances_data = data.size();
		
		double[][] dArray_matrix = new double[inputNeuronsCount][lenInstances_data];
		
		double min = 0;
		double max = 0;
		for (int i_inputAttribute = 0; i_inputAttribute < inputNeuronsCount-1; i_inputAttribute++) {
			double[] data_oneCol = data.attributeToDoubleArray(i_inputAttribute);
			min = data.kthSmallestValue(i_inputAttribute, 1);
			max = data.kthSmallestValue(i_inputAttribute, lenInstances_data);
			data_oneCol = arrayNormalization(data_oneCol, min, max, newMin, newMax);
			dArray_matrix[i_inputAttribute] = data_oneCol;
		}
		double[] data_oneCol = data.attributeToDoubleArray(inputNeuronsCount-1);
		dArray_matrix[inputNeuronsCount-1] = data_oneCol;
		Matrix matrix_data = new Matrix(dArray_matrix);
		matrix_data = matrix_data.transpose();
		
		return matrix_data.getArray();
	}
	public double[][] normalization(double[][] data,int inputNeuronsCount){
		double newMin=0;
		double newMax=1;
		
		Matrix matrix_data = new Matrix(data);
		
		double[][] dArray_matrix = matrix_data.transpose().getArray();
		
		double min = 0;
		double max = 0;
		for (int i_inputAttribute = 0; i_inputAttribute < inputNeuronsCount; i_inputAttribute++) {
			double[] data_oneCol = dArray_matrix[i_inputAttribute];
			min = Arrays.stream(data_oneCol).min().getAsDouble();
			max = Arrays.stream(data_oneCol).max().getAsDouble();
			data_oneCol = arrayNormalization(data_oneCol, min, max, newMin, newMax);
		}
		
		Matrix matrix_data_transpose = new Matrix(dArray_matrix);
		matrix_data_transpose = matrix_data_transpose.transpose();
		
		return matrix_data_transpose.getArray();
	}
}