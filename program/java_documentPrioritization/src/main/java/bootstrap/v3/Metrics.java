package bootstrap.v3;

import java.util.ArrayList;

public class Metrics {
	//evaluating indicator of ranking effectiveness
	double[][] accuracy_all_ranking = null;
	double[][] precision_all_ranking = null;
	double[][] recall_all_ranking = null;
	double[][] F1_all_ranking = null;
	double[] AUC_ROC_all = null;
	double[] AUC_PR_all = null;
	
	//evaluating indicator of classification effectiveness
//	double[] accuracy_all_binary = null;
	double[] precision_all_binary = null;
	double[] recall_all_binary = null;
	double[] F1_all_binary = null;
	
	ArrayList<Double[]> initialWeights = null;
	ArrayList<Double[]> finalWeights = null;
	
	int numTopK = 0;

	public Metrics() {
		int runs = 10;
		int folds = 10;
		numTopK = 10;
		int len = runs * folds;

		accuracy_all_ranking = new double[numTopK][len];
		precision_all_ranking = new double[numTopK][len];
		recall_all_ranking = new double[numTopK][len];
		F1_all_ranking = new double[numTopK][len];
		AUC_ROC_all = new double[len];
		AUC_PR_all = new double[len];
		
//		accuracy_all_binary = new double[len];
		precision_all_binary = new double[len];
		recall_all_binary = new double[len];
		F1_all_binary = new double[len];
		
		initialWeights = new ArrayList<Double[]>(len);
		finalWeights = new ArrayList<Double[]>(len);
		
	}

	public Metrics(int runs, int folds, int in_numTopK) {
		int len = runs * folds;
		numTopK = in_numTopK;

		accuracy_all_ranking = new double[numTopK][len];
		precision_all_ranking = new double[numTopK][len];
		recall_all_ranking = new double[numTopK][len];
		F1_all_ranking = new double[numTopK][len];
		AUC_ROC_all = new double[len];
		AUC_PR_all = new double[len];
		
//		accuracy_all_binary = new double[len];
		precision_all_binary = new double[len];
		recall_all_binary = new double[len];
		F1_all_binary = new double[len];

		initialWeights = new ArrayList<Double[]>(len);
		finalWeights = new ArrayList<Double[]>(len);
		
	}
	
	public Metrics(int len, int in_numTopK) {
//		int len = runs * folds;
		numTopK = in_numTopK;

		accuracy_all_ranking = new double[numTopK][len];
		precision_all_ranking = new double[numTopK][len];
		recall_all_ranking = new double[numTopK][len];
		F1_all_ranking = new double[numTopK][len];
		AUC_ROC_all = new double[len];
		AUC_PR_all = new double[len];
		
//		accuracy_all_binary = new double[len];
		precision_all_binary = new double[len];
		recall_all_binary = new double[len];
		F1_all_binary = new double[len];

		initialWeights = new ArrayList<Double[]>(len);
		finalWeights = new ArrayList<Double[]>(len);
		
	}
	
	public void calculateMetrics_overall(double[][] predictedActual, double threshold) {
		int i_runs = 0;
		int i_folds = 0;
		calculateMetrics_ranking(predictedActual, i_runs, i_folds);
		calculateMetrics_binary(predictedActual, i_runs, i_folds, threshold);
	}
	
	public void calculateMetrics_overall_PageRank(double[][] predictedActual, double threshold) {
		int i_runs = 0;
		int i_folds = 0;
		calculateMetrics_ranking(predictedActual, i_runs, i_folds);
		calculateMetrics_binary_PageRank(predictedActual, i_runs, i_folds, threshold);
	}
	
	
	public void calculateMetrics_PageRank(double[][] predictedActual, int i_runs, int i_folds, double threshold) {
//		calculateMetrics_ranking(predictedActual, i_runs, i_folds);
		calculateMetrics_binary_PageRank(predictedActual, i_runs, i_folds, threshold);
	}
	
	public void calculateMetrics(double[][] predictedActual, int i_runs, int i_folds, double threshold) {
//		calculateMetrics_ranking(predictedActual, i_runs, i_folds);
		calculateMetrics_binary(predictedActual, i_runs, i_folds, threshold);
	}
	
	public void calculateMetrics_ranking(double[][] predictedActual, int i_runs, int i_folds) {
		
		for (int i_topK = 5; i_topK <= 5 * numTopK; i_topK += 5) {
			calculateAccPreRecF1_ranking(predictedActual, i_runs, i_folds, i_topK);
		}

	}
	
	public void calculateMetrics_binary(double[][] predictedActual, int i_runs, int i_folds, double threshold) {
		
		int len_rows = predictedActual.length;
		for (int i = 0; i < len_rows; i++) {
			if (predictedActual[i][0] >= threshold) {
				predictedActual[i][0] = 1;
			}
			else {
				predictedActual[i][0] = 0;
			}
		}
		calculateAccPreRecF1_binary(predictedActual, i_runs, i_folds);
	}
	
	// Calculating accuracy, precision, recall and F1
	public void calculateMetrics_binary_PageRank(double[][] predictedActual, int i_runs, int i_folds, double threshold) {
//			double k = i_topK * 1.0 / 100;
//			int i_Kth = (i_topK - 5) / 5;
		double k = threshold;
		


		
		int len_oneFold = predictedActual.length;
		// TP+FN
		int len_oneFold_1 = 0;
		for (int i = 0; i < len_oneFold; i++) {
			if (predictedActual[i][1] == 1) {
				len_oneFold_1++;
			}
		}
		
		// TP+FP: the amount of important class files in top k%
		int len_k = (int) Math.round(len_oneFold * k);
		
		

		
		int len_k_1_TP = 0;
		for (int i = 0; i < len_k; i++) {
			if (predictedActual[i][1] == 1) {
				len_k_1_TP++;
			}
		}

//		int len_afterK_0_TN = 0;
//		for (int i = len_k; i < len_oneFold; i++) {
//			if (predictedActual[i][1] == 0) {
//				len_afterK_0_TN++;
//			}
//		}
//		double accuracy = (len_k_1_TP + len_afterK_0_TN) * 1.0 / len_oneFold;
		double precision = 0;
		if (len_k != 0) {
			precision = len_k_1_TP * 1.0 / len_k;
		}
		double recall = 0;
		if (len_oneFold_1 != 0) {
			recall = len_k_1_TP * 1.0 / len_oneFold_1;
		}
		double F1 = 0;
		if ((precision + recall) != 0) {
			F1 = 2 * precision * recall * 1.0 / (precision + recall);
		}
		int i_temp = i_folds + i_runs * 10;
		precision_all_binary[i_temp] = precision;
		recall_all_binary[i_temp] = recall;
		F1_all_binary[i_temp] = F1;	
	}
	
	// Calculating accuracy, precision, recall and F1
	public void calculateAccPreRecF1_binary(double[][] predictedActual, int i_runs, int i_folds) {

		
		
		int len_oneFold = predictedActual.length;
		// TP+FN
		int len_oneFold_1 = 0;
		for (int i = 0; i < len_oneFold; i++) {
			if (predictedActual[i][1] == 1) {
				len_oneFold_1++;
			}
		}
		// TP+FP
		int len_oneFold_prediction_1 = 0;
		for (int i = 0; i < len_oneFold; i++) {
			if (predictedActual[i][0] == 1) {
				len_oneFold_prediction_1++;
			}
		}
		// TP和TN
		int len_TP = 0;
//		int len_TN = 0;
		for (int i = 0; i < len_oneFold; i++) {
			if (predictedActual[i][0] == predictedActual[i][1] && predictedActual[i][1] == 1) {
				len_TP++;
			}
//			else if (predictedActual[i][0] == predictedActual[i][1] && predictedActual[i][1] == 0) {
//				len_TN++;
//			}
		}
//		double accuracy = (len_TP + len_TN) * 1.0 / len_oneFold;
		double precision = 0;
		if (len_oneFold_prediction_1 != 0) {
			precision = len_TP * 1.0 / len_oneFold_prediction_1;
		}
		double recall = 0;
		if (len_oneFold_1 != 0) {
			recall = len_TP * 1.0 / len_oneFold_1;
		}
		double F1 = 0;
		if ((precision + recall) != 0) {
			F1 = 2 * precision * recall * 1.0 / (precision + recall);
		}
		int i_temp = i_folds + i_runs * 10;
//		accuracy_all_binary[i_temp] = accuracy;
		precision_all_binary[i_temp] = precision;
		recall_all_binary[i_temp] = recall;
		F1_all_binary[i_temp] = F1;
	}

	// Calculating accuracy, precision, recall and F1
	public void calculateAccPreRecF1_ranking(double[][] predictedActual, int i_runs, int i_folds, int i_topK) {
//		for (int i_topK = 5; i_topK <= 5 * numTopK; i_topK += 5) {
			double k = i_topK * 1.0 / 100;
			int i_Kth = (i_topK - 5) / 5;


			int len_oneFold = predictedActual.length;
			// TP+FN
			int len_oneFold_1 = 0;
			for (int i = 0; i < len_oneFold; i++) {
				if (predictedActual[i][1] == 1) {
					len_oneFold_1++;
				}
			}
			

			// TP+FP: the amount of important class files in top k%
			int len_k = (int) Math.round(len_oneFold * k);

			

			int len_k_1_TP = 0;
			for (int i = 0; i < len_k; i++) {
				if (predictedActual[i][1] == 1) {
					len_k_1_TP++;
				}
			}
			
			int len_afterK_0_TN = 0;
			for (int i = len_k; i < len_oneFold; i++) {
				if (predictedActual[i][1] == 0) {
					len_afterK_0_TN++;
				}
			}
			double accuracy = (len_k_1_TP + len_afterK_0_TN) * 1.0 / len_oneFold;
			double precision = 0;
			if (len_k != 0) {
				precision = len_k_1_TP * 1.0 / len_k;
			}
			double recall = 0;
			if (len_oneFold_1 != 0) {
				recall = len_k_1_TP * 1.0 / len_oneFold_1;
			}
			double F1 = 0;
			if ((precision + recall) != 0) {
				F1 = 2 * precision * recall * 1.0 / (precision + recall);
			}
			int i_temp = i_folds + i_runs * 10;
			accuracy_all_ranking[i_Kth][i_temp] = accuracy;
			precision_all_ranking[i_Kth][i_temp] = precision;
			recall_all_ranking[i_Kth][i_temp] = recall;
			F1_all_ranking[i_Kth][i_temp] = F1;			
//		}
	}
}
