import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
}
