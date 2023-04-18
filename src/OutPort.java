import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.concurrent.LinkedBlockingQueue;

public class OutPort extends Port {


    public OutPort(int x, int y, int a, int b){
        super(x, y, a,b);

    }

    @Override
    public BorderPane toGUI() {

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
        if (getInQueue().peek() != null){
            in.setText(String.valueOf(getInQueue().peek()));}
        Label out = new Label();
        if (getOutQueue().peek() != null){
            out.setText(String.valueOf(getOutQueue().peek()));}

        if (this.getRowOffset() == 0) {//// fix this and read from here
            vbox.getChildren().add(out);
            vbox.getChildren().add(RightArrow(0, 0));
            vbox.getChildren().add(LeftArrow(0, 0));
            vbox.getChildren().add(in);
        } else {
            HBox hbox2 = new HBox();
            hbox2.setAlignment(Pos.CENTER);
            vbox.getChildren().add(hbox2);
            hbox2.getChildren().add(out);
            hbox2.getChildren().add(UpArrow(0, 0));
            hbox2.getChildren().add(DownArrow(0, 0));
            hbox2.getChildren().add(in);
        }
        hbox.getChildren().add(vbox);
        BorderPane temp = new BorderPane(hbox);
        return temp;
    }

}

