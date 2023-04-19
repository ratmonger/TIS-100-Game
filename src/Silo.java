import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Silo implements Component{

    private Thread siloThread;
    private Interpreter interp;

    public Silo (Thread t, Interpreter interp){
        this.siloThread = t;
        this.interp = interp;
    }

    @Override
    public BorderPane toGUI() {

        String cssLayout = "-fx-border-color: white;\n" +
                "-fx-border-outsets: 5;\n" +
                "-fx-border-width: 3;\n";

        TextArea sil = new TextArea();
        sil.setFont(new Font("Courier New Bold", 12));

        ArrayList<String> texts = getSiloCommands();
        for (String s : texts) {
            sil.appendText(s + "\n");
        }
        sil.setPrefSize(130, 130);
        String text = sil.getText();


        sil.setStyle(
                "-fx-highlight-fill: black; " +
                        "-fx-highlight-text-fill: white;" +
                        "-fx-focus-color: transparent ; " +
                        "-fx-faint-focus-color: transparent ;");



//        sil.setStyle(
//                "text-area-background: rgb(23, 23, 23);"+
//                        "-fx-focus-color: rgb(23, 23, 23) ; " +
//                        "-fx-faint-focus-color: rgb(23, 23, 23) ;"+
//                        "-fx-highlight-fill: rgb(23, 23, 23);" +
//                        //"-fx-highlight-text-fill: white;"+
//                        "-fx-background-color: rgb(23, 23, 23);" +
//                        "-fx-background-radius: 0;" +
//                        "-fx-control-inner-background: rgb(23, 23, 23); "
//                        //"-fx-text-fill: white;"
//                         );



//        +
//                "-fx-highlight-fill: rgb(23, 23, 23); " +
//                "-fx-highlight-text-fill: rgb(23, 23, 23);" +

        //sil.setBackground(Background.fill(Color.rgb(23, 23, 23)));
        // THIS IS WHERE WE WILL HIGHLIGHT
        // WE NEED TO GET THE RANGE HERE
        // WRITE A SILO METHOD THAT RETURNS THE RANGE!

        int[] range = getCommandRange();
        sil.selectRange(range[0], range[1]);
        Label acc = new Label("ACC:\n"+this.interp.getACC() );
        Label bak = new Label("BAK:\n"+this.interp.getBAK() );
        acc.setAlignment(Pos.CENTER);
        bak.setAlignment(Pos.CENTER);


        VBox vbox = new VBox(acc,bak);
        vbox.setSpacing(20);


        acc.setStyle(cssLayout);
        bak.setStyle(cssLayout);
        acc.setTextFill(Paint.valueOf("BLACK"));
        acc.setBackground(Background.fill(Color.WHITE));
        bak.setBackground(Background.fill(Color.WHITE));
        acc.setFont(new Font("Courier New Bold",
                             12));
        bak.setTextFill(Paint.valueOf("BLACK"));
        bak.setFont(new Font("Courier New Bold",
                             12));
        HBox hbox = new HBox(sil, vbox);
        BorderPane bp = new BorderPane(hbox);
        //bp.setCenter(sil);
        //bp.setRight(vbox);
        bp.setStyle(cssLayout);
        bp.setMinSize(170,170);
        bp.setBackground(Background.fill(Color.WHITE));

        String cssLayout2 = "-fx-border-color: BLACK ;\n" +
                "-fx-border-outsets: 5;\n" +
                "-fx-border-insets: 3;\n"+
                "-fx-border-width: 3;\n";

        acc.setStyle(cssLayout2);
        bak.setStyle(cssLayout2);

        bp.setStyle(cssLayout2);


        return bp;

    }

    public void runSilo(){
        siloThread.run();
    }

    public void startSilo(){
        siloThread.start();
    }
    public void pauseSilo(){
        interp.pause();
    }
    public void resumesilo(){
        interp.resume();
    }
    public void stepsilo(){
        interp.step();
    }

    public ArrayList<String> getSiloCommands(){
        return this.interp.getCommands();
    }

    public int[] getCommandRange(){
        int[] pair = new int[2];
        int instruct = this.interp.getCount();
        int totalChar = 0;
        int size = getSiloCommands().size();
        if (instruct > size -1){
            instruct = 0;
        }
        for (int i = 0; i < instruct; i++){
            totalChar += getSiloCommands().get(i).length();
            totalChar++;
        }
        pair[0] = totalChar;
        pair[1] = totalChar + getSiloCommands().get(instruct).length();

        return pair;
    }
}
