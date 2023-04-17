import java.util.concurrent.BlockingQueue;

public class OutPort extends Port {

    @Override
    public BlockingQueue<Integer> getInQueue() {
        return null;
    }

}

