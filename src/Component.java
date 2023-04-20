import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public interface Component {

    /* interface providing functionality for all objects of type Component
    this allows the Silo, Ports, In/Out to share common methods
     */



    /**
     * returns GUI representation of the component
     * @return Borderpane GUI information
     */
    BorderPane toGUI();
}
