import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class pageRankKazem{

    

        public static void main(String[] args)
    {
	String fileName="munmun_twitter_social.txt";
      
        int numOfEdges=0;
	int numOfNodes=0;
        
        double dampingFactor=0.85;
        
        double sumRanks=0.0;
        
        boolean convergence=false;//to stop when reach convergence
        
        String from; //to read first lines' values
        String to;//to read second lines' values
        int tempCount;//to count out links
        
        
	Hashtable<String, Integer> nodesOut = new Hashtable<String, Integer>();//store number of out links of each node
        Set<Integer> deadendsSet = new HashSet<Integer>();

        //reading the file
	try {        
            Scanner scanner = new Scanner(new File(fileName));
            numOfEdges = scanner.nextInt();
            numOfNodes = scanner.nextInt();
            
            int [][]sparse = new int [numOfEdges][2];  
            double []value = new double [numOfEdges];  
            
            double []r0 = new double [numOfNodes]; 
            double []r1 = new double [numOfNodes]; 
            
            double []temp = new double [numOfNodes]; // to use when switch pointer between r0 and r1
            
            double sumOfDeadends=0;
            
            for(int i=0;i<r0.length;i++)//initialize r0
            {
                r0[i]=1.0/numOfNodes ;
                
            }
            
            
            int counter=0;
            while(scanner.hasNext()){//start reading entries from the file
            
            from= scanner.next();//source nodes
            to= scanner.next();// destination node
           
            
            
            //count the degree of the nodes
            if(nodesOut.get(from)==null){
            nodesOut.put(from,1);
            }
            else{
            tempCount= nodesOut.get(from)+1 ;
            
            nodesOut.put(from,tempCount);
            }
            
            deadendsSet.add(Integer.parseInt(to));//in the first reading we will assume all destination nodes as deadends
            
            
            sparse[counter][0]=Integer.parseInt(to)-1;
            sparse[counter][1]=Integer.parseInt(from)-1;
                        
            value[counter]=0;
            counter++;  
            }// end reading from file
            
            
            //clean deadends and fill the value of each row is sparse matrix respectively
            for(int i=0;i<numOfEdges;i++)
            {
                if(deadendsSet.contains((sparse[i][1])+1) )
                    deadendsSet.remove((sparse[i][1])+1);  // now only nodes no referring to any node stays in deadends set
                
            
                 String column =  Integer.toString((sparse[i][1])+1) ;
                
           
                if(nodesOut.get(column)!=null)
                {
                  
                    value[i]=1.0/nodesOut.get(column);
                     
                }
                else{//in case node "column" doesn't have any outlink
                     value[i]=0.0;
                }
                
            }
            // convert the deadend set to array
            Integer []deadendsArray = deadendsSet.toArray(new Integer[deadendsSet.size()]);
            
            
            
            int convergenceCounter=0;
             
            while(convergence == false)// keep looping until converge
            {
                //initialze r1 to 0
                
                for (int i=0;i<r1.length;i++)
                r1[i]=0;              
                
                
                // calculate the sum value of deadends from r0
                
                 
               for(int i=0;i<deadendsArray.length;i++)
                {
                    int deadendIndex=(deadendsArray[i])-1;
                    sumOfDeadends=sumOfDeadends+r0[deadendIndex];// subtract -1 since the index start from 0 
                }
                    
              
                sumOfDeadends=sumOfDeadends/numOfNodes;
                
                //calculate first part of r1
                  
               for(int i=0;i<value.length;i=i+1)
                  {
                      
                        int row = sparse[i][0];
                        int column = sparse[i][1];             
                                           
                        r1[row]=r1[row]+(r0[column]*value[i]);
                       
                   }
                
                
                         
                //calculate second part of r1
                for(int i=0;i<r1.length;i++)
                    {
                       
                       r1[i]=((r1[i]+sumOfDeadends)*dampingFactor)+((1-dampingFactor)/numOfNodes);
                        //r1[i]=(r1[i]*dampingFactor)+((1-dampingFactor)/numOfNodes);
                          
                    }
                    
                // check convergence over all the items in r
                
                convergence=true;
                for(int i=0;i<r1.length;i++)
                {
                    if(r0[i]!=r1[i])

                    {
                        convergence=false;
                        break;
                    }
                }
                
                if(convergence==false)
                {
                    temp=r0;
                    r0=r1;
                    r1=temp;
                }
                
        
                
                 
            convergenceCounter++;
            }
            
            
           //compute the cumulative sum of page rank values
            for(int i=0;i<r1.length;i++)
            {
                sumRanks=sumRanks+r1[i];
            }
              
            
                  
        System.out.println(" ");
        System.out.println("---------------------------------------------");
        
       
	System.out.println("# File name                         : "+fileName);
        System.out.println("# Damping Factor(Beta)              : "+dampingFactor);
	System.out.println("# Total number Of Nodes             : "+numOfNodes);
	System.out.println("# Total number Of Edges             : "+numOfEdges);
        System.out.println("# Nodes with positive degree        : "+nodesOut.size());
        System.out.println("# Nodes with 0 degree (deadends)    : "+deadendsSet.size());
        System.out.println("# Iterations needed to converge     : "+convergenceCounter);
        System.out.println("# cumulative sum of page ranks      : "+sumRanks);
        System.out.println("# The sparse matrix saved in        : 'sparseMatrix.csv'");
        System.out.println("# The rank of each node saved in    : 'nodesAndRankValues.csv'");
        
       
              
        System.out.println("---------------------------------------------");
        System.out.println(" "); 
        
        
        // print the r1 result to a csv file
             
        try (PrintWriter writer = new PrintWriter(new File("nodesAndRankValues.csv"))) 
      {

      StringBuilder sb = new StringBuilder();
      sb.append("node");
      sb.append(',');
      sb.append("RankValue");
      sb.append('\n');

      for(int i=0;i<r1.length;i++)
      {
      sb.append(i+1);
      sb.append(',');
      sb.append(r1[i]);
      sb.append('\n'); 
      }
      

      writer.write(sb.toString());

      

    } catch (FileNotFoundException e) 
    {
      System.out.println(e.getMessage());
    }
       
        //print the sparse matrix
         try (PrintWriter writer = new PrintWriter(new File("sparseMatrix.csv"))) 
      {

      StringBuilder sb = new StringBuilder();
      sb.append("row");
      sb.append(',');
      sb.append("column");
      sb.append(',');
      sb.append("value");
      sb.append('\n');

      for(int i=0;i<value.length;i++)
      {
      sb.append(sparse[i][0]+1);
      sb.append(',');
      sb.append(sparse[i][1]+1);
      sb.append(',');
      sb.append(value[i]);
      sb.append('\n'); 
      }
      

      writer.write(sb.toString());

      

    } catch (FileNotFoundException e) 
    {
      System.out.println(e.getMessage());
    }
    
        
        
        
        
	}
	catch(FileNotFoundException fnfe)
        {
        }

	
	 
        

	
}

}
