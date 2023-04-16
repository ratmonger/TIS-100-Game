import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Parser {
    /*
    //
   _oo\
  (__/ \  _  _
     \  \/ \/ \
     (         )\
      \_______/  \
       [[] [[]
       [[] [[]
  */
    List<String> tokens = new ArrayList<>();
    String word;
    Scanner input;
    ArrayList<List<String>> silos;
    int row;
    int col;
    int inputRow;
    int inputCol;
    int outputRow;
    int outputCol;
    Silo[][] siloCamp;
    List<Integer> inputList;
    List<Integer> outputList;
    List<String> inputStuff = new ArrayList<>();

    public void ReadParser(String filePath) {
        File file = new File(filePath);
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        row = input.nextInt();
        col = input.nextInt();
        input.nextLine();
        boolean beforeInput = true;
        while (input.hasNextLine()) {
            word = input.nextLine();
            if (word.equals("INPUT")) {
                beforeInput = false;
            }

            if (beforeInput) {
                if (!word.equals("OUTPUT")) {
                    tokens.add(word);
                }
            } else {
                inputStuff.add(word);
            }
        }
        processInputStuff();
        siloCamp(row, col);
    }



    public void setSilos() {
        silos = new ArrayList<>();
        List<String> silo = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            silo.add(token);
            if (token.equals("END")) {
                silos.add(silo);
                silo = new ArrayList<>();
            }
        }
    }

    public void siloCamp(int row, int col) {
        siloCamp = new Silo[row][col];
        setSilos();
        int siloIndex = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (siloIndex < silos.size()) {
                    List<String> siloP = silos.get(siloIndex);
                    Silo silo = new Silo(siloP);
                    siloCamp[i][j] = silo;
                    siloIndex++;
                }
            }
        }
        outputList = new ArrayList<>();
    }
    public void processInputStuff() {
        ListIterator<String> iterator = inputStuff.listIterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (word.equals("INPUT")) {
                inputValues(iterator);
            } else if (word.equals("OUTPUT")) {
                outputValues(iterator);
            }
        }
    }
    public void inputValues(ListIterator<String> iterator) {
        String[] rowAndCol = iterator.next().split(" ");
        if(rowAndCol[0].equals("-")){
            inputRow = -1 * Integer.parseInt(rowAndCol[1]);
            inputCol = Integer.parseInt(rowAndCol[2]);
        }else {
            inputRow = Integer.parseInt(rowAndCol[0]);
            inputCol = Integer.parseInt(rowAndCol[1]);
        }
        inputList = new ArrayList<>();
        String val = iterator.next();
        while (!val.equals("END")) {
            inputList.add(Integer.parseInt(val));
            if (iterator.hasNext()) {
                val = iterator.next();
            } else {
                break;
            }
        }
    }
    public void outputValues(ListIterator<String> iterator) {
        String[] rowAndCol = iterator.next().split(" ");
        outputRow = Integer.parseInt(rowAndCol[0]);
        outputCol = Integer.parseInt(rowAndCol[1]);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sb.append("Silo at (").append(i).append(", ").append(j).append("):\n");
                sb.append(siloCamp[i][j].toString()).append("\n");

            }
        }
        return sb.toString();
    }
}