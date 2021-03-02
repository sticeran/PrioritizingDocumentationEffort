package bootstrap.v3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WriteMetrics {


	public void writeMetricsToCsv_ranking(Metrics metrics, String filePath, String fileHeader, int numTopK) {

		String savedCsvHeader = "topK,"+fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");

			
			int rowLen =  metrics.accuracy_all_ranking[0].length;
			double initial_topK = 5;
			for (int i_Kth = 0; i_Kth < numTopK; i_Kth++) {
				for (int i = 0; i < rowLen; i++) {

					double temp_topK = initial_topK * (i_Kth+1) / 100;
					strBuilder.append(temp_topK);
					strBuilder.append(',');
					strBuilder.append(metrics.accuracy_all_ranking[i_Kth][i]);
					strBuilder.append(',');
					strBuilder.append(metrics.precision_all_ranking[i_Kth][i]);
					strBuilder.append(',');
					strBuilder.append(metrics.recall_all_ranking[i_Kth][i]);
					strBuilder.append(',');
					strBuilder.append(metrics.F1_all_ranking[i_Kth][i]);
					strBuilder.append("\r\n");

				}
			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writeTimesToCsv(String[] projectNames_times, double[][] runTimes, String filePath, String fileHeader) {

		StringBuilder strBuilder = new StringBuilder();
		
		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(fileHeader);
			strBuilder.append("\r\n");
			
			int rowLen =  runTimes.length;
			int colLen =  runTimes[0].length;
			for (int i_project = 0; i_project < rowLen; i_project++)
			{
				strBuilder.append(projectNames_times[i_project]);
				strBuilder.append(',');
				for (int i = 0; i < colLen; i++) {
					strBuilder.append(runTimes[i_project][i]);
					strBuilder.append(',');
				}
				strBuilder.append("\r\n");
			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writeMetricsToCsv_binary(Metrics metrics, String filePath, String fileHeader) {

		String savedCsvHeader = fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");

			
			int rowLen =  metrics.precision_all_ranking[0].length;
			for (int i = 0; i < rowLen; i++) {

				strBuilder.append(metrics.precision_all_binary[i]);
				strBuilder.append(',');
				strBuilder.append(metrics.recall_all_binary[i]);
				strBuilder.append(',');
				strBuilder.append(metrics.F1_all_binary[i]);

				strBuilder.append("\r\n");

			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writeMetricsToCsv_AUCROC_AUCPR(Metrics metrics, String filePath, String fileHeader) {

		String savedCsvHeader = fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");

			
			int rowLen =  metrics.accuracy_all_ranking[0].length;
			for (int i = 0; i < rowLen; i++) {

				strBuilder.append(metrics.AUC_ROC_all[i]);
				strBuilder.append(',');
				strBuilder.append(metrics.AUC_PR_all[i]);
				strBuilder.append("\r\n");

			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writePredictedResultsToCsv(double[][] predictedActual, String filePath, String fileHeader) {

		String savedCsvHeader = fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");

			
			int rowLen =  predictedActual.length;
			for (int i = 0; i < rowLen; i++) {

				strBuilder.append(predictedActual[i][0]);
				strBuilder.append(',');
				strBuilder.append(predictedActual[i][1]);
				strBuilder.append("\r\n");

			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writeEveryFoldThresholdPercentageToCsv(double[] everyFold_threshold_percentage, String filePath, String fileHeader) {

		String savedCsvHeader = fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");

			
			int rowLen =  everyFold_threshold_percentage.length;
			for (int i = 0; i < rowLen; i++) {

				strBuilder.append(everyFold_threshold_percentage[i]);
				strBuilder.append("\r\n");

			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void writeMetricsToCsv_neuralNetWeights(Metrics metrics, String filePath, String fileHeader) {

		String savedCsvHeader = fileHeader;
		StringBuilder strBuilder = new StringBuilder();
		

		File savedFile = new File(filePath);
		if (!savedFile.exists()) {
			try {
				savedFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(savedFile));
			strBuilder.append(savedCsvHeader);
			strBuilder.append("\r\n");


			int numArray = metrics.initialWeights.size();
			for (int i_numArray=0;i_numArray<numArray;i_numArray++) {
				Double[] initialWeights = metrics.initialWeights.get(i_numArray);
				Double[] finalWeights = metrics.finalWeights.get(i_numArray);
				int rowLen =  initialWeights.length;
				for (int i = 0; i < rowLen; i++) {

					strBuilder.append(initialWeights[i]);
					strBuilder.append(',');
					strBuilder.append(finalWeights[i]);
					strBuilder.append("\r\n");

				}
			}
			writer.write(strBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
