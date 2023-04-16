import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    double width = 1750;
    double height = 1000;
    Pane root;
    Silo[][] siloCamp;
    Parser parser = new Parser();
    List<Integer> inputList = new ArrayList<>();

    List<Integer> outputList = new ArrayList<>();
    TextArea silo;
    GridPane silos;
    Button stopButton;
    Button playButton;
    Button pauseButton;
    Button stepButton;
    Ports ports = new Ports();
    SiloGrid siloGrid;
    InOutPorts inOutPorts = new InOutPorts();
    Label accLabel;
    Label backLabel;
    SiloRegisters siloRegisters;
    VBox regLabel;
    StackPane stackPane;
    siloReader siloReader = new siloReader();
    Thread thread;
    Button start;
    Label intro;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, width, height);
        root.setBackground(Background.fill(Color.BLACK));
        parser.ReadParser("SampleText.txt");
        siloCamp = parser.siloCamp;
        inputList = parser.inputList;
        outputList = parser.outputList;
        VBox introV = new VBox();
        intro = new Label("Virtual Assembly Machine");
        start = new Button("Start");
        start.setFont(new Font(25));
        start.setTextFill(Color.CYAN);
        start.setBackground(Background.fill(Color.BLACK));
        start.setStyle("-fx-text-fill: cyan; -fx-background-color: black; -fx-border-color: cyan; -fx-border-width: 1px;");
        intro.setFont(new Font(50));
        intro.setTextFill(Color.CYAN);
        start.setPrefSize(200,50);
        introV.setSpacing(20);
        introV.getChildren().addAll(intro,start);
        introV.setLayoutX(width/2 - 300);
        introV.setLayoutY(height/2 - 100);
        introV.setAlignment(Pos.CENTER);
        root.getChildren().add(introV);
        start.setOnAction(event -> {
            root.getChildren().remove(introV);
            Silos();
        });
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void Silos() {
        silos = new GridPane();
        for (int i = 0; i < parser.row; i++) {
            for (int j = 0; j < parser.col; j++) {
                silo = new TextArea();
                silo.setStyle("-fx-control-inner-background: black; -fx-text-inner-color: white;");
                silo.setFont(new Font(15));
                silo.setText(siloCamp[i][j].toString());

                siloRegisters = siloCamp[i][j].getSiloRegisters();
                accLabel = new Label("ACC\n  " + siloRegisters.getAcc());
                backLabel = new Label("BAK\n  " + siloRegisters.getBackup());
                accLabel.setFont(new Font(15));
                backLabel.setFont(new Font(15));
                accLabel.setTextFill(Color.WHITE);
                backLabel.setTextFill(Color.WHITE);
                regLabel = new VBox(accLabel, backLabel);
                regLabel.setSpacing(5);
                regLabel.setAlignment(Pos.TOP_RIGHT);
                regLabel.setPickOnBounds(false);

                stackPane = new StackPane();
                stackPane.getChildren().addAll(silo, regLabel);

                silos.add(stackPane, j, i);
            }
        }
        silos.setPrefWidth(325 * parser.col);
        silos.setPrefHeight(250 * parser.row);
        silos.setHgap(75);
        silos.setVgap(75);
        silos.setLayoutX(600);
        silos.setLayoutY(50);
        root.getChildren().add(silos);
        ResultGrid();
        createControlButtons();
        Arrows();
    }
    public void SetLabel(){
        accLabel.setText("ACC\n  " + siloRegisters.getAcc());
        backLabel.setText("BAK\n  " + siloRegisters.getBackup());
    }

    public void createControlButtons() {
        stopButton = new Button("Stop");
        pauseButton = new Button("Pause");
        stepButton = new Button("Step");
        playButton = new Button("Play");

        stopButton.setStyle("-fx-base: white; -fx-text-fill: black; -fx-font-size: 20;");
        pauseButton.setStyle("-fx-base: white; -fx-text-fill: black; -fx-font-size: 20;");
        stepButton.setStyle("-fx-base: white; -fx-text-fill: black; -fx-font-size: 20;");
        playButton.setStyle("-fx-base: white; -fx-text-fill: black; -fx-font-size: 20;");

        int buttonSize = 100;
        stopButton.setMinSize(buttonSize, buttonSize);
        pauseButton.setMinSize(buttonSize, buttonSize);
        stepButton.setMinSize(buttonSize, buttonSize);
        playButton.setMinSize(buttonSize, buttonSize);

        stopButton.setLayoutX(20);
        stopButton.setLayoutY(height - 120);
        pauseButton.setLayoutX(140);
        pauseButton.setLayoutY(height - 120);
        stepButton.setLayoutX(260);
        stepButton.setLayoutY(height - 120);
        playButton.setLayoutX(380);
        playButton.setLayoutY(height - 120);

        root.getChildren().addAll(stopButton, pauseButton, stepButton, playButton);
        setButtons();
    }

    public void setButtons(){
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Playing!");
//                try {
//                    FileWriter writer = new FileWriter("output.txt");
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    for (Node stackPaneNode : silos.getChildren()) {
//                        if (stackPaneNode instanceof StackPane stackPane) {
//                            for (Node child : stackPane.getChildren()) {
//                                if (child instanceof TextArea textArea) {
//                                    stringBuilder.append(textArea.getText());
//                                }
//                            }
//                        }
//                    }
//                    writer.write(parser.row + " " + parser.col + "\n");
//                    for (String item : parser.inputStuff) {
//                        stringBuilder.append(item).append("\n");
//                    }
//                    writer.write(stringBuilder.toString());
//                    writer.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
                MultiThread(parser.row, parser.col);
            }
        });
    }
    public void MultiThread(int row, int col) {
        CountDownLatch latch = new CountDownLatch(row * col);
        for (int b = 0; b < inputList.size(); b++) {
            inOutPorts.setInport(inputList.get(b));
            Thread[][] siloThreads = new Thread[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    siloThreads[i][j] = SetThread(latch, siloCamp, i, j,inOutPorts.getInport());
                    siloThreads[i][j].start();
                }
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            outputList.add(siloCamp[parser.outputRow - 1][parser.outputCol - 1].getSiloRegisters().getAcc());
        }
        SetLabel();
        Result(outputList);

    }
    public Thread SetThread(CountDownLatch countDownLatch, Silo[][] siloCamp, int i, int j, int inputPort){
        thread = siloCamp[i][j].getSiloThread();
        thread = new Thread(() -> {
            for (int k = 0; k < siloCamp[i][j].getInstruction().size(); k++) {
                String[] inputTokens = siloCamp[i][j].getInstruction().get(k).split("\\s+");
                System.out.println("instruction of " + i + " " + j + " : " + siloCamp[i][j].getInstruction().get(k));
                siloReader.Reader(inputTokens,siloCamp, siloCamp[i][j].getSiloRegisters(), i, j, inputPort);
                System.out.println("ACC VALUE: " + i + " " + j + " : "  + siloCamp[i][j].getSiloRegisters().getAcc());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            countDownLatch.countDown();

        });
        return thread;
    }

    public void ResultGrid() {
        Label inputLabel = new Label("Input");
        Label outputLabel = new Label("Output");
        Label resultLabel = new Label("Result");

        inputLabel.setTextFill(Color.WHITE);
        outputLabel.setTextFill(Color.WHITE);
        resultLabel.setTextFill(Color.WHITE);

        inputLabel.setLayoutX(50);
        inputLabel.setLayoutY(50);
        outputLabel.setLayoutX(150);
        outputLabel.setLayoutY(50);
        resultLabel.setLayoutX(250);
        resultLabel.setLayoutY(50);

        root.getChildren().addAll(inputLabel, outputLabel, resultLabel);

        for (int i = 0; i < inputList.size(); i++) {
            Label numberLabel = new Label(Integer.toString(inputList.get(i)));
            numberLabel.setTextFill(Color.WHITE);
            numberLabel.setLayoutX(50);
            numberLabel.setLayoutY(70 + i * 20);
            root.getChildren().add(numberLabel);
        }
    }
    public void Result(List<Integer> list){
        for (int i = 0; i < list.size(); i++) {
            Label numberLabel = new Label(Integer.toString(list.get(i)));
            numberLabel.setTextFill(Color.WHITE);
            numberLabel.setLayoutX(250);
            numberLabel.setLayoutY(70 + i * 20);
            root.getChildren().add(numberLabel);
        }
    }
    public void RightArrow(double x , double y) {
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
        root.getChildren().add(group);
    }
    public void LeftArrow(double x , double y){
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
        root.getChildren().add(group);
    }
    public void upArrow(double x, double y){
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
        root.getChildren().add(group);

    }
    public void DownArrow(double x, double y){
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
        root.getChildren().add(group);
    }

    public void Arrows() {
        double hGap = silos.getHgap();
        double vGap = silos.getVgap();
        double gridX = silos.getLayoutX();
        double gridY = silos.getLayoutY();

        for (int i = 0; i < parser.row; i++) {
            for (int j = 0; j < parser.col - 1; j++) {
                if (siloCamp[i][j] != null && siloCamp[i][j + 1] != null) {
                    double x = gridX + (j * (270 + hGap)) + 305;
                    double y = gridY + (i * (200 + vGap)) + 75;

                    RightArrow(x, y);
                }
            }
        }
        for (int i = 0; i < parser.row; i++) {
            for (int j = 0; j < parser.col; j++) {
                if (j > 0 && siloCamp[i][j] != null && siloCamp[i][j - 1] != null) {

                    double x = gridX + (j * (270 + hGap)) - 60;
                    double y = gridY + (i * (200 + vGap)) + 100;

                    LeftArrow(x, y);
                }
            }
        }
        for (int i = 0; i < parser.row; i++) {
            for (int j = 0; j < parser.col; j++) {
                if (i > 0 && siloCamp[i][j] != null && siloCamp[i - 1][j] != null) {

                    double x = gridX + (j * (270 + hGap)) + 150;
                    double y = gridY + (i * (195 + vGap)) - 55;
                    upArrow(x, y);
                }
            }
        }
        for (int i = 0; i < parser.row - 1; i++) {
            for (int j = 0; j < parser.col; j++) {
                if (siloCamp[i][j] != null && siloCamp[i + 1][j] != null) {

                    double x = gridX + (j * (270 + hGap)) + 125;
                    double y = gridY + (i * (195 + vGap)) + 232;

                    DownArrow(x, y);
                }
            }
        }

        if (siloCamp[parser.inputRow + 1][parser.inputCol] != null) {
            double x = gridX + ((parser.inputCol - 1) * (270 + hGap)) + 130;
            double y = gridY - 25;
            DownArrow(x, y);
        }
        if (siloCamp[parser.outputRow - 1][parser.outputCol - 1] != null) {
            double x = gridX + ((parser.outputCol) * (270 + hGap)) + 130;
            double y = gridY + ((parser.outputRow - 1) * (270 + vGap)) + 280;
            DownArrow(x, y);
        }
    }
}
