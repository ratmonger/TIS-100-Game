/**
 * @author Bryce Palmer, Amr Kassem, Momen KatbaBader
 * @version date ( in_ISO_8601 format : 2023 - 4 - 19 )
 * @class CS351
 * @project TIS100
 *
 * Windows compile: javac *.java
 * Windows execute: java Main
 *
 * Or with jar
 * Windows execute: java -jar TIS100.jar
 *
 * The program emulates the game TIS-100 by Zachtronics.
 * The program reads in mock assembly language code that performs sepcific
 * task on a set of number to produce a set of outputs.
 */

import java.util.*;

public class Parcer {
    /*

    chunger
     /\_/\
    ( o.o )
     > ^ <

     */
    String word;

    static ArrayList<ArrayList<String>> instruct =
            new ArrayList<ArrayList<String>>();

    static String[][] newArray;
    static Queue[][] inputs;
    static String[][] original;
    private static boolean scanningDone = false;
    private static Scanner input;

    int row1;
    int col1;

    int numRows;
    int numCols;


    public Parcer() {
        if (!scanningDone) {
            input = new Scanner(System.in);
            row1 = input.nextInt();
            col1 = input.nextInt();

            original = new String[row1][col1];

            this.numRows = 2 * original.length + 1;
            this.numCols = 2 * original[0].length + 1;

            newArray = new String[numRows][numCols];
            inputs = new Queue[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    newArray[i][j]="****";
                }
            }

            for (int i = 0; i < original.length; i++) {
                for (int j = 0; j < original[0].length; j++) {
                    original[i][j] = "silo";
                    newArray[i*2+1][j*2+1] =  original[i][j];
                }
            }
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if(newArray[i][j].equals("silo")){
                        newArray[i+1][j]="port";
                        newArray[i-1][j]="port";
                        newArray[i][j+1]="port";
                        newArray[i][j-1]="port";
                    }
                }
            }
            String line = input.nextLine();
            line = input.nextLine();
            while(!line.equals("INPUT")&&!line.equals("OUTPUT")){
                ArrayList<String> parts = new ArrayList<String>();
                while(!line.equals("END")){
                    parts.add(line);
                    line = input.nextLine();
                }
                instruct.add(parts);

                try {
                    line = input.nextLine();
                } catch (NoSuchElementException e) {
                    break;
                }
            }


            while(input.hasNextLine()) {

                if (line.equals("INPUT")) {
                    inputFN();
                } else if (line.equals("OUTPUT")) {
                    outputFN();
                }
                try {
                    line = input.nextLine();
                    if (line.equals("END")){
                        break;
                    }
                } catch (NoSuchElementException e) {
                    break;
                }
            }
            scanningDone = true;
            System.out.println("");
            printarray(newArray);

        }

    }


    /**
     * Captures the next lines of input into the input ports
     */
    public  void inputFN(){
        Queue<Integer> queue = new LinkedList<>();
        //System.out.println(input.nextLine());
        int row = input.nextInt();

        int col = input.nextInt();
        String val = input.next();
        while (!val.equals("END")){
            queue.add(Integer.valueOf(val));
            try {
                val = input.next();
            } catch (NoSuchElementException e) {
                break;
            }
        }
        //System.out.println(queue);
        row = 2*row+1;
        col = 2*col+1;
        if(row==-1)
            row = 0;
        else if(row==newArray.length)
            row = row-1;
        if(col==-1)
            col = 0;
        else if (col==newArray[0].length)
            col = col-1;

        newArray[row][col]="inpt";
        inputs[row][col]=queue;

    }



    /**
     * Prints the array of elements
     */
    public void printarray(String[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Captures the next lines of input into the output ports
     */
    public  void outputFN() {
        int row = input.nextInt();
        int col = input.nextInt();
        row = 2*row+1;
        col = 2*col+1;
        if(row==-1)
            row = 0;
        else if(row==newArray.length)
            row = row-1;
        if(col==-1)
            col = 0;
        else if (col==newArray[0].length)
            col = col-1;

        newArray[row][col]="oupt";
        input.next();


    }

    /**
     * Gets the rows
     * @return rows
     */
    public int getRows(){
        return row1;
    }

    /**
     * Gets the cols
     * @return cols
     */
    public int getCols(){
        return col1;
    }

    /**
     * Gets the rows that were made on new array
     * @return new rows
     */
    public int getRowsNew(){
        return numRows;
    }

    /**
     * Gets the cols made for the new array
     * @return number of new cols
     */
    public int getColsNew(){
        return numCols;
    }


    /**
     * Gets the instructions
     * @return list of instructions
     */
    public ArrayList<ArrayList<String>> getinstruct(){
        return instruct;
    }

    /**
     * Returns string rep of the grid as array
     * @return string array
     */
    public String[][] getarray(){
        return newArray;
    }

    /**
     * Returns all the queues in an array
     * @return array of queues
     */
    public Queue[][] getinputs(){
        return inputs;
    }

    /**
     * Sets the instructions to the given list
     */
    public void setInstruct( ArrayList<ArrayList<String>> ls){
        instruct = ls;
    }
}
