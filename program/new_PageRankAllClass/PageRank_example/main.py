import new_PageRankAllClass.PageRank_example.csvDataFiltering as DF;
import new_PageRankAllClass.PageRank_example.PageRank as PR;

if __name__ == "__main__":
    
    pathName_common = "D:/yourPath/pagerank/test/v1/";
    
    projectName = "PageRank_example";
    
    filename_dataset = pathName_common+"PageRank_example_class.csv";
    
    filename_CA = pathName_common+"CAInteractionMatrix.csv";
    filename_CM = pathName_common+"CMInteractionMatrix.csv";
    filename_MM = pathName_common+"MMInteractionMatrix.csv";
    filename_Inheritance = pathName_common+"CIInheritanceMatrix.csv";
    savedFilename_PageRank = pathName_common+"PageRank.csv";

    (dic_dataset,fileName_dataset,dataFrame_dataset) = DF.getDatasetDicFileName(filename_dataset);
    
    # 经过数据过滤，得到4种依赖关系矩阵
    #matrix矩阵中行列元素顺序对应于数据集中的类
    matrix_CA = DF.dataFiltering(dic_dataset,fileName_dataset,filename_CA);
    matrix_CM = DF.dataFiltering(dic_dataset,fileName_dataset,filename_CM);
    matrix_MM = DF.dataFiltering(dic_dataset,fileName_dataset,filename_MM);
    matrix_Inheritance = DF.dataFiltering(dic_dataset,fileName_dataset,filename_Inheritance);
    print(matrix_CA);
    print(matrix_CM);
    print(matrix_MM);
    print(matrix_Inheritance);

    # 得到转移概率矩阵。#如果要给不同的依赖类型加权重的话，可以在上一步加或在这一步加。#权重的不同会影响结果，是否加反向依赖也会影响结果。
    array_matrix_total = PR.getTransitionProbabilityMatrix(matrix_CA,matrix_CM,matrix_MM,matrix_Inheritance);
    print(array_matrix_total);
    
    # 计算PageRank
    vector_page_ranks = PR.pageRank(dic_dataset,array_matrix_total);#解法都一样，不一样的是转移概率矩阵构建
    print(vector_page_ranks);
    dataFrame_dataset['PageRank']=vector_page_ranks;
    # 把PageRank列放到class列后面，这一步可以不要
    col_name = list(dataFrame_dataset);
#     col_name.insert(1,col_name.pop(col_name.index('PageRank')))
#     dataFrame_dataset=dataFrame_dataset.ix[:,col_name]
    dataFrame_dataset.to_csv(savedFilename_PageRank);#写入csv文件
    
    
    
    