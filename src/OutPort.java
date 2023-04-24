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

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;


public class OutPort extends Port {


    public OutPort(int x, int y, int a, int b) {
        super(x, y, a, b);

    }

    private ArrayList<Integer> outputInts = new ArrayList<Integer>(0);

    /** Returns list of numbers passed to the outport
     * @return arraylist of numbers
     */
    public ArrayList<Integer> getOutputInts(){


        return this.outputInts;
    }

    /**
     * Fills a borderpane with the representation of an Output Port
     * @return borderpane containing GUI representation of an output port
     */
    @Override
    public BorderPane toGUI() {

        if (getInQueue().peek() != null) {
            outputInts.add(getInQueue().poll());
        }
        if (getOutQueue().peek() != null) {
            outputInts.add(getOutQueue().poll());
        }




        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        hbox.setMinSize(50, 50);
        hbox.setMaxSize(50, 50);
        vbox.setMinSize(50, 50);
        vbox.setMaxSize(50, 50);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        Label in = new Label();
        if (outputInts.size() > 0) {

            in.setText(String.valueOf(outputInts.get(outputInts.size() - 1)));

        }
        Label out = new Label();
        out.setTextFill(Paint.valueOf("WHITE"));
        in.setTextFill(Paint.valueOf("WHITE"));
        out.setFont(new Font("Courier New Bold",
                              12));
        in.setFont(new Font("Courier New Bold",
                            12));
        if (getOutQueue().peek() != null) {
            out.setText(String.valueOf(getOutQueue().peek()));
        }


        if (this.getRowOffset() == 0) {//// fix this and read from here
            HBox hbox2 = new HBox();
            hbox2.setSpacing(5);
            hbox2.setAlignment(Pos.CENTER);
            vbox.getChildren().add(hbox2);
            hbox2.getChildren().add(UpArrow(0, 0));
            hbox2.getChildren().add(out);
        }

        if (this.getRowOffset() == this.getRows() - 1) {

            HBox hbox2 = new HBox();
            hbox2.setSpacing(5);
            hbox2.setAlignment(Pos.CENTER);
            vbox.getChildren().add(hbox2);
            hbox2.getChildren().add(in);
            hbox2.getChildren().add(DownArrow(0, 0));
        }

        if (this.getColOffset() == 0) {
            vbox.setSpacing(5);
            vbox.getChildren().add(in);
            vbox.getChildren().add(LeftArrow(0, 0));
        }

        if (this.getColOffset() == this.getCols() - 1) {
            vbox.setSpacing(5);
            vbox.getChildren().add(RightArrow(0, 0));
            vbox.getChildren().add(out);
        }


        hbox.getChildren().add(vbox);
        BorderPane temp = new BorderPane(hbox);
        return temp;
    }

}
