package bootstrap.v3;

import java.util.Arrays;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;

public class CalculatePerceptron {

	public MultiLayerPerceptron createMultiLayerPerceptron(int inputNeuronsCount,int outputNeuronsCount) {
		//TransferFunctionType.LINEAR
		
		MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputNeuronsCount,10,10,outputNeuronsCount);
		
		MomentumBackpropagation learningRule = (MomentumBackpropagation)neuralNet.getLearningRule();
		
//		learningRule.addListener(this);
		
		learningRule.setMaxIterations(1000);
		learningRule.setLearningRate(0.25);
		neuralNet.setLearningRule(learningRule);
//		//Randomizes connection weights for the whole network using specified
//		Random initialSeed = new Random(10);
//		neuralNet.randomizeWeights(initialSeed);
		
		return neuralNet;
	}
	
	public double[] testNeuralNetwork(MultiLayerPerceptron nnet, DataSet tset) {
		double[] allOutputs = new double[tset.size()];
		int i_tset = 0;
		for (DataSetRow dataRow : tset.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			allOutputs[i_tset++] = networkOutput[0];
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
		}
		return allOutputs;
	}

}