import os

# 增加分类标签
def addCategoryLabel(dataFrame_fileList,keyClass_list):
    dataFrame_fileList['isImportant'] = 0;
    class_column = dataFrame_fileList['Class'];
    i_class = 0;
    for str_i_class_column in class_column:
        className = os.path.basename(str_i_class_column);
        if className in keyClass_list:
            dataFrame_fileList.ix[i_class,'isImportant'] = 1;
        i_class+=1;

# if __name__ == "__main__":