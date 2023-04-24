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

import javafx.scene.layout.BorderPane;

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
