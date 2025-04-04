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


import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class Main extends Application {
    private Scene scene;
    private Parcer parcer;

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Launches the program and GUI
     * @param primaryStage // utilizes the top level javafx container
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.parcer = new Parcer();

        StackPane home = new StackPane();
        home.setBackground(Background.fill(Color.rgb(23, 23, 23)));
        scene = new Scene(home);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        Label name = new Label("TIS 101");
        Button butt = new Button("Run Program");
        butt.setFont(new Font("Courier New Bold", 16));

        name.setFont(new Font("Courier New", 100));
        name.setMinSize(100,5);
        name.setTextFill(Color.WHITE);
        BorderPane fillBox = new BorderPane();
        fillBox.setMinSize(40,80);


        VBox vbox0 = new VBox();
        vbox0.getChildren().addAll(name,fillBox, butt);
        vbox0.setAlignment(Pos.CENTER);
        home.setAlignment(Pos.CENTER);
        home.getChildren().add(vbox0);

        butt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Display display = new Display(scene, parcer);

            }
        });

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Program is closing");
            System.exit(1);
            // Save file
        });

        primaryStage.setTitle("Silos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
