package bootstrap.v3;

import weka.attributeSelection.*;

import weka.core.*;

import weka.core.converters.ConverterUtils.*;

import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron;
//import weka.classifiers.meta.*;

//import weka.classifiers.trees.*;

import weka.filters.*;

import java.util.*;

/**
 * 
 * performs attribute selection using CfsSubsetEval and GreedyStepwise
 * 
 * (backwards) and trains J48 with that. Needs 3.5.5 or higher to compile.
 *
 * 
 * 
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * 
 */

public class WekaAttributeSelection {

	/**
	 * 
	 * uses the meta-classifier
	 * 
	 */

	protected static void useClassifier(Instances data) throws Exception {

		System.out.println("n1. Meta-classfier");

//    AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();

//    CfsSubsetEval eval = new CfsSubsetEval();
//
//    GreedyStepwise search = new GreedyStepwise();
//
//    search.setSearchBackwards(true);

//    J48 base = new J48();
		MultilayerPerceptron base = new MultilayerPerceptron();
		base.setHiddenLayers("10,10");
		base.setLearningRate(0.25);
		base.setTrainingTime(1000);
//    base.setAutoBuild(true);
//    base.setNormalizeAttributes(true);
//    base.setNormalizeNumericClass(true);
		base.setNominalToBinaryFilter(true);
//    base.setSeed(5);

//    classifier.setClassifier(base);

//    classifier.setEvaluator(eval);

//    classifier.setSearch(search);

//    base.buildClassifier(data);
		
		Evaluation eval = null;
//    for(int i=0;i<10;i++){
		eval = new Evaluation(data);
//     eval.crossValidateModel(base, data, 10, new Random(i));
		eval.crossValidateModel(base, data, 10, new Random(0));
//    }
		System.out.println(eval.toSummaryString());
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
		System.out.printf("precision: %f\n", eval.weightedPrecision());
		System.out.printf("recall: %f\n", eval.weightedRecall());
		System.out.printf("F1: %f\n", eval.weightedFMeasure());

//    Evaluation evaluation = new Evaluation(data);
//    
//    evaluation.crossValidateModel(classifier, data, 10, new Random(1));
//
//    System.out.println(evaluation.toSummaryString());
//    System.out.println(evaluation.confusionMatrix()[0][0]);
//    System.out.println(evaluation.confusionMatrix()[0][1]);
//    System.out.println(evaluation.confusionMatrix()[1][0]);
//    System.out.println(evaluation.confusionMatrix()[1][1]);
//    System.out.printf("precision: %f\n",evaluation.weightedPrecision());
//    System.out.printf("recall: %f\n",evaluation.weightedRecall());
//    System.out.printf("F1: %f\n",evaluation.weightedFMeasure());

	}

	/**
	 * 
	 * uses the filter
	 * 
	 */

	protected static Instances useFilter(Instances data) throws Exception {

//		System.out.println("n2. Filter");

		weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();

		CfsSubsetEval eval = new CfsSubsetEval();

//		GreedyStepwise search = new GreedyStepwise();
		ASSearch search = new BestFirst();
		
//		search.setSearchBackwards(true);

		filter.setEvaluator(eval);

		filter.setSearch(search);

		filter.setInputFormat(data);

		Instances newData = Filter.useFilter(data, filter);

//		System.out.println(newData);
//		System.out.print("selected attribute: ");
//		for (int i = 0 ;i<newData.numAttributes();i++) {
//			System.out.print(newData.attribute(i));
//		}
//		System.out.print("\n");
//		System.out.println(newData.numAttributes());
		
		return newData;
	}

	/**
	 * 
	 * uses the low level approach
	 * 
	 */

	protected static void useLowLevel(Instances data) throws Exception {

		System.out.println("n3. Low-level");

		AttributeSelection attsel = new AttributeSelection();

		CfsSubsetEval eval = new CfsSubsetEval();

		GreedyStepwise search = new GreedyStepwise();

		search.setSearchBackwards(true);

		attsel.setEvaluator(eval);

		attsel.setSearch(search);

		attsel.SelectAttributes(data);

		int[] indices = attsel.selectedAttributes();

		System.out.println("selected attribute indices (starting with 0):n" + Utils.arrayToString(indices));

	}


	@SuppressWarnings("deprecation")
	protected static Instances delAttr(Instances model, Instances origin) {
		boolean flag = false;
		ArrayList<Integer> al = new ArrayList<Integer>();
		for (int q = 0; q < origin.numAttributes() - 1; q++) {
			String temp2 = origin.attribute(q).name();
			for (int x = 0; x < model.numAttributes() - 1; x++) {
				String temp1 = model.attribute(x).name();
				if (temp1.equals(temp2)) {
					flag = true;
					break;
				}
			}
			if (flag) {
				flag = false;
				continue;
			} else
//              dataCopy.deleteAttributeAt(q);   //you can not do like this
				al.add(new Integer(q));
		}

		for (int q = 0; q < al.size(); q++) {
			int deltemp = al.get(q) - q; // pay attention to this line
			origin.deleteAttributeAt(deltemp);
		}

		return origin;
	}

	/**
	 * 
	 * takes a dataset as first argument
	 *
	 * 
	 * 
	 * @param args the commandline arguments
	 * 
	 * @throws Exception if something goes wrong
	 * 
	 */

	public static void main(String[] args) throws Exception {
		String filePath = "D:\\workspace\\DataFolder\\newNumeric\\arff_v2.0\\jgrapht\\"; 
		String fileName = "jgrapht_VSM_stemming_lowercase.arff";
		// load data

		System.out.println("n0. Loading data");

		DataSource source = new DataSource(filePath + fileName);

		Instances data = source.getDataSet();

		if (data.classIndex() == -1)

			data.setClassIndex(data.numAttributes() - 1);

		// 1. meta-classifier

//    useClassifier(data);

		// 2. filter

		useFilter(data);

		// 3. low-level

//    useLowLevel(data);

	}

}