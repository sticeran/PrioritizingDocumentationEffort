import numpy as ny

damping_factor = 0.85;  # 阻尼系数,即α
max_iterations = 100;  # 最大迭代次数
min_delta = 0.00000001;  # 确定迭代是否结束的参数,即ϵ
F_back_recommendation = 0.5;  # 后向推荐fraction

weightList_dependenceType = [1,1,1,1];  # 依赖类型权重
# weightList_dependenceType = [3,3,2,4];  # 依赖类型权重

# 得到转移概率矩阵
def getTransitionProbabilityMatrix(matrix_CA,matrix_CM,matrix_MM,matrix_Inheritance):
    #numpy中关于数据类型：Array中只允许存储相同的数据类型，这样可以更有效的使用内存，提高运算效率。
    array_matrix_CA = ny.array(matrix_CA,dtype='float64');
    array_matrix_CM = ny.array(matrix_CM,dtype='float64');
    array_matrix_MM = ny.array(matrix_MM,dtype='float64');
    array_matrix_Inheritance = ny.array(matrix_Inheritance,dtype='float64');
    array_matrix_total = weightList_dependenceType[0]*array_matrix_CA + weightList_dependenceType[1]*array_matrix_CM + weightList_dependenceType[2]*array_matrix_MM + weightList_dependenceType[3]*array_matrix_Inheritance;
    #是否使用反向推荐，反向推荐相当于矩阵转置，若使用，原矩阵增加0<F<1的矩阵转置
#     array_matrix_total_transpose = ny.transpose(array_matrix_total);
#     array_matrix_total = array_matrix_total + F_back_recommendation*array_matrix_total_transpose;
    
    
#     print(array_matrix_total);
    array_sumColumn = array_matrix_total.sum(axis=0);#按列求和
#     print(array_sumColumn);
    for index, value in enumerate(array_sumColumn):
        if value==0:
            array_matrix_total[:,index] = 1;#给元素都是0列赋值为1
            array_sumColumn[index]=len(array_sumColumn);#该列的和则是元素个数
#     print(array_matrix_total);
#     array_matrix_total = array_matrix_total / array_sumColumn;
#     print(array_matrix_total);
    return array_matrix_total / array_sumColumn;


#计算pageRank值，V`=aMV+(1-a)e，M转移概率矩阵
def pageRank(dic_dataset,M):
    dic_size = len(dic_dataset);
    if dic_size == 0:
        return {};
#     dic_nodes = dic_dataset.keys();
    #给每个节点赋予初始的PR值，即构造V0
#     dic_V = dict.fromkeys(dic_nodes, 1 / dic_size);
    V = [ 1 / dic_size for i in range(len(dic_dataset))];#pr = firstPr(M) @UnusedVariable
    vector_damping_value = [ i*(1 - damping_factor) for i in V];#公式中的(1−α)e部分
    flag = False;
    for i in range(max_iterations):
#         change = 0;
        #判断pr矩阵是否收敛，若小于ϵ则停止循环  
        V_next = damping_factor*ny.dot(M,V) + vector_damping_value;
        if (abs(V - V_next) < min_delta).all() == True:
            flag = True;
            break;
        V = V_next;
    if flag:
        print("finished in %d iterations!" % i);
    else:
        print("finished out of 100 iterations!")
    return V;

#V`=(aM+(1-a))V,其中(1-a)是(1-a)和M中的每一个元素相加。
def pageRank2(dic_dataset,M):
    dic_size = len(dic_dataset);
    if dic_size == 0:
        return {};
#     dic_nodes = dic_dataset.keys();
    #给每个节点赋予初始的PR值，即构造V0
#     dic_V = dict.fromkeys(dic_nodes, 1 / dic_size);
    V = [ 1 / dic_size for i in range(len(dic_dataset))];#pr = firstPr(M) @UnusedVariable
#     vector_damping_value = [ i*(1 - damping_factor) for i in V];#公式中的(1−α)e部分
    flag = False;
    
    M_rows = M.shape[0];
    M_cols = M.shape[1];
    M_ones = ny.ones((M_rows,M_cols));
    for i in range(max_iterations):
#         change = 0;
        #判断pr矩阵是否收敛，若小于ϵ则停止循环  
#         V_next = damping_factor*ny.dot(M,V) + vector_damping_value;
        A = damping_factor*M + (1-damping_factor)/M_rows *M_ones;
        V_next = ny.dot(A,V);
        
        if (abs(V - V_next) < min_delta).all() == True:
            flag = True;
            break;
        V = V_next;
    if flag:
        print("finished in %d iterations!" % i);
    else:
        print("finished out of 100 iterations!")
    return V;

if __name__=="__main__":
#     A=[0,1/2,3/4,0]
#     B=[2/7,0/7,0,0]
#     C=[4/7,0/7,0,1]
#     D=[1/7,1/2,1/4,0]
#     M=list();
#     M.append(A)
#     M.append(B)
#     M.append(C)
#     M.append(D)
    
    a = ((1/3,1/2),(2/3,1/2))
    M = ny.array(a)
    dic_dataset=["1","2"]
    print (pageRank(dic_dataset,M))  # 计算pr值 
#     print (pageRank2(dic_dataset,M))  # 计算pr值 

#     A = [[1,0,0],[0,0,0],[0,0,0]]
#     B = [[0,0,0],[1,0,0],[0,0,0]]
#     C = [[0,0,0],[0,0,0],[1,0,0]]
#     D = [[0,1,0],[0,1,0],[0,1,0]]
#     getTransitionProbabilityMatrix(A,B,C,D)

