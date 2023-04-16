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
    private static boolean scanningDone = false;
    private static Scanner input;
    Queue<Integer> queue = new LinkedList<>();


    public Parcer() {
            if (!scanningDone) {
                input = new Scanner(System.in);
                int row = input.nextInt();
                int col = input.nextInt();

                String[][] original = new String[row][col];

                int numRows = 2 * original.length + 1;
                int numCols = 2 * original[0].length + 1;

                newArray = new String[numRows][numCols];

                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        newArray[i][j]="    ";
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

                while(line.equals("INPUT")){

                    inputFN();
                    line = input.nextLine();
                    line = input.nextLine();
                }
                while(line.equals("OUTPUT")){
                    outputFN();
                    line = input.nextLine();
                }
                scanningDone = true;
            }
        }

    public  void inputFN(){
        int row = input.nextInt();
        int col = input.nextInt();
        newArray[row+1][col+1]="inpt";
        String val = input.next();
        while (!val.equals("END")){
            queue.add(Integer.valueOf(val));
            val = input.next();
        }
    }

    public  void outputFN() {
        int row = input.nextInt();
        int col = input.nextInt();
        newArray[row+1][col+1]="oupt";


    }

    public List<List> getinstruct(){
        return instruct;
    }
    public String[][] getarray(){
        return newArray;
    }
    public Queue<Integer> getQueue(){
        return queue;
    }
}