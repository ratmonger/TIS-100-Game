import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Display {

    private Scene scene;
    private BorderPane root;
    private Component[][] elements;
    private String[][] location;
    GridPane grid;
    VBox inbox;
    VBox outbox;
    private long startTime;
    private List<List> instruct;

    public Display(Scene scene) {

        this.root = new BorderPane();
        this.scene = scene;
        this.scene.setRoot(this.root);
        root.getChildren().clear();
        root.setBackground(Background.fill(Color.rgb(23, 23, 23)));

        this.grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        HBox inout = new HBox();

        grid.setAlignment(Pos.CENTER_RIGHT);
        VBox vbox1 = new VBox();
        this.inbox = new VBox();
        this.outbox = new VBox();
        outbox.setAlignment(Pos.CENTER);
        inbox.setAlignment(Pos.CENTER);
        inout.getChildren().addAll(inbox, outbox);
        vbox1.getChildren().add(inout);
        vbox1.setSpacing(10);
        inbox.setSpacing(10);
        inout.setSpacing(10);
        outbox.setSpacing(10);
        HBox hbox2 = new HBox();
        Button start = new Button("start");
        Button pause = new Button("pause");
        Button halt = new Button("Halt");
        start.setAlignment(Pos.BOTTOM_CENTER);
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(halt, pause, start);
        hbox2.setAlignment(Pos.BOTTOM_CENTER);
        vbox1.getChildren().add(hbox2);
        root.setPadding(new Insets(10));
        root.setCenter(grid);
        root.setLeft(vbox1);

        vbox1.setAlignment(Pos.CENTER);


        int counter = 0;
        Parcer parcer = new Parcer();
        location = parcer.getarray();
        int size = parcer.getRows() * parcer.getCols();
        this.instruct = parcer.getinstruct();
        Port[][] objects = new Port[location.length][location[0].length];
        this.elements = new Component[location.length][location[0].length];
        // array containing ALL components
        CountDownLatch latch = new CountDownLatch(size);


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("port")) {
                    objects[i][j] = new Port(i, j, location.length,
                                             location[0].length);
                    elements[i][j] = objects[i][j];
                }
                if (location[i][j].equals("inpt")) {
                    objects[i][j] = new InputPort(i, j, location.length,
                                                  location[0].length);
                    if (i == 0 || j == location[0].length - 1) {
                        objects[i][j].getInQueue()
                                .addAll(parcer.getinputs()[i][j]);
                    } else {
                        objects[i][j].getOutQueue()
                                .addAll(parcer.getinputs()[i][j]);

                    }
                    elements[i][j] = objects[i][j];

                    TextArea input = new TextArea("input\n");
                    input.setEditable(false);
                    input.setPrefSize(50, 200);

                    for (Object s : parcer.getinputs()[i][j]) {
                        input.appendText(s + "\n");
                    }
                    inbox.getChildren().addAll(input);

                }
                if (location[i][j].equals("oupt")) {
                    objects[i][j] = new OutPort(i, j, location.length,
                                                location[0].length);
                    elements[i][j] = objects[i][j];

                    TextArea output = new TextArea("output");
                    output.setEditable(false);
                    output.setPrefSize(50, 200);
                    output.setMaxSize(55, 200);
                    output.setMinSize(55, 200);
                    outbox.getChildren().addAll(output);

                }
            }
        }
        Boolean[] firstime = {true};
        Boolean[] istep = {false};
        start.setOnAction(e -> {
            if(firstime[0]) {
                startAllSilo();
                firstime[0] =false;
            }
            resumesilos();
            istep[0]=false;
            pause.setText("pause");
                }
        );

        pause.setOnAction(e ->{
            if(!istep[0]) {
                stopALLsilos();
                pause.setText("step");
                istep[0]=true;
            }
            }
        );


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    Interpreter interp = new Interpreter((objects[i - 1][j]),
                                                         objects[i + 1][j],
                                                         objects[i][j - 1],
                                                         objects[i][j + 1],
                                                         latch, counter);
                    Thread silo = new Thread(interp);
                    elements[i][j] = new Silo(silo, interp);//pass thread and
                    // interp to new silo class


                    counter++;
                }
            }
        }


        // remove boundary ports
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (i == 0 || i == location.length - 1 || j == 0 ||
                        j == location[0].length - 1) {
                    if (elements[i][j] instanceof Port &
                            !(elements[i][j] instanceof InputPort) &
                            !(elements[i][j] instanceof OutPort)) {
                        elements[i][j] = null;
                    }

                }

            }
        }

        grid.getChildren().clear();

        int outer = 0;
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {

                if (location[i][j].equals("port")) {

                    Label port = new Label("port");
                    port.setAlignment(Pos.BASELINE_CENTER);
                    //grid.add(port, j, i);
                    if (elements[i][j] != null) {
                        BorderPane temp = elements[i][j].toGUI();
                        grid.add(temp, j, i);
                    } else {
                        HBox temp = new HBox();
                        grid.add(temp, j, i);
                    }

                }
                if (location[i][j].equals("silo")) {
                    TextArea sil = new TextArea();
                    ;
                    List<String> texts = instruct.get(outer);
                    for (String s : texts) {
                        sil.appendText(s + "\n");
                    }
                    sil.setPrefSize(120, 120);
                    grid.add(sil, j, i);
                    String text = sil.getText();
                    int endIndex = text.indexOf("\n");
                    sil.setStyle(
                            "-fx-highlight-fill: lightgray; " +
                                    "-fx-highlight-text-fill: black;");
                    sil.selectRange(0, endIndex);
                    outer++;
                }
                if (location[i][j].equals("inpt")) {
                    if (elements[i][j] != null) {
                        BorderPane temp = elements[i][j].toGUI();
                        grid.add(temp, j, i);
                    } else {
                        HBox temp = new HBox();
                        grid.add(temp, j, i);
                    }


                }
                if (location[i][j].equals("oupt")) {
                    if (elements[i][j] != null) {
                        BorderPane temp = elements[i][j].toGUI();
                        grid.add(temp, j, i);
                    } else {
                        HBox temp = new HBox();
                        grid.add(temp, j, i);
                    }
                }
            }
        }

    }


    public void startTimer() {

        this.startTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //if (finish) {
                //    this.stop();
                //}

                    long finishTime = System.nanoTime();
                    long time =
                            TimeUnit.NANOSECONDS.toMillis(
                                    finishTime - startTime);
                    float elapsed = (float) time / 1000;
                    if (elapsed > 1) {
                        grid.getChildren().clear();

                        int outer = 0;
                        for (int i = 0; i < location.length; i++) {
                            for (int j = 0; j < location[0].length; j++) {

                                if (location[i][j].equals("port")) {

                                    Label port = new Label("port");
                                    port.setAlignment(Pos.BASELINE_CENTER);
                                    //grid.add(port, j, i);
                                    if (elements[i][j] != null) {
                                        BorderPane temp = elements[i][j].toGUI();
                                        grid.add(temp, j, i);
                                    } else {
                                        HBox temp = new HBox();
                                        grid.add(temp, j, i);
                                    }

                                }
                                if (location[i][j].equals("silo")) {
                                    TextArea sil = new TextArea();

                                    List<String> texts = instruct.get(outer);
                                    for (String s : texts) {
                                        sil.appendText(s + "\n");
                                    }
                                    sil.setPrefSize(120, 120);
                                    grid.add(sil, j, i);
                                    String text = sil.getText();
                                    int endIndex = text.indexOf("\n");
                                    sil.setStyle(
                                            "-fx-highlight-fill: lightgray; " +
                                                    "-fx-highlight-text-fill: black;");
                                    sil.selectRange(0, endIndex);
                                    outer++;
                                }
                                if (location[i][j].equals("inpt")) {
                                    if (elements[i][j] != null) {
                                        BorderPane temp = elements[i][j].toGUI();
                                        grid.add(temp, j, i);
                                    } else {
                                        HBox temp = new HBox();
                                        grid.add(temp, j, i);
                                    }


                                }
                                if (location[i][j].equals("oupt")) {
                                    if (elements[i][j] != null) {
                                        BorderPane temp = elements[i][j].toGUI();
                                        grid.add(temp, j, i);
                                    } else {
                                        HBox temp = new HBox();
                                        grid.add(temp, j, i);
                                    }
                                }
                            }
                        }

                    }

            }

        };
        timer.start();


    }





    public void startAllSilo() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    ((Silo) elements[i][j]).startSilo();

                }
            }
        }
        startTimer();
    }
    public void stopALLsilos() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    ((Silo) elements[i][j]).pauseSilo();
                }
            }
        }
    }
    public void resumesilos() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    ((Silo) elements[i][j]).resumesilo();
                }
            }
        }
    }


}
