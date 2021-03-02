import math

def overlap(a,b,k):
    c=len(b)*k;
    c = math.ceil(c);
    a=a[:c];
    b=b[:c];
    intersection = list(set(a).intersection(set(b)));
    percentage = len(intersection)/c;
    return len(intersection),percentage

def getResult_k(dataFrame_dataset,top_k):
    list_class_PageRank = dataFrame_dataset['PageRank'].rank(method="first",ascending=False);
    list_class_avgUserScore = dataFrame_dataset['avgUserScore'].rank(method="first",ascending=False);
    result_intersection = [];
    result_percentage = [];
    for k in top_k:
        result,percentage = overlap(list_class_PageRank, list_class_avgUserScore, k);
        result_intersection.append(result);
        result_percentage.append(percentage);
    return result_intersection,result_percentage;

# a = [1,1,3,3,5,7]
# b = [1,3,4,6,8,9]
# k=0.25;
# result = overlap(a,b,k);
# print(result);