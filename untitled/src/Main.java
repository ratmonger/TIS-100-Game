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
import java.util.concurrent.CountDownLatch;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        int counter = 0;
        Parcer parcer = new Parcer();
        String[][] location = parcer.getarray();
        int size = ((location.length-1)/2)*((location[0].length-1)/2);
        List<List> instruct= parcer.getinstruct();
        Port[][] objects = new Port[location.length][location[0].length];
        CountDownLatch latch = new CountDownLatch(size);

        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if(location[i][j].equals("port")){
                    objects[i][j] = new Port();
                }
                if(location[i][j].equals("inpt")){
                    objects[i][j] = new InputPort();
                    objects[i][j].getInQueue().addAll(parcer.getQueue());
                }
            }
        }
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if(location[i][j].equals("silo")){
                    Thread silo = new Thread(new Interpreter((objects[i-1][j]),objects[i+1][j],objects[i][j-1],objects[i][j+1], latch, counter));
                    silo.start();
                    counter++;
                }
            }
        }



        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER_RIGHT);
        int outer =0;
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {

                if(location[i][j].equals("port")){


                   grid.add(new Label("port"),j,i);
                }
                if(location[i][j].equals("silo")){
                    TextArea sil = new TextArea();

                    sil.setEditable(false);
                    sil.setMouseTransparent(true);
                    sil.setFocusTraversable(false);
                    List<String> texts = instruct.get(outer);
                    for (String s : texts) {
                        sil.appendText(s + "\n");
                    }
                    sil.setPrefSize(120,120);
                    grid.add(sil,j,i);
                    String text = sil.getText();
                    int endIndex = text.indexOf("\n");
                    sil.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: black;");
                    sil.selectRange(0, endIndex);
                    outer++;
                }
                if(location[i][j].equals("inpt")){
                    grid.add(new Label("inpt"),j,i);
                }
                if(location[i][j].equals("oupt")){
                    grid.add(new Label("oupt"),j,i);
                }
            }
        }


        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        vbox1.getChildren().addAll(hbox1,hbox2);
        vbox1.setSpacing(200);
        vbox1.setAlignment(Pos.CENTER);
        TextArea input = new TextArea("input");
        input.setEditable(false);
        TextArea output = new TextArea("output");
        output.setEditable(false);
        input.setPrefSize(50,200);
        output.setPrefSize(50,200);
        hbox1.setSpacing(10);
        hbox1.getChildren().addAll(input,output);

        Button start = new Button("start");
        Button pause = new Button("pause");
        Button halt = new Button("Halt");
        start.setAlignment(Pos.BOTTOM_CENTER);
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(halt,pause,start);
        hbox2.setAlignment(Pos.BOTTOM_CENTER);
        root.setCenter(grid);
        root.setLeft(vbox1);
        primaryStage.setTitle("Silos");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
