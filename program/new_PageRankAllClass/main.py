import new_PageRankAllClass.getFileList as GFL
import new_PageRankAllClass.csvDataFiltering as DF;
import new_PageRankAllClass.PageRank as PR;
import new_PageRankAllClass.addCategoryLabel as ADD;
# import new_PageRankAllClass.IntersectionOfPRandScore as IRS
import pandas as pd
import time

if __name__ == "__main__":
    
    project_style = "(documentation)project";
#     project_style = "(keyClass)project";
    veisionName_pagerank = "/v1/";
    
    pathName_common = "D:/workspace/DataFolder/FirstPaper_regular/pagerank/dependence/"+project_style;
    pathName_codeCommon = "D:/workspace/DataFolder/FirstPaper_regular/projectCode_segment/"+project_style+"/";
    
    list_projectName = ["nanoxml-2.2.1","jexcelapi-2.6.12","jgrapht-0.9.1"];
    list_pathName_project = [pathName_codeCommon + "nanoxml-2.2.1/Sources",pathName_codeCommon + "jexcelapi-2.6.12/src",pathName_codeCommon + "jgrapht-0.9.1/source/jgrapht-core/src/main/java"];
    
#     list_projectName = ["ant-1.6.1","argouml-0.9.5","jedit-5.1.0","jhotdraw-6.0b.1","jmeter-2.0.1","wro4j-1.6.3"];
#     list_pathName_project = [pathName_codeCommon + "ant-1.6.1/src/main/org/apache/tools",pathName_codeCommon + "argouml-0.9.5/org/argouml",
#                              pathName_codeCommon + "jedit-5.1.0/org",pathName_codeCommon + "jhotdraw-6.0b.1/src/org/jhotdraw",
#                              pathName_codeCommon + "jmeter-2.0.1/src/core/org/apache",pathName_codeCommon + "wro4j-1.6.3/wro4j-core/src"];
    keyClass_list_1 = ['Project.java', 'Target.java', 'UnknownElement.java', 'RuntimeConfigurable.java', 'Task.java', 'IntrospectionHelper.java', 'ProjectHelper2.java', 'ProjectHelperImpl.java'];
    keyClass_list_2 = ['Designer.java', 'Critic.java', 'CrUML.java', 'ToDoItem.java', 'ToDoList.java', 'History.java', 'ControlMech.java', 'ProjectBrowser.java', 'Project.java', 'Wizard.java', 'Configuration.java', 'Argo.java'];
    keyClass_list_3 = ['jEdit.java', 'View.java', 'EditPane.java', 'Buffer.java', 'JEditTextArea.java', 'Log.java', 'EBMessage.java'];
    keyClass_list_4 = ['Figure.java', 'Drawing.java', 'DrawingView.java', 'DrawApplication.java', 'Tool.java', 'Handle.java', 'DrawingEditor.java', 'StandardDrawingView.java', 'CompositeFigure.java'];
    keyClass_list_5 = ['AbstractAction.java', 'JMeterEngine.java', 'JMeterTreeModel.java', 'JMeterThread.java', 'JMeterGUIComponent.java', 'Sampler.java', 'SampleResult.java', 'TestCompiler.java', 'TestElement.java', 'TestListener.java', 'TestPlan.java', 'TestPlanGui.java', 'ThreadGroup.java'];
    keyClass_list_6 = ['WroModel.java', 'WroModelFactory.java', 'Group.java', 'Resource.java', 'WroManager.java', 'WroManagerFactory.java', 'ResourcePreProcessor.java', 'ResourcePostProcessor.java', 'UriLocator.java', 'UriLocatorFactory.java', 'WroFilter.java', 'ResourceType.java'];
    list_keyClass_list = [keyClass_list_1,keyClass_list_2,keyClass_list_3,keyClass_list_4,keyClass_list_5,keyClass_list_6];
    
    savedFileName_times = pathName_common+"/"+"time.csv";
    df_times = pd.DataFrame(columns = ["projectName", "time"]) #创建一个空的dataframe
    i_project = 0;
    
#     projectName = "nanoxml-2.2.1";
#     projectName = "jexcelapi-2.6.12";
#     projectName = "jgrapht-0.9.1";
    
#     projectName = "ant-1.6.1";
#     projectName = "argouml-0.9.5";
#     projectName = "jedit-5.1.0";
#     projectName = "jhotdraw-6.0b.1";
#     projectName = "jmeter-2.0.1";
#     projectName = "wro4j-1.6.3";
    
#     pathName_project = pathName_codeCommon + "nanoxml-2.2.1/Sources";
#     pathName_project = pathName_codeCommon + "jexcelapi-2.6.12/src";
#     pathName_project = pathName_codeCommon + "jgrapht-0.9.1/source/jgrapht-core/src/main/java";
    
#     pathName_project = pathName_codeCommon + "ant-1.6.1/src/main/org/apache/tools";#加上org/apache/tools，不然和CA等数据集里的路径对不上
#     pathName_project = pathName_codeCommon + "argouml-0.9.5/org/argouml";#加上org/argouml，不然和CA等数据集里的路径对不上
#     pathName_project = pathName_codeCommon + "jedit-5.1.0/org";
#     pathName_project = pathName_codeCommon + "jhotdraw-6.0b.1/src/org/jhotdraw";#加上org/jhotdraw，不然和CA等数据集里的路径对不上
#     pathName_project = pathName_codeCommon + "jmeter-2.0.1/src/core/org/apache";#加上org/apache，不然和CA等数据集里的路径对不上
#     pathName_project = pathName_codeCommon + "wro4j-1.6.3/wro4j-core/src";#src，不然和CA等数据集里的路径对不上
    
    for projectName,pathName_project,keyClass_list in zip(list_projectName,list_pathName_project,list_keyClass_list):
     
        filename_CA = pathName_common+"/"+projectName+veisionName_pagerank+"CAInteractionMatrix.csv";
        filename_CM = pathName_common+"/"+projectName+veisionName_pagerank+"CMInteractionMatrix.csv";
        filename_MM = pathName_common+"/"+projectName+veisionName_pagerank+"MMInteractionMatrix.csv";
        filename_Inheritance = pathName_common+"/"+projectName+veisionName_pagerank+"CIInheritanceMatrix.csv";
        
        #存储路径
        savedFilename_PageRank = pathName_common+"/"+projectName+veisionName_pagerank+"PageRank.csv";
        
        #计时开始
        beginTime = time.perf_counter(); # 调用一次 perf_counter()，从计算机系统里随机选一个时间点A，计算其距离当前时间点B1有多少秒。当第二次调用该函数时，默认从第一次调用的时间点A算起，距离当前时间点B2有多少秒。两个函数取差，即实现从时间点B1到B2的计时功能。
        
        if project_style == "(keyClass)project":
            level = 1;#目录层级
            path_initial = pathName_project+"/";#在递归时需要计算减去，和csv文件路径名一致
            fileList = [];#储存读出的文件
            fileList_fileName = [];#存储不包括路径的文件名
            allFileNum=[0];
        
            #获取项目java文件列表
            GFL.getFileList_java(level, pathName_project, path_initial, fileList, fileList_fileName, allFileNum);#fileList_fileName里名字不对
            print ('总文件数 =', allFileNum);
            
            # 把文件列表转字典
            dic_fileList = DF.fileListToDic(fileList);
        elif project_style == "(documentation)project":
            filename_dataset = pathName_common + "/originalCsv/" + projectName + ".csv";
            dic_fileList,fileList_fileName,keyClass_list = DF.getDatasetDicFileName(filename_dataset);
        
        # 经过数据过滤，得到4种依赖关系矩阵
        #matrix矩阵中行列元素顺序对应于fileList中的类
        matrix_CA = DF.dataFiltering(dic_fileList,fileList_fileName,filename_CA);
        matrix_CM = DF.dataFiltering(dic_fileList,fileList_fileName,filename_CM);
        matrix_MM = DF.dataFiltering(dic_fileList,fileList_fileName,filename_MM);
        matrix_Inheritance = DF.dataFiltering(dic_fileList,fileList_fileName,filename_Inheritance);
    
        # 得到转移概率矩阵
        array_matrix_total = PR.getTransitionProbabilityMatrix(matrix_CA,matrix_CM,matrix_MM,matrix_Inheritance);
        
        #===计算PageRank，并存储===#
        vector_page_ranks = PR.pageRank(dic_fileList,array_matrix_total);
        # 创建一个 DataFrame，值为字典中的类
        list_keys= list(dic_fileList.keys())
        data_dic_fileList = {'Class':list_keys};
        dataFrame_fileList = pd.DataFrame(data_dic_fileList)
        dataFrame_fileList['PageRank']=vector_page_ranks;
        
        #判断哪个类是重要的类，加上标签，重要的类是1，不重要的是0
        ADD.addCategoryLabel(dataFrame_fileList,keyClass_list);
        
        #计时结束
        endTime = time.perf_counter();
        costTime = (endTime - beginTime);
        
        df_times.loc[i_project]=[projectName,costTime];
        i_project+=1;
        
        #按降序排序
    #     dataFrame_fileList.sort_values(by = 'PageRank',axis = 0,ascending = False,inplace=True);
        #在写入csv之前有可选步骤，要不要只存数据集类对应的pagerank
        dataFrame_fileList.to_csv(savedFilename_PageRank);#写入csv文件
        df_times.to_csv(savedFileName_times,index=False);#不保存行索引
        print("存储完毕");
        #===end计算PageRank，并存储===#
        
        
        
        
        
        
        
        
        
        