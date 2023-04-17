import javafx.scene.layout.HBox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port implements Runnable, Component {

    private final BlockingQueue<Integer> inQueue;
    private final BlockingQueue<Integer> outQueue;
    private int rowOffset;

    public Port(int x) {
        rowOffset = x;
        this.inQueue = new LinkedBlockingQueue<>();
        this.outQueue = new LinkedBlockingQueue<>();
    }

    // returns the first element
    public int peekPort(){
        return outQueue.peek();
    }

    // removes and returns the first element
    public int popPort(){
        return outQueue.poll();
    }

    // if 0, port is located above/below silos
    // if 1, port is left/right of silo
    public int getRowOffset(){
        return rowOffset;
    }


    public BlockingQueue<Integer> getInQueue() {
        return inQueue;
    }
    public BlockingQueue<Integer> getOutQueue() {
        return outQueue;
    }
    @Override
    public void run() {
        System.out.println("waiting for input");
    }

    @Override
    public HBox toGUI() {
        return null;
    }
}

