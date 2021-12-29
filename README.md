# The replication toolkit for the paper "Prioritizing documentation effort: Can we do it simpler but better?"

## Titile: Prioritizing documentation effort: Can we do it simpler but better?

Our work studies the effectiveness of the supervised artiﬁcial neural network (ANN) approach and the unsupervised PageRank approach for prioritizing documentation effort.

We currently implement a improved PageRank based on Visits Of Links (PageRank-VOL) proposed by Kumar et al. [1] and code replication for the supervised artiﬁcial neural network (ANN) proposed by McBurney et al. [2].

## Quick Start
"ProgramAndData/program/java_documentPrioritization" folder is the code of ANN approach.  
"ProgramAndData/program/external JARs" folder is the external JARs package required by the ANN approach.  
"ProgramAndData/program/new_PageRankAllClass" folder is the code of PageRank approach. 

"ProgramAndData/data/initialArffData" folder are the detailed results of the importance (score) of code files calculated by each approach in the EXPERIMENTAL RESULTS section and DISCUSSIONS section of our paper.  
"ProgramAndData/data/results" folder are the evolution indicators results of each approach.  
"ProgramAndData/data/graphs" folder are linear charts of the comparison of the evolution indicators results of each approach.

## References
[1]	G. Kumar, N. Duhan, A.K Sharma. Page ranking based on number of visits of links of web page. ICCCT 2011: 11-14.  
[2]	P.W. McBurney, S. Jiang, M. Kessentini, N.A. Kraft, A. Armaly, M.W. Mkaouer, C. McMillan. Towards prioritizing documentation effort. IEEE Transactions on Software Engineering, 44(9), 2018: 897-913.
