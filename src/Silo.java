import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        return null;
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
