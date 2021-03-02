package bootstrap.v3;

import java.io.File;

public class Main_bootstrap {

	public static void main(String[] args) throws Exception {
		// project name
		String[] array_projectNames = { "nanoxml-2.2.1", "jexcelapi-2.6.12", "jgrapht-0.9.1"};
//		String[] array_projectNames = { "ant-1.6.1", "argouml-0.9.5", "jedit-5.1.0", "jhotdraw-6.0b.1", "jmeter-2.0.1", "wro4j-1.6.3" };
		// project style
		String project_style = "(documentation)project";
//		String project_style = "(keyClass)project";
		// File Path
		String dataFilePath_common = "D:\\workspace\\DataFolder\\initialArffData\\";
//		String dataFilePath_common = "D:\\workspace\\DataFolder\\initialArffData\\discussion\\7.2\\";
		// model Name
//		String[] modelNames = { };//7.2,7.3
		String[] modelNames = { "SCM", "VSM" };//6,7.4
		String[] modelNames_unsupervised = { "PageRank" };//6,7.4
//		String[] modelNames_unsupervised = { "PageRank_allClasses" };//7.1
//		String[] modelNames_unsupervised = { "PageRank", "PageRank_W", "PageRank_R", "PageRank_W+R" };//7.2
//		String[] modelNames_unsupervised = { "PageRank", "AllDependency", };//7.3
		
		// saved Csv file Header
		String savedCsvHeader_ranking_A_P_R_F = "accuracy" + "," + "precision" + "," + "recall" + "," + "F1";

		int runs = 100;
		int numTopK = 10;
		Metrics metrics = null;// precision,recall,F1
		WriteMetrics WM = new WriteMetrics();// Tools for writing

		double threshold = 0.5;// threshold
		
		//Added a Timing Array
		double[][] runTimes = new double[array_projectNames.length][modelNames.length*2+modelNames_unsupervised.length];
		String[] projectNames_times = new String[array_projectNames.length];
		int i_project = 0;
		int i_column = 0;
		
		//Timing Variables
		long begintime = 0;
		long endtime = 0;
		double costTime = 0;
		
		// Saved File Path
		String savedFilePath_times = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + "ranking times of each project.csv";
		String savedCsvHeader_ranking_times = "projectName" + "," + "SCM" + "," + "SCM_FS" + "," + "VSM" + "," + "VSM_FS" + "," + "PageRank";
//		String savedFilePath_times = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + "(rebalancing)ranking times of each project.csv";//7.4
//		String savedCsvHeader_ranking_times = "projectName" + "," + "SCM_R" + "," + "SCM_R_FS" + "," + "VSM_R" + "," + "VSM_R_FS" + "," + "PageRank";//7.4
		
		for (String projectName : array_projectNames)
		{
			projectNames_times[i_project] = projectName;
			i_column = 0;
			// Saved File Path
			String savedFilePath = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + projectName
					+ "\\bootstrap_v2.0\\";
//			String savedFilePath = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + projectName
//					+ "\\discussion\\7.1\\";
//			String savedFilePath = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + projectName
//					+ "\\discussion\\7.2\\";
//			String savedFilePath = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + projectName
//					+ "\\discussion\\7.3\\";
//			String savedFilePath = "D:\\workspace\\DataFolder\\results\\" + project_style + "\\" + projectName
//					+ "\\discussion\\7.4\\";
			
			// This statement determines whether the directory exists.
			File directory_file = new File(savedFilePath);
			if (!directory_file.exists()) {
				directory_file.mkdirs();
			}
			
			// supervised methods
			for (String i_modelName : modelNames) {
				Bootstrap boot = new Bootstrap(dataFilePath_common + i_modelName + "\\" + project_style + "\\" + projectName + ".arff");
				
				metrics = new Metrics_bootstrap(runs, numTopK);// precision,recall,F1
				
				//Timing begins
				begintime = System.nanoTime();
				// run bootstrap, supervised methods without feature selection
				boot.runBootstrapping(metrics, runs, threshold);
				// run bootstrap, supervised methods with rebalancing but without feature selection
//				boot.runBootstrapping_rebalancing(metrics, runs, threshold);//7.4
				//Timing end
				endtime = System.nanoTime();
				costTime = (endtime - begintime)*1.0/1000000000;
				runTimes[i_project][i_column] = costTime;
				i_column++;
				
				WM.writeMetricsToCsv_ranking(metrics, savedFilePath + "ranking indicators(A,P,R,F) of " + i_modelName + ".csv",
						savedCsvHeader_ranking_A_P_R_F, numTopK);
//				WM.writeMetricsToCsv_ranking(metrics, savedFilePath + "ranking indicators(A,P,R,F) of " + i_modelName + "_rebalancing.csv",
//						savedCsvHeader_ranking_A_P_R_F, numTopK);//7.4
				
				/*===feature selection===*/
				// re-new one metrics class
				metrics = new Metrics_bootstrap(runs, numTopK);// precision,recall,F1
				
				//Timing begins
				begintime = System.nanoTime();
				// run bootstrap, supervised methods with feature selection
				boot.runBootstrapping_featureSelection(metrics, runs, threshold);
				// run bootstrap, supervised methods with rebalancing and feature selection
//				boot.runBootstrapping_rebalancing_featureSelection(metrics, runs, threshold);//7.4
				//Timing end
				endtime = System.nanoTime();
				costTime = (endtime - begintime)*1.0/1000000000;
				runTimes[i_project][i_column] = costTime;
				i_column++;
				
				WM.writeMetricsToCsv_ranking(metrics, savedFilePath + "ranking indicators(A,P,R,F) of " + i_modelName + "_featureSelection.csv",
						savedCsvHeader_ranking_A_P_R_F, numTopK);
//				WM.writeMetricsToCsv_ranking(metrics, savedFilePath + "ranking indicators(A,P,R,F) of " + i_modelName + "_rebalancing_featureSelection.csv",
//						savedCsvHeader_ranking_A_P_R_F, numTopK);//7.4
			}
			
			// unsupervised methods
			for (String i_modelName : modelNames_unsupervised)
			{
				Bootstrap boot = new Bootstrap(dataFilePath_common + i_modelName + "\\" + project_style + "\\" + projectName + ".arff");
				
				metrics = new Metrics_bootstrap(runs, numTopK);// precision,recall,F1
				
				//Timing begins
				begintime = System.nanoTime();
				// run bootstrap
				boot.runBootstrapping_pagerank(metrics, runs, threshold);
				//Timing end
				endtime = System.nanoTime();
				costTime = (endtime - begintime)*1.0/1000000000;
				runTimes[i_project][i_column] = costTime;
				i_column++;
				
				WM.writeMetricsToCsv_ranking(metrics, savedFilePath + "ranking indicators(A,P,R,F) of " + i_modelName + ".csv",
						savedCsvHeader_ranking_A_P_R_F, numTopK);
			}
			i_project++;
		}
		WM.writeTimesToCsv(projectNames_times, runTimes, savedFilePath_times, savedCsvHeader_ranking_times);
	}
}
