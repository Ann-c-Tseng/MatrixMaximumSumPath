/*
Program: 
Given a matrix, find and print the optimal path and individual cell values through the matrix,
also show the final max sum.
The movement goes from top to bottom of the matrix, with each move being "down", "down to the right", or "down to the left".
*/

import java.util.*;
import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class MatrixMaxSum {


//This method prints the collection path.
public void printPath(List<List<Integer>> givenMatrix, List<List<Integer>> solutionMatrix, int rows, int columns, int maxColumnIndex) {
   //Note: rows = last INDEX of matrix's final row, columns = last INDEX of matrix's final column
   int[] pathIndices = new int[rows+1];
   
   //Save the max element's column index first, before grabbing all others.
   pathIndices[0] = maxColumnIndex;
   
   int k = 1;
   int idx = 0;
   for(int i = rows; i >= 1; i--) {
      int prevInt = solutionMatrix.get(i).get(maxColumnIndex) - givenMatrix.get(i).get(maxColumnIndex);
      
      if(maxColumnIndex == 0) { //If curr number is in left-most column of solMatrix, check match prevInt for up + diagonal-right
         if(solutionMatrix.get(i-1).get(0) == prevInt) { //up
            idx = 0;
         } else { //diagonal-right (up a row)
            idx = 1;
         }
      } else if(maxColumnIndex == columns) { //If curr number is in right-most column of solMatrix, check match prevInt for up + diagonal-left
         if(solutionMatrix.get(i-1).get(columns) == prevInt) { //up
            idx = columns;
         } else { //diagonal-left (up a row)
            idx = columns-1;
         }
      } else { //We are not at corner case, and must check up + diagonal-right + diagonal-left
         if(solutionMatrix.get(i-1).get(maxColumnIndex) == prevInt) { //up
            idx = maxColumnIndex;
         } else if(solutionMatrix.get(i-1).get(maxColumnIndex+1) == prevInt) { //diagonal-right (up a row)
            idx = maxColumnIndex+1;
         } else { //diagonal-left (up a row)
            idx = maxColumnIndex-1;
         }
      }
      
      pathIndices[k] = idx; //update the array with chosen idx.
      maxColumnIndex = pathIndices[k]; //make idx the new maxColumnIndex for new itertation
      k++; //plus 1 to pathIndices array index for new iteration.

   }
   
   //Add 1 for 1-indexing to pathIndices for path printing
   int rowNum = 1;
   for(int i = rows; i >= 0; i--) {
      System.out.println("[" + rowNum + "," + (pathIndices[i] + 1) + "]"
      + " – " + givenMatrix.get(rowNum-1).get(pathIndices[i]));
      rowNum++;
   }
}


//This method finds and returns the maximum total value from the given matrix.
public int maxValue(List<List<Integer>> givenMatrix, List<List<Integer>> solutionMatrix) {
   
   int rows = givenMatrix.size()-1; //last row index
   int columns = givenMatrix.get(0).size()-1; //last column index
   
   for(int i = 0; i <= rows; i++) {
      for(int j = 0; j <= columns; j++) {
         int curVal = givenMatrix.get(i).get(j);
        
         if(i == 0) { //If we are at the first row, we just input the givenMatrix value
            solutionMatrix.get(i).set(j,curVal);
         } else if(j == 0) { //If in the left most column, consider down + diagonal-left
            int case2 = curVal + Math.max(solutionMatrix.get(i-1).get(j), solutionMatrix.get(i-1).get(j+1));
            solutionMatrix.get(i).set(j, case2);
         } else if(j == columns) { //If in the right most column, consider down + diagonal-right
            int case3 = curVal + Math.max(solutionMatrix.get(i-1).get(j), solutionMatrix.get(i-1).get(j-1));
            solutionMatrix.get(i).set(j, case3);
         } else { //Otherwise, we need to consider all three travel paths
            int case4 = curVal + Math.max(solutionMatrix.get(i-1).get(j),
            Math.max(solutionMatrix.get(i-1).get(j-1),solutionMatrix.get(i-1).get(j+1)));
            solutionMatrix.get(i).set(j, case4);
         }
      }   
   }
   
   int columnIndex = 0;
   int max = solutionMatrix.get(rows).get(0);
   for(int i = 1; i <= columns; i++) {
      int temp = solutionMatrix.get(rows).get(i);
      if(max < temp) {
         max = temp;
         columnIndex = i;
      }
   }
   
   //Create MatrixMaxSum obj and call printPath.
   MatrixMaxSum obj = new MatrixMaxSum();
   obj.printPath(givenMatrix, solutionMatrix, rows, columns, columnIndex);
   
   return max;
}


public static void main(String[] args) {
      try {
         File myObj = new File(args[0]);
         Scanner myReader = new Scanner(myObj);
         List<List<Integer>> givenMatrix = new ArrayList<List<Integer>>();
         List<List<Integer>> solutionMatrix = new ArrayList<List<Integer>>();
         
         //Making the given matrix from input txt matrix.
         while(myReader.hasNextLine()) {
            String[] sequenceOfString = myReader.nextLine().split("\\s+");

            List<Integer> intArr = new ArrayList<Integer>();
            List<Integer> intArr2 = new ArrayList<Integer>();
            
            for(String s: sequenceOfString) {
               intArr.add(Integer.parseInt(s));
               intArr2.add(0);
            }  
     
            givenMatrix.add(intArr);
            solutionMatrix.add(intArr2);
         }
         
         //Create MatrixMaxSum object and find maximum.
         MatrixMaxSum mObj = new MatrixMaxSum();
         int max = mObj.maxValue(givenMatrix, solutionMatrix);
                  
         System.out.println(max + " is the maximum value");
         
      } catch (FileNotFoundException e) {
         System.out.println("An error occurred. Please check if your '.txt' file input is correct.");
         e.printStackTrace();
      }     
   }
}
