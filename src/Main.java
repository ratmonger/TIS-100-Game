import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {
    private Scene scene;
    private Parcer parcer;

    public static void main(String[] args) {
        launch(args);
    }

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
