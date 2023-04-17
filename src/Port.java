import javafx.scene.layout.HBox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port implements Runnable, Component {

    private final BlockingQueue<Integer> inQueue;
    private final BlockingQueue<Integer> outQueue;

    public Port() {
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

