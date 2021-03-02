package bootstrap.v3;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.random.RangeRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Bootstrap {
	// loads data and set class index
	DataSource source = null;// contains the full dataset we wann create train/test sets from
	Instances data = null;// raw data
	Instances train = null;// Train data
	Instances test = null;// Test data
	// classifier
	CalculatePerceptron cp = null;// In this class, a multi-layer (feed-forward) neural network is created.
	int inputNeuronsCount = 0;// Number of neurons in input layer
	int outputNeuronsCount = 0;// Number of neurons in output layer

	DataSet trainSet = null;// Establishment of training set and test set
	DataSet testSet = null;// Because the format of the Instances data does not match that of the neuralNet.learn () method, it needs to be transformed
	MinMaxNormalization normal = null;// Normalization tool class, Neuroph framework needs to normalize all input values first
	// other options
	int runs = 0;
	int folds = 0;
	SorfMultiDoubleArray smda = null;// Tool class, sorted by predicted values
	ArrayList<Integer> list_raw;// Subscript list of original data set for bootstrap to generate training set and test set
	
	public Bootstrap(String filePath) {
		// loads data and set class index
		try {
			source = new DataSource(filePath);// contains the full dataset we wann create train/test sets from
			data = source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String clsIndex = "last";
		if (clsIndex.length() == 0)
			clsIndex = "last";
		if (clsIndex.equals("first"))
			data.setClassIndex(0);
		else if (clsIndex.equals("last"))
			data.setClassIndex(data.numAttributes() - 1);
		else
			data.setClassIndex(Integer.parseInt(clsIndex) - 1);

		// classifier
		cp = new CalculatePerceptron();
		inputNeuronsCount = data.numAttributes() - 1;
		outputNeuronsCount = 1;

//		neuralNet = cp.createMultiLayerPerceptron(inputNeuronsCount, outputNeuronsCount);
		train = new Instances(data,0);
		test = new Instances(data,0);
		
		trainSet = new DataSet(inputNeuronsCount, outputNeuronsCount);
		testSet = new DataSet(inputNeuronsCount, outputNeuronsCount);
		
		normal = new MinMaxNormalization();
		
		// other options
		runs = 10;
		folds = 10;
		smda = new SorfMultiDoubleArray();
		
		list_raw = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			list_raw.add(i);
		}
	}
	
	public void runBootstrapping_rebalancing_featureSelection(Metrics metrics, int runs, double threshold) throws Exception {
		int total_bootstrap = runs; // Number of bootstrap iterations
		int datasize = data.size();
		int initialSeed = 0;// Neural network guarantees recurrence by setting random weights at each level
		for (int i_bootstrap = 0; i_bootstrap < total_bootstrap; i_bootstrap++) {
			
			oneRunBootstrapping(i_bootstrap, datasize);//The training set of n elements sampled randomly is obtained, and the test set of eliminating the training set is obtained.
			
			// rebalancing
			train = WekaRebalancing.rebalancing(data, initialSeed);
			
			// The feature selection is performed on the training set. The test set only leaves the attributes after feature selection of the training set.
			train = WekaAttributeSelection.useFilter(train);
			test = WekaAttributeSelection.delAttr(train, test);
			int inputNeuronsCount_featureSelection = train.numAttributes() - 1;
			
			// Because the format of the Instances data does not match that of the neuralNet.learn () method, it needs to be transformed
			trainSet = new DataSet(inputNeuronsCount_featureSelection, outputNeuronsCount);
			testSet = new DataSet(inputNeuronsCount_featureSelection, outputNeuronsCount);
			
			// all inputs need normalization
			double[][] train_normalization = normal.normalization(train, inputNeuronsCount_featureSelection+1);
			double[][] test_normalization = normal.normalization(test, inputNeuronsCount_featureSelection+1);


			trainSet = changeToDataset(trainSet, train_normalization);
			testSet = changeToDataset(testSet, test_normalization);

			
			MultiLayerPerceptron neuralNet = cp.createMultiLayerPerceptron(inputNeuronsCount_featureSelection, outputNeuronsCount);
			
			// Randomizes connection weights for the whole network using specified. For the purpose of reappearance.
			initialSeed = i_bootstrap;
			Random weightsSeed = new Random(initialSeed);
			RangeRandomizer weightsRange = new RangeRandomizer(-0.7, 0.7);
			weightsRange.setRandomGenerator(weightsSeed);
			neuralNet.randomizeWeights(weightsRange);
			
			
			neuralNet.learn(trainSet);
			
			double[] predictedResults = cp.testNeuralNetwork(neuralNet, testSet);
			

			double[][] predictedActual = combinePredictedActual(predictedResults, test_normalization);
			
			
			predictedActual = smda.soft_second_ASC(predictedActual);
			
			predictedActual = smda.soft(predictedActual);
			
			
			((Metrics_bootstrap) metrics).calculateMetrics_bootstrap(predictedActual, i_bootstrap, threshold);
			System.out.printf("the %d run completes the evaluation.\n", i_bootstrap);
		}
	}
	
	public void runBootstrapping_rebalancing(Metrics metrics, int runs, double threshold) throws Exception {
		int total_bootstrap = runs; // Number of bootstrap iterations
		int datasize = data.size();
		int initialSeed = 0;// Neural network guarantees recurrence by setting random weights at each level
		for (int i_bootstrap = 0; i_bootstrap < total_bootstrap; i_bootstrap++) {
			
			oneRunBootstrapping(i_bootstrap, datasize);//The training set of n elements sampled randomly is obtained, and the test set of eliminating the training set is obtained.
			
			// rebalancing
			train = WekaRebalancing.rebalancing(train, initialSeed);
			
			trainSet.clear();
			testSet.clear();

			// all inputs need normalization
			double[][] train_normalization = normal.normalization(train, inputNeuronsCount+1);
			double[][] test_normalization = normal.normalization(test, inputNeuronsCount+1);

			// Assignment of trainSet and testSet according to the "double" data format input and output arrays required by neuroph
			trainSet = changeToDataset(trainSet, train_normalization);
			testSet = changeToDataset(testSet, test_normalization);

			// A multi-layer (feed-forward) neural network with two hidden layers and 10 neurons in each hidden layer is established. The maximum number of iterations is 1000 and the learning rate is 0.25.
			MultiLayerPerceptron neuralNet = cp.createMultiLayerPerceptron(inputNeuronsCount, outputNeuronsCount);
			
			// Randomizes connection weights for the whole network using specified. For the purpose of reappearance.
			initialSeed = i_bootstrap;
			Random weightsSeed = new Random(initialSeed);
			RangeRandomizer weightsRange = new RangeRandomizer(-0.7, 0.7);
			weightsRange.setRandomGenerator(weightsSeed);
			neuralNet.randomizeWeights(weightsRange);
			
			// train
			neuralNet.learn(trainSet);
			// test
			double[] predictedResults = cp.testNeuralNetwork(neuralNet, testSet);
			
			// Matrix of predicted value and actual label to facilitate calculation of precision, recall and F1
			double[][] predictedActual = combinePredictedActual(predictedResults, test_normalization);
			

			predictedActual = smda.soft_second_ASC(predictedActual);

			predictedActual = smda.soft(predictedActual);
			
			
			((Metrics_bootstrap) metrics).calculateMetrics_bootstrap(predictedActual, i_bootstrap, threshold);
			System.out.printf("the %d run completes the evaluation.\n", i_bootstrap);
		}
	}
	
	public void runBootstrapping_featureSelection(Metrics metrics, int runs, double threshold) throws Exception {
		int total_bootstrap = runs; // Number of bootstrap iterations
		int datasize = data.size();
		int initialSeed = 0;// Neural network guarantees recurrence by setting random weights at each level
		for (int i_bootstrap = 0; i_bootstrap < total_bootstrap; i_bootstrap++) {
			
			oneRunBootstrapping(i_bootstrap, datasize);//The training set of n elements sampled randomly is obtained, and the test set of eliminating the training set is obtained.
			
			// The feature selection is performed on the training set. The test set only leaves the attributes after feature selection of the training set.
			train = WekaAttributeSelection.useFilter(train);
			test = WekaAttributeSelection.delAttr(train, test);
			int inputNeuronsCount_featureSelection = train.numAttributes() - 1;
			
			// Because the format of the Instances data does not match that of the neuralNet.learn () method, it needs to be transformed
			trainSet = new DataSet(inputNeuronsCount_featureSelection, outputNeuronsCount);
			testSet = new DataSet(inputNeuronsCount_featureSelection, outputNeuronsCount);
			
			// all inputs need normalization
			double[][] train_normalization = normal.normalization(train, inputNeuronsCount_featureSelection+1);
			double[][] test_normalization = normal.normalization(test, inputNeuronsCount_featureSelection+1);


			trainSet = changeToDataset(trainSet, train_normalization);
			testSet = changeToDataset(testSet, test_normalization);

			
			MultiLayerPerceptron neuralNet = cp.createMultiLayerPerceptron(inputNeuronsCount_featureSelection, outputNeuronsCount);
			
			// Randomizes connection weights for the whole network using specified. For the purpose of reappearance.
			initialSeed = i_bootstrap;
			Random weightsSeed = new Random(initialSeed);
			RangeRandomizer weightsRange = new RangeRandomizer(-0.7, 0.7);
			weightsRange.setRandomGenerator(weightsSeed);
			neuralNet.randomizeWeights(weightsRange);
			
			
			neuralNet.learn(trainSet);
			
			double[] predictedResults = cp.testNeuralNetwork(neuralNet, testSet);
			

			double[][] predictedActual = combinePredictedActual(predictedResults, test_normalization);
			
			
			predictedActual = smda.soft_second_ASC(predictedActual);
			
			predictedActual = smda.soft(predictedActual);
			
			
			((Metrics_bootstrap) metrics).calculateMetrics_bootstrap(predictedActual, i_bootstrap, threshold);
			System.out.printf("the %d run completes the evaluation.\n", i_bootstrap);
		}
	}
	
	public void runBootstrapping(Metrics metrics, int runs, double threshold) {
		int total_bootstrap = runs; // Number of bootstrap iterations
		int datasize = data.size();
		int initialSeed = 0;// Neural network guarantees recurrence by setting random weights at each level
		for (int i_bootstrap = 0; i_bootstrap < total_bootstrap; i_bootstrap++) {
			
			oneRunBootstrapping(i_bootstrap, datasize);//The training set of n elements sampled randomly is obtained, and the test set of eliminating the training set is obtained.
			
			
			trainSet.clear();
			testSet.clear();

			// all inputs need normalization
			double[][] train_normalization = normal.normalization(train, inputNeuronsCount+1);
			double[][] test_normalization = normal.normalization(test, inputNeuronsCount+1);

			// Assignment of trainSet and testSet according to the "double" data format input and output arrays required by neuroph
			trainSet = changeToDataset(trainSet, train_normalization);
			testSet = changeToDataset(testSet, test_normalization);

			// A multi-layer (feed-forward) neural network with two hidden layers and 10 neurons in each hidden layer is established. The maximum number of iterations is 1000 and the learning rate is 0.25.
			MultiLayerPerceptron neuralNet = cp.createMultiLayerPerceptron(inputNeuronsCount, outputNeuronsCount);
			
			// Randomizes connection weights for the whole network using specified. For the purpose of reappearance.
			initialSeed = i_bootstrap;
			Random weightsSeed = new Random(initialSeed);
			RangeRandomizer weightsRange = new RangeRandomizer(-0.7, 0.7);
			weightsRange.setRandomGenerator(weightsSeed);
			neuralNet.randomizeWeights(weightsRange);
			
			// train
			neuralNet.learn(trainSet);
			// test
			double[] predictedResults = cp.testNeuralNetwork(neuralNet, testSet);
			
			// Matrix of predicted value and actual label to facilitate calculation of precision, recall and F1
			double[][] predictedActual = combinePredictedActual(predictedResults, test_normalization);
			

			predictedActual = smda.soft_second_ASC(predictedActual);

			predictedActual = smda.soft(predictedActual);
			
			
			((Metrics_bootstrap) metrics).calculateMetrics_bootstrap(predictedActual, i_bootstrap, threshold);
			System.out.printf("the %d run completes the evaluation.\n", i_bootstrap);
		}
	}
	
	public void runBootstrapping_pagerank(Metrics metrics, int runs, double threshold) {
		int total_bootstrap = runs;
		int datasize = data.size();
		for (int i_bootstrap = 0; i_bootstrap < total_bootstrap; i_bootstrap++) {
			oneRunBootstrapping(i_bootstrap, datasize);
			// PageRank has been calculated, which is equivalent to the probability calculated by the supervised model.
			double[] predictedResults = test.attributeToDoubleArray(0);
			double[] actualResults = test.attributeToDoubleArray(1);
			
			double[][] predictedActual = combinePredictedActual_pagerank(predictedResults,actualResults);
			
			
			predictedActual = smda.soft_second_ASC(predictedActual);
			
			predictedActual = smda.soft(predictedActual);
			

			((Metrics_bootstrap) metrics).calculateMetrics_bootstrap(predictedActual, i_bootstrap, threshold);
			System.out.printf("the %d run completes the evaluation.\n", i_bootstrap);
		}
	}
	
	/**
	 * if data has N cases, sample N cases at random without replacement.
	 * @param <T>
	 * 
	 * @param N numbers of cases
	 * 
	 * @return The training set and the test set are obtained by taking the remaining number after N times of value and de-duplication.
	 */
	public <T> void oneRunBootstrapping(int seed, int N) {
		Random random = new Random(seed);
		
		ArrayList<Integer> list_train = new ArrayList<Integer>();
		train = new Instances(data,0);
		test = new Instances(data,0);
		
		for (int i = 0; i < N; i++) {
			int index = random.nextInt(N);
			list_train.add(index);
			train.add(data.get(index));
		}


		Set<Integer> set_raw = new HashSet<Integer>(list_raw);

		Set<Integer> set_train = new HashSet<Integer>(list_train);

		set_raw.removeAll(set_train);
		Iterator<Integer> it = set_raw.iterator();
		while (it.hasNext()) {
			Integer index = it.next();
			test.add(data.get(index));
		}
	}
	
	public DataSet changeToDataset(DataSet dataSet, double[][] data_normalization) {
		
		int lenInstances_data = data_normalization.length;
		int temp_inputNeuronsCount = data_normalization[0].length - 1;
		
		
		for (int j = 0; j < lenInstances_data; j++) {
			double[] doubleArray_inputNeurons = Arrays.copyOfRange(data_normalization[j], 0,
					temp_inputNeuronsCount);
			double[] doubleArray_outputNeurons = Arrays.copyOfRange(data_normalization[j], temp_inputNeuronsCount,
					temp_inputNeuronsCount + 1);	
			dataSet.addRow(new DataSetRow(doubleArray_inputNeurons, doubleArray_outputNeurons));
		}
		return dataSet;
	}
	
	// Bind the output of testSet to predictedResults and store them in an array.
	public double[][] combinePredictedActual(double[] predictedResults, double[][] data_normalization) {
		
		int lenInstances_test = data_normalization.length;
		int temp_outNeuronsCount = data_normalization[0].length - 1;
		
		double[][] predictedActual = new double[lenInstances_test][2];
		
		for (int j = 0; j < lenInstances_test; j++) {
			predictedActual[j][0] = predictedResults[j];
			predictedActual[j][1] = data_normalization[j][temp_outNeuronsCount];
		}
		
		return predictedActual;
	}
	
	// Bind the output of testSet to predictedResults and store them in an array.
	public double[][] combinePredictedActual_pagerank(double[] predictedResults, double[] actualResults) {
		
		int lenInstances_test = predictedResults.length;
		
		double[][] predictedActual = new double[lenInstances_test][2];
		
		for (int j = 0; j < lenInstances_test; j++) {
			predictedActual[j][0] = predictedResults[j];
			predictedActual[j][1] = actualResults[j];
		}
		
		return predictedActual;
	}
}
