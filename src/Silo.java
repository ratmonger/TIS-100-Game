import javafx.scene.layout.HBox;

public class Silo implements Component{

    private Thread siloThread;
    private Interpreter interp;

    public Silo (Thread t, Interpreter interp){
        this.siloThread = t;
        this.interp = interp;
    }

    @Override
    public HBox toGUI() {
        return null;
    }
}
