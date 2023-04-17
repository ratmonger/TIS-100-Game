import java.util.*;

public class Parcer {
    /*

    chunger
     /\_/\
    ( o.o )
     > ^ <

     */
    String word;

    static List<List> instruct = new ArrayList<List>();

    static String[][] newArray;
    static Queue[][] inputs;
    static String[][] original;
    private static boolean scanningDone = false;
    private static Scanner input;

    int row1;
    int col1;


    public Parcer() {
        if (!scanningDone) {
            input = new Scanner(System.in);
            row1 = input.nextInt();
            col1 = input.nextInt();

            original = new String[row1][col1];

            int numRows = 2 * original.length + 1;
            int numCols = 2 * original[0].length + 1;

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
            while(!line.equals("INPUT")){
                List<String> parts = new ArrayList<String>();
                while(!line.equals("END")){
                    parts.add(line);
                    line = input.nextLine();
                }
                instruct.add(parts);
                line = input.nextLine();
            }



            while(input.hasNextLine()) {

                if (line.equals("INPUT")) {
                    inputFN();
                } else if (line.equals("OUTPUT")) {
                    outputFN();
                }
                try {
                    line = input.nextLine();
                } catch (NoSuchElementException e) {
                    break;
                }
            }
            scanningDone = true;
            System.out.println("");
            printarray(newArray);

        }

    }

    public  void inputFN(){
        Queue<Integer> queue = new LinkedList<>();
        int row = input.nextInt();
        int col = input.nextInt();
        String val = input.next();
        while (!val.equals("END")){
            queue.add(Integer.valueOf(val));
            val = input.next();
        }
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



    public void printarray(String[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

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

    public int getRows(){
        return row1;
    }

    public int getCols(){
        return col1;
    }


    public List<List> getinstruct(){
        return instruct;
    }
    public String[][] getarray(){
        return newArray;
    }
    public Queue[][] getinputs(){
        return inputs;
    }
}
