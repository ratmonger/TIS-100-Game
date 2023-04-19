import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port implements Runnable, Component {

    private final BlockingQueue<Integer> inQueue;
    private final BlockingQueue<Integer> outQueue;
    private int rowOffset;
    private int colOffset;
    private int rows;
    private int cols;

    public Port(int x, int y, int rows, int cols) {
        this.rowOffset = x;
        this.colOffset = y;
        this.rows = rows;
        this.cols = cols;
        this.inQueue = new LinkedBlockingQueue<>();
        this.outQueue = new LinkedBlockingQueue<>();
    }


    // returns the first element
    public int peekPort() {
        return outQueue.peek();
    }

    // removes and returns the first element
    public int popPort() {
        return outQueue.poll();
    }


    public int getRowOffset() {
        return rowOffset;
    }

    public int getColOffset(){
        return colOffset;
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }


    public BlockingQueue<Integer> getInQueue() {
        return inQueue;
    }

    public BlockingQueue<Integer> getOutQueue() {
        return outQueue;
    }

    @Override
    public void run() {
        System.out.println("waiting for input");
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
        out.setTextFill(Paint.valueOf("WHITE"));
        in.setTextFill(Paint.valueOf("WHITE"));
        out.setFont(new Font("Courier New Bold",
                             12));
        in.setFont(new Font("Courier New Bold",
                            12));
        if (getOutQueue().peek() != null){
            out.setText(String.valueOf(getOutQueue().peek()));}

        if (rowOffset % 2 == 1) {
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


    public Group RightArrow(double x, double y) {
        Group group = new Group();

        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, 0.0,
                15.0, 7.5,
                0.0, 15.0
        );
        Rectangle arrowBack = new Rectangle();
        arrowBack.setWidth(20);
        arrowBack.setHeight(5);
        arrowBack.setStroke(Color.WHITE);
        arrowBack.setFill(Color.TRANSPARENT);
        arrow.setFill(Color.TRANSPARENT);
        arrow.setStroke(Color.WHITE);
        arrowBack.setLayoutY(arrow.getLayoutY() + 5);
        arrowBack.setLayoutX(arrow.getLayoutX() - 20.3);
        group.getChildren().addAll(arrowBack, arrow);
        arrow.toFront();
        group.setLayoutX(x);
        group.setLayoutY(y);
        return group;
    }

    public Group LeftArrow(double x, double y) {
        Group group = new Group();
        Polygon arrow = new Polygon();

        arrow.getPoints().addAll(
                15.0, 0.0,
                0.0, 7.5,
                15.0, 15.0
        );
        Rectangle arrowBack = new Rectangle();
        arrowBack.setWidth(20);
        arrowBack.setHeight(5);
        arrowBack.setStroke(Color.WHITE);
        arrowBack.setFill(Color.TRANSPARENT);
        arrow.setFill(Color.TRANSPARENT);
        arrow.setStroke(Color.WHITE);
        arrowBack.setLayoutY(arrow.getLayoutY() + 5);
        arrowBack.setLayoutX(arrow.getLayoutX() + 15.3);
        group.getChildren().addAll(arrow, arrowBack);
        group.setLayoutX(x);
        group.setLayoutY(y);
        return group;
    }

    public Group UpArrow(double x, double y) {
        Group group = new Group();
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, 15.0,
                7.5, 0.0,
                15.0, 15.0
        );
        Rectangle arrowBack = new Rectangle();
        arrowBack.setWidth(5);
        arrowBack.setHeight(20);
        arrowBack.setStroke(Color.WHITE);
        arrowBack.setFill(Color.TRANSPARENT);
        arrow.setFill(Color.TRANSPARENT);
        arrow.setStroke(Color.WHITE);
        arrowBack.setLayoutY(arrow.getLayoutY() + 15.3);
        arrowBack.setLayoutX(arrow.getLayoutX() + 5);
        group.getChildren().addAll(arrowBack, arrow);
        group.setLayoutX(x);
        group.setLayoutY(y);
        return group;

    }

    public Group DownArrow(double x, double y) {
        Group group = new Group();
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, 0.0,
                15.0, 0.0,
                7.5, 15.0
        );
        Rectangle arrowBack = new Rectangle();
        arrowBack.setWidth(5);
        arrowBack.setHeight(20);
        arrowBack.setStroke(Color.WHITE);
        arrowBack.setFill(Color.TRANSPARENT);
        arrow.setFill(Color.TRANSPARENT);
        arrow.setStroke(Color.WHITE);
        arrowBack.setLayoutY(arrow.getLayoutY() - 20);
        arrowBack.setLayoutX(arrow.getLayoutX() + 5);
        group.getChildren().addAll(arrowBack, arrow);
        group.setLayoutX(x);
        group.setLayoutY(y);
        return group;
    }


}

