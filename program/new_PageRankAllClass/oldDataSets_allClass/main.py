import new_PageRankAllClass.getFileList as GFL
import new_PageRankAllClass.oldDataSets_allClass.csvDataFiltering as DF;
import new_PageRankAllClass.oldDataSets_allClass.PageRank as PR;
# import new_PageRankAllClass.IntersectionOfPRandScore as IRS
import pandas as pd

if __name__ == "__main__":
    
    project_style = "(documentation)project";
#     project_style = "(keyClass)project";
    
    pathName_common = "D:/workspace/DataFolder/FirstPaper_regular/pagerank/dependence/"+project_style;
    pathName_codeCommon = "D:/workspace/DataFolder/FirstPaper_regular/projectCode/"+project_style+"/";
    
    projectName = "nanoxml-2.2.1";
#     projectName = "jexcelapi-2.6.12";
#     projectName = "jgrapht-0.9.1";
    
    filename_dataset = pathName_common + "/originalCsv/" + projectName + ".csv";
    
    pathName_project = pathName_codeCommon + "nanoxml-2.2.1/Sources";
#     pathName_project = pathName_codeCommon + "jexcelapi-2.6.12/src";
#     pathName_project = pathName_codeCommon + "jgrapht-0.9.1/source/jgrapht-core/src/main/java";
    
    filename_CA = pathName_common+"/"+projectName+"/v1/"+"CAInteractionMatrix.csv";
    filename_CM = pathName_common+"/"+projectName+"/v1/"+"CMInteractionMatrix.csv";
    filename_MM = pathName_common+"/"+projectName+"/v1/"+"MMInteractionMatrix.csv";
    filename_Inheritance = pathName_common+"/"+projectName+"/v1/"+"CIInheritanceMatrix.csv";
    
    #存储路径
    savedFilename_PageRank = pathName_common+"/"+projectName+"/v1/"+"PageRank_allClasses.csv";
    savedFilename_PageRank_all_scored = pathName_common+"/"+projectName+"/v1/"+"PageRank_allClasses_scored.csv";
    
#     savedPath_IntersectionOfPRandScore = pathName_common+"/allClass/"+projectName+"/"+"intersection of PR and Score "+projectName+".csv";#???

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
    
    # 经过数据过滤，得到4种依赖关系矩阵
    #matrix矩阵中行列元素顺序对应于fileList中的类
    matrix_CA = DF.dataFiltering(dic_fileList,fileList_fileName,filename_CA);
    matrix_CM = DF.dataFiltering(dic_fileList,fileList_fileName,filename_CM);
    matrix_MM = DF.dataFiltering(dic_fileList,fileList_fileName,filename_MM);
    matrix_Inheritance = DF.dataFiltering(dic_fileList,fileList_fileName,filename_Inheritance);
#     print(matrix_CA);
#     print(matrix_CM);
#     print(matrix_MM);
#     print(matrix_Inheritance);

    # 得到转移概率矩阵
    array_matrix_total = PR.getTransitionProbabilityMatrix(matrix_CA,matrix_CM,matrix_MM,matrix_Inheritance);
    
    #===计算PageRank，并存储===#
    vector_page_ranks = PR.pageRank(dic_fileList,array_matrix_total);
    # 创建一个 DataFrame，值为字典中的类
    list_keys= list(dic_fileList.keys())
    data_dic_fileList = {'File Name':list_keys};
    dataFrame_fileList = pd.DataFrame(data_dic_fileList)
    dataFrame_fileList['PageRank']=vector_page_ranks;
    
#     #判断哪个类是重要的类，加上标签，重要的类是1，不重要的是0
#     ADD.addCategoryLabel(dataFrame_fileList,keyClass_list);
    
    #按降序排序
#     dataFrame_fileList.sort_values(by = 'PageRank',axis = 0,ascending = False,inplace=True);
    #在写入csv之前有可选步骤，要不要只存数据集类对应的pagerank
    dataFrame_fileList.to_csv(savedFilename_PageRank);#写入csv文件
#     print("存储完毕");
    #===end计算PageRank，并存储===#
    
    #判断dataFrame_fileList类是否在数据集中，在数据集中时数据集存入对应pagerank
    df_intersection = DF.judgeIntersection(filename_dataset,dataFrame_fileList)
    #求与用户打分类的交集
    df_intersection.to_csv(savedFilename_PageRank_all_scored);#写入csv文件
    print("存储完毕");
    
    # 在不同top k下PageRank命中几个，PageRank得分排名和用户打分排名越接近效果越好
    # 判断top k取不同值时(5%-50%)，PageRank得分排名和用户打分排名，前k%中相同的有几个
#     top_k = [i/100 for i in range(5, 55, 5)];
#     result_inter,result_percentage = IRS.getResult_k(df_intersection, top_k);
#     print(result_inter,result_percentage);
#     df_KSI = pd.DataFrame({'top_k':top_k,'num_intersection':result_inter,'percentage':result_percentage});
#     df_KSI.to_csv(savedPath_IntersectionOfPRandScore);#写入csv文件

    

    
    
    