import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        root.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        HBox inout = new HBox();

        grid.setAlignment(Pos.CENTER_RIGHT);
        VBox vbox1 = new VBox();
        VBox inbox = new VBox();
        VBox outbox = new VBox();
        outbox.setAlignment(Pos.CENTER);
        inbox.setAlignment(Pos.CENTER);
        inout.getChildren().addAll(inbox,outbox);
        vbox1.getChildren().add(inout);
        vbox1.setSpacing(10);
        inbox.setSpacing(10);
        inout.setSpacing(10);
        outbox.setSpacing(10);

        vbox1.setAlignment(Pos.CENTER);


        int counter = 0;
        Parcer parcer = new Parcer();
        String[][] location = parcer.getarray();
        int size = parcer.getRows() * parcer.getCols();
        List<List> instruct = parcer.getinstruct();
        Port[][] objects = new Port[location.length][location[0].length];
        Component[][] elements =
                new Component[location.length][location[0].length];
        // array containing ALL components
        CountDownLatch latch = new CountDownLatch(size);


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("port")) {
                    objects[i][j] = new Port(i % 2);
                    elements[i][j] = objects[i][j];
                }
                if (location[i][j].equals("inpt")) {
                    objects[i][j] = new InputPort(i % 2);
                    if (i == 0 || j == location[0].length -1) {
                        objects[i][j].getInQueue()
                                .addAll(parcer.getinputs()[i][j]);
                    } else {
                        objects[i][j].getOutQueue()
                                .addAll(parcer.getinputs()[i][j]);

                    }
                    elements[i][j] = objects[i][j];

                    TextArea input = new TextArea("input\n");
                    input.setEditable(false);
                    input.setPrefSize(50,200);

                    for (Object s : parcer.getinputs()[i][j]) {
                        input.appendText(s + "\n");
                    }
                    inbox.getChildren().addAll(input);

                }
                if (location[i][j].equals("oupt")) {
                    objects[i][j] = new OutPort(i % 2);
                    elements[i][j] = objects[i][j];

                    TextArea output = new TextArea("output");
                    output.setEditable(false);
                    output.setPrefSize(50,200);
                    output.setMaxSize(50,200);
                    output.setMinSize(50,200);
                    outbox.getChildren().addAll(output);

                }
            }
        }

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
                    silo.start();
                    counter++;
                }
            }
        }


        int outer = 0;
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {

                if (location[i][j].equals("port")) {

                    Label port = new Label("port");
                    port.setAlignment(Pos.BASELINE_CENTER);
                    grid.add(port, j, i);
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
                    grid.add(new Label("inpt"), j, i);
                }
                if (location[i][j].equals("oupt")) {
                    grid.add(new Label("oupt"), j, i);
                }
            }
        }



        HBox hbox2 = new HBox();
        Button start = new Button("start");
        Button pause = new Button("pause");
        Button halt = new Button("Halt");
        start.setAlignment(Pos.BOTTOM_CENTER);
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(halt, pause, start);
        hbox2.setAlignment(Pos.BOTTOM_CENTER);
        vbox1.getChildren().add(hbox2);
        root.setCenter(grid);
        root.setLeft(vbox1);
        primaryStage.setTitle("Silos");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}
