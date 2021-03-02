import os
import csv
import pandas as pd

# 得到数据集字典和文件名
def getDatasetDicFileName(filename_dataset):
    #读取目标项目数据集csv文件
    dataFrame_dataset = pd.read_csv(filename_dataset, usecols=['Class', 'isImportant']);
    #只得到第一列数据，即类全名
    list_column1 = dataFrame_dataset['Class'];
    print("第一列：",list_column1);
    #创建第一列类的字典
    dic_dataset = dict.fromkeys(list_column1, 0);#初始化字典
    value=1;#类名对应的value值
    fileName_dataset=[];#类名列表，不包含路径名
    for key in dic_dataset:
        dic_dataset[key] = value;#给key对应的value赋值，按序赋值
        fileName_dataset.append(os.path.basename(key));#类名
        value+=1;
    print("第一列字典：",dic_dataset);
    print("类名列表：",fileName_dataset);
    return (dic_dataset,fileName_dataset,dataFrame_dataset);

# 数据过滤
def dataFiltering(dic_dataset,fileName_dataset,filename_CA):
    # 读从.udb读取的csv数据文件
    csvFile_CA = open(filename_CA,"r");
    reader = csv.reader(csvFile_CA);# 返回的是迭代类型
    #得到所有列数据
    list_columns = [row for row in reader];#reader里面的东西只会读一次，读过后就删掉，是为了内存考虑
    csvFile_CA.close();

    # 过滤.udb的csv文件数据
    #创建以数据集中类的顺序对应的矩阵
    matrix = [[0 for i in range(len(fileName_dataset))] for row in range(len(fileName_dataset))];
    str_sep= "-->";
    str_suffix=".java";
    #k=1;
    for i in range(0, len(list_columns)):
#         list_columns[i][0] =list_columns[i][0].replace("\\", "/");#需要转换，不然会因分隔符不同匹配不上
#         list_columns[i][1] =list_columns[i][1].replace("\\", "/");
        str_column_one = list_columns[i][0];#第一行第一列，即文件全名
        delimiter = str_column_one.find(str_sep);
        key_fullName_column_one = str_column_one[:delimiter];#得到第一列数据-->之前的文件全名，作为key
    #     print (key_column1);
        if dic_dataset.__contains__(key_fullName_column_one):#判断源文件名是否是数据集中出现的
    #         print(dic_dataset[key_fullName_column_one]);
            str_column_two = list_columns[i][1];#第一行第二列，即依赖的类名
            delimiter = str_column_two.find(str_sep);#找到"-->"的位置
            key_fullName_column_two = str_column_two[:delimiter];#截取依赖的类全名
            delimiter += len(str_sep); #开始位置加分隔字符串长度
            fileName_column_two = str_column_two[delimiter:];#截取依赖的"-->"后面指向的类名，因为指向的不一定是类名
            fileName_column_two +=str_suffix;#添加.java后缀
    #         k+=1;
            #第一步过滤，过滤非类名，以及非数据集文件名列表中的类
            if fileName_column_two in fileName_dataset:
    #             print(k,   fileName_column_two);
                #进一步过滤，过滤该类对应的全名不在数据集字典中，该过滤是考虑到类名相同包名不同的情况
                #即过滤相同类名但不同包名的不在数据集字典中的类
                if dic_dataset.__contains__(key_fullName_column_two):
                    str_column_three = list_columns[i][2];#得到第三列的数字
                    col = dic_dataset[key_fullName_column_one];
                    row = dic_dataset[key_fullName_column_two];
                    matrix[row-1][col-1] += int(str_column_three);#得到的数字是字符串类型需要转成整型存入矩阵
    return matrix;






