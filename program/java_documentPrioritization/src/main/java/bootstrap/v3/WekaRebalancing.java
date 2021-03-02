package bootstrap.v3;

import weka.core.*;

import weka.filters.unsupervised.attribute.NumericToNominal;

import weka.filters.*;
import weka.filters.supervised.attribute.NominalToBinary;

/**
 * 
 * performs rebalancing using SMOTE
 *
 * 
 * 
 * @author Shiran Liu
 * 
 */

public class WekaRebalancing {

	/**
	 * 
	 * uses the filter
	 * 
	 */

	protected static Instances rebalancing(Instances data, int seed) throws Exception {
		//Get label column subscript
		int index_label = 0;
        for(int j = 0; j < data.numAttributes(); j++){
            if(data.attribute(j).name().equals("isImportant")){
            	index_label = j;
            	break;
            }
        }
        
		//Obtain the proportion relationship between majority class and minority class
		double[] col_label = data.attributeToDoubleArray(index_label);
		int num_1 = 0;
		int num_0 = 0;
		double percentage = 0;
		for(int i = 0; i < col_label.length; i++)
		{
			if(col_label[i] == 1) num_1++;
		}
		num_0 = col_label.length - num_1;
		if (num_1 <= num_0)
		{
			percentage = ((num_0*1.0)/(num_1*1.0) - 1)*100;
		}
		else
		{
			percentage = ((num_1*1.0)/(num_0*1.0) - 1)*100;
		}
        
        //Change the label column to "nominal"
		NumericToNominal num2Nominal = new NumericToNominal();
		num2Nominal.setInputFormat(data);
		num2Nominal.setAttributeIndices(String.valueOf(index_label+1));
		data = Filter.useFilter( data , num2Nominal);
		
		Instances newData;
		if (num_1>1) {
			//smote
//			int len1 = data.size();
			SMOTE smote = new SMOTE();
			String[] options = {};
			if (num_1>=5) {
				String[] temp = {"-S", String.valueOf(seed), "-P", String.valueOf(percentage), "-K", "5"};
				options = temp;
			}
			else {//1<num_1<5
				String[] temp = {"-S", String.valueOf(seed), "-P", String.valueOf(percentage), "-K", String.valueOf(num_1)};
				options = temp;
			}
			smote.setOptions(options);
//			smote.setMaxCount(n); //number of majority class n
			smote.setInputFormat(data);
			newData = Filter.useFilter(data, smote);
//			int len2 = newData.size();
		}
		else {
			newData = data;
		}
		
//		System.out.println(newData);
		
		return newData;
	}

}