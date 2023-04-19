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
import javafx.stage.Stage;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main extends Application {
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane home = new StackPane();
        scene = new Scene(home, 800, 800);
        Label name = new Label("TIS 101");
        Button butt = new Button("Run Program");
        VBox vbox0 = new VBox();
        vbox0.getChildren().addAll(name, butt);
        vbox0.setAlignment(Pos.CENTER);
        home.setAlignment(Pos.CENTER);
        home.getChildren().add(vbox0);

        butt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Display display = new Display(scene);

            }
        });


        primaryStage.setTitle("Silos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }




}
