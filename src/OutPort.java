import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.ArrayList;


public class OutPort extends Port {


    public OutPort(int x, int y, int a, int b) {
        super(x, y, a, b);

    }

    private ArrayList<Integer> outputInts = new ArrayList<Integer>();

    @Override
    public BorderPane toGUI() {

        if (getInQueue().peek() != null) {
            outputInts.add(getInQueue().poll());
        }
        if (getOutQueue().peek() != null) {
            outputInts.add(getOutQueue().poll());
        }



        /*
        in
         up in
         down out
         left out
           right in



           dst
           up out
           down in
           left in
           right out
         */


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        //hbox.setBackground(Background.fill(Color.YELLOW)); // display box size
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
        out.setTextFill(Paint.valueOf("YELLOW"));
        in.setTextFill(Paint.valueOf("YELLOW"));
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

