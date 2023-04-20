import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
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
    private ArrayList<ArrayList<String>> instruct;
    Parcer parcer;
    private long delay;
    private boolean paused = false;


    public Display(Scene scene, Parcer parcer) {

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
        Button start = new Button("Start");
        start.setFont(new Font("Courier New Bold", 12));
        Button pause = new Button("Pause");
        pause.setFont(new Font("Courier New Bold", 12));
        Button halt = new Button("Halt");
        halt.setFont(new Font("Courier New Bold", 12));
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
        this.parcer = parcer;
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
                    input.setFont(new Font("Courier New Bold", 12));
                    input.setEditable(false);
                    input.setPrefSize(55, 200);

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
                    output.setFont(new Font(
                            "Courier New Bold",
                            12));
                    output.setEditable(false);
                    output.setPrefSize(60, 200);
                    output.setMaxSize(60, 200);
                    output.setMinSize(60, 200);
                    outbox.getChildren().addAll(output);

                }
            }
        }
        Boolean[] firstime = {true};
        Boolean[] istep = {false};
        start.setOnAction(e -> {
            paused = false;
                              if (firstime[0]) {
                                  startAllSilo();
                                  firstime[0] = false;
                              }
                              resumesilos();
                              istep[0] = false;
                              pause.setText("pause");
                          }
        );

        pause.setOnAction(e -> {
                              if (!istep[0]) {
                                  stopALLsilos();
                                  this.delay = -1;
                                  pause.setText("step");
                                  istep[0] = true;
                              } else if (istep[0]) {
                                  if (firstime[0]) {
                                      startAllSilo();
                                      firstime[0] = false;
                                  }
                                  stepALLsilos();
                                  paused = true;

                              }
                          }
        );


        halt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<ArrayList<String>> instructions =
                        new ArrayList<ArrayList<String>>();


                for (int i = 0; i < location.length; i++) {
                    for (int j = 0; j < location[0].length; j++) {
                        if (location[i][j].equals("silo")) {
                            instructions.add(
                                    ((Silo) elements[i][j]).getSiloText());
                        }
                    }
                }

//                for (ArrayList<String> ls : instructions) {
//                    for (String s: ls
//                         ) {
//                        System.out.println(s);
//
//                    }
//
//                }

                new Display(scene, parcer, instructions);


            }
        });


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    Interpreter interp = new Interpreter((objects[i - 1][j]),
                                                         objects[i + 1][j],
                                                         objects[i][j - 1],
                                                         objects[i][j + 1],
                                                         latch, counter,
                                                         parcer);
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
                    grid.add(elements[i][j].toGUI(), j, i);
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

    /////////////////////////////////////////
    public Display(Scene scene, Parcer parcer,
                   ArrayList<ArrayList<String>> instructions,
                   Boolean running) {

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
        Button start = new Button("Start");
        start.setFont(new Font("Courier New Bold", 12));
        Button pause = new Button("Pause");
        pause.setFont(new Font("Courier New Bold", 12));
        Button halt = new Button("Halt");
        halt.setFont(new Font("Courier New Bold", 12));
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
        this.parcer = parcer;
        location = parcer.getarray();
        int size = parcer.getRows() * parcer.getCols();
        this.instruct = instructions;
        this.parcer.setInstruct(instructions);
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
                    input.setFont(new Font("Courier New Bold", 12));
                    input.setEditable(false);
                    input.setPrefSize(55, 200);

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
                    output.setFont(new Font(
                            "Courier New Bold",
                            12));
                    output.setEditable(false);
                    output.setPrefSize(60, 200);
                    output.setMaxSize(60, 200);
                    output.setMinSize(60, 200);
                    outbox.getChildren().addAll(output);

                }
            }
        }
        Boolean[] firstime = {true};
        Boolean[] istep = {false};
        start.setOnAction(e -> {
            paused = false;
                              if (firstime[0]) {


                                  instructions.clear();


                                  for (int i = 0; i < location.length; i++) {
                                      for (int j = 0; j < location[0].length; j++) {
                                          if (location[i][j].equals("silo")) {
                                              instructions.add(
                                                      ((Silo) elements[i][j]).getSiloText());
                                          }
                                      }
                                  }


                                  new Display(scene, parcer, instructions,
                                              true);

                                  startAllSilo();
                                  firstime[0] = false;
                              }
                              resumesilos();
                              istep[0] = false;
                              pause.setText("pause");
                          }
        );

        pause.setOnAction(e -> {
                              if (!istep[0]) {
                                  stopALLsilos();
                                  this.delay = -1;
                                  pause.setText("step");
                                  istep[0] = true;
                              } else if (istep[0]) {
                                  if (firstime[0]) {
                                      startAllSilo();
                                      firstime[0] = false;
                                  }
                                  stepALLsilos();
                                  paused = true;

                              }
                          }
        );


        halt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<ArrayList<String>> instructions =
                        new ArrayList<ArrayList<String>>();


                for (int i = 0; i < location.length; i++) {
                    for (int j = 0; j < location[0].length; j++) {
                        if (location[i][j].equals("silo")) {
                            instructions.add(
                                    ((Silo) elements[i][j]).getSiloText());
                        }
                    }
                }

//                for (ArrayList<String> ls : instructions) {
//                    for (String s: ls
//                         ) {
//                        System.out.println(s);
//
//                    }
//
//                }

                new Display(scene, parcer, instructions);


            }
        });


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    Interpreter interp = new Interpreter((objects[i - 1][j]),
                                                         objects[i + 1][j],
                                                         objects[i][j - 1],
                                                         objects[i][j + 1],
                                                         latch, counter,
                                                         parcer);
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
                    grid.add(elements[i][j].toGUI(), j, i);
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


        startAllSilo();
        firstime[0] = false;

        resumesilos();
        istep[0] = false;
        pause.setText("pause");


    }


    public Display(Scene scene, Parcer parcer,
                   ArrayList<ArrayList<String>> instructions) {

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
        Button start = new Button("Start");
        start.setFont(new Font("Courier New Bold", 12));
        Button pause = new Button("Pause");
        pause.setFont(new Font("Courier New Bold", 12));
        Button halt = new Button("Halt");
        halt.setFont(new Font("Courier New Bold", 12));
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
        this.parcer = parcer;
        location = parcer.getarray();
        int size = parcer.getRows() * parcer.getCols();
        this.instruct = instructions;
        this.parcer.setInstruct(instructions);
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
                    input.setFont(new Font("Courier New Bold", 12));
                    input.setEditable(false);
                    input.setPrefSize(55, 200);

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
                    output.setFont(new Font(
                            "Courier New Bold",
                            12));
                    output.setEditable(false);
                    output.setPrefSize(60, 200);
                    output.setMaxSize(60, 200);
                    output.setMinSize(60, 200);
                    outbox.getChildren().addAll(output);

                }
            }
        }
        Boolean[] firstime = {true};
        Boolean[] istep = {false};
        start.setOnAction(e -> {
            paused = false;
                              if (firstime[0]) {


                                  instructions.clear();


                                  for (int i = 0; i < location.length; i++) {
                                      for (int j = 0; j < location[0].length; j++) {
                                          if (location[i][j].equals("silo")) {
                                              instructions.add(
                                                      ((Silo) elements[i][j]).getSiloText());
                                          }
                                      }
                                  }


                                  new Display(scene, parcer, instructions,
                                              true);

                                  startAllSilo();
                                  firstime[0] = false;
                              }
                              resumesilos();
                              istep[0] = false;
                              pause.setText("pause");
                          }
        );

        pause.setOnAction(e -> {
                              if (!istep[0]) {
                                  stopALLsilos();
                                  this.delay = -1;
                                  pause.setText("step");
                                  istep[0] = true;
                              } else if (istep[0]) {
                                  if (firstime[0]) {
                                      startAllSilo();
                                      firstime[0] = false;
                                  }
                                  stepALLsilos();
                                  paused = true;

                              }
                          }
        );


        halt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<ArrayList<String>> instructions =
                        new ArrayList<ArrayList<String>>();


                for (int i = 0; i < location.length; i++) {
                    for (int j = 0; j < location[0].length; j++) {
                        if (location[i][j].equals("silo")) {
                            instructions.add(
                                    ((Silo) elements[i][j]).getSiloText());
                        }
                    }
                }

//                for (ArrayList<String> ls : instructions) {
//                    for (String s: ls
//                         ) {
//                        System.out.println(s);
//
//                    }
//
//                }

                new Display(scene, parcer, instructions);


            }
        });


        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    Interpreter interp = new Interpreter((objects[i - 1][j]),
                                                         objects[i + 1][j],
                                                         objects[i][j - 1],
                                                         objects[i][j + 1],
                                                         latch, counter,
                                                         parcer);
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
                    grid.add(elements[i][j].toGUI(), j, i);
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


    ////////////////////////////////////////


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
                if (elapsed > 2) {
                    grid.getChildren().clear();
                    outbox.getChildren().clear();
                    inbox.getChildren().clear();

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

                                grid.add(elements[i][j].toGUI(), j, i);
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

                                TextArea input = new TextArea("input\n");
                                input.setFont(new Font("Courier New Bold", 12));
                                input.setEditable(false);
                                input.setPrefSize(55, 200);

                                for (Object s :
                                        parcer.getinputs()[i][j]) {
                                    input.appendText(s + "\n");
                                }
                                inbox.getChildren().addAll(input);


                            }
                            if (location[i][j].equals("oupt")) {
                                if (elements[i][j] != null) {
                                    BorderPane temp = elements[i][j].toGUI();
                                    grid.add(temp, j, i);
                                    TextArea output = new TextArea("output");
                                    output.setEditable(false);
                                    output.setPrefSize(60, 200);
                                    output.setMaxSize(60, 200);
                                    output.setMinSize(60, 200);

                                    output.setFont(new Font(
                                            "Courier New Bold",
                                            12));
                                    outbox.getChildren().addAll(output);
                                    ArrayList<Integer> temp1 =
                                            getList((OutPort) elements[i][j]);
                                    for (Object s : temp1) {
                                        output.appendText("\n" + s);
                                    }
                                } else {
                                    HBox temp = new HBox();
                                    grid.add(temp, j, i);
                                }

                                //  for (Object s : temp ) {
                                //     output.appendText(s + "\n");
                                //}


                            }
                        }
                    }

                }


                if (delay == -1){
                    stop();

                } else if (paused == true){
                    delay--;
                }

            }

        };
        timer.start();


    }


    public ArrayList<Integer> getList(OutPort o) {
        return o.getOutputInts();

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

    public void stepALLsilos() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[0].length; j++) {
                if (location[i][j].equals("silo")) {
                    ((Silo) elements[i][j]).stepsilo();
                }
            }
        }
    }


}
