package bootstrap.v3;

public class Metrics_bootstrap extends Metrics {
		
//	double[][] accuracy_all_ranking = null;
//	double[][] precision_all_ranking = null;
//	double[][] recall_all_ranking = null;
//	double[][] F1_all_ranking = null;
//	double[] AUC_ROC_all = null;
//	double[] AUC_PR_all = null;	
	
	
//	double[] precision_all_binary = null;
//	double[] recall_all_binary = null;
//	double[] F1_all_binary = null;

//	int numTopK = 0;
//	LaunchRserve LR = null;

	public Metrics_bootstrap() {
		int runs = 10;
//		int folds = 10;
		numTopK = 10;
//		int len = runs * folds;
		int len = runs;

		accuracy_all_ranking = new double[numTopK][len];
		precision_all_ranking = new double[numTopK][len];
		recall_all_ranking = new double[numTopK][len];
		F1_all_ranking = new double[numTopK][len];
		AUC_ROC_all = new double[len];
		AUC_PR_all = new double[len];
		
		
		precision_all_binary = new double[len];
		recall_all_binary = new double[len];
		F1_all_binary = new double[len];

	}

	public Metrics_bootstrap(int runs, int in_numTopK) {
//		int len = runs * folds;
		int len = runs;
		numTopK = in_numTopK;

		accuracy_all_ranking = new double[numTopK][len];
		precision_all_ranking = new double[numTopK][len];
		recall_all_ranking = new double[numTopK][len];
		F1_all_ranking = new double[numTopK][len];
		AUC_ROC_all = new double[len];
		AUC_PR_all = new double[len];
		
		
		precision_all_binary = new double[len];
		recall_all_binary = new double[len];
		F1_all_binary = new double[len];

	}
	
	public void calculateMetrics_bootstrap(double[][] predictedActual, int i_runs, double threshold) {
		calculateMetrics_ranking_bootstrap(predictedActual, i_runs);
//		calculateMAPMRR(predictedActual, i_runs, i_folds);
//		calculateAUC_ROCandPR(predictedActual, i_runs, i_folds);
//		calculateMetrics_binary_10folds(predictedActual, i_runs, threshold);
	}

	public void calculateMetrics_ranking_bootstrap(double[][] predictedActual, int i_runs) {
		
		for (int i_topK = 5; i_topK <= 5 * numTopK; i_topK += 5) {
			calculateAccPreRecF1_ranking_bootstrap(predictedActual, i_runs, i_topK);
		}

	}

	// Calculating accuracy, precision, recall and F1
	public void calculateAccPreRecF1_ranking_bootstrap(double[][] predictedActual, int i_runs, int i_topK) {
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
//			int i_temp = i_folds + i_runs * 10;
			int i_temp = i_runs;
			accuracy_all_ranking[i_Kth][i_temp] = accuracy;
			precision_all_ranking[i_Kth][i_temp] = precision;
			recall_all_ranking[i_Kth][i_temp] = recall;
			F1_all_ranking[i_Kth][i_temp] = F1;			
//		}
	}
}
