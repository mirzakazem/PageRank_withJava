# PageRank_withJava

## How to use it:

1- compile the java file:
  javac .\pageRankKazem.java

2- run the java file:
   java .\pageRankKazem.java
   
## How it works:

### first step, read and process data:
- it reads from  "munmun_twitter_social.txt" the number edges, nodes and linkes (vertices) from each entry in the file
- create a sparse matrix (row, column, value)
- create an array for the deadends nodes. (nodes with out degree =0)
- create r0 array and initialize all its items value to 1/n (n= number of nodes)

### second step, start compute the r1 until it converges
inside a while loop we will keep do the following, until r1 stop evolving (converges)

- initialize r1 to 0
- get the sum of deadends values from r0 then normalize it by n.
- compute the first part of the r1 formula, for each value of the sparse matrix: r1 = r1 + (r0 * value)
- compute he seconf part of the r1 formula, for each value of the r1 : r1= ((r1 + sum of deanends) * Beta) + ((1-Beta)/n))

### final step, print result:

1- in the screen you will see the following:
   - name of the file program read data from
   - Beta value
   - Number of the Nodes
   - Number of Edges
   - Number Nodes with positive degree ( degree > 0 )
   - Number of Nodes without any out links ( degree = 0 ) 
   - Number of Iteration the r1 needed to converge
   - cumulative sum of all page ranks
   - name of file contains the sparse matrix values that program used ( row, column , value)
   - name of the file contain the rank of each node

2- in your current folder, you will have two files:
    - "sparseMatrix.csv" which contains the sparse matrix values
    - "nodesAndRankValues.csv"  which contains the nodes and their final page rank value (you can easily filter the values in descending order to know the top nodes)
  
# Note:
- you can modify the value of beta by changing the value of the double variable named : "dampingFactor" (line 17)
