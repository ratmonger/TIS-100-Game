import java.util.concurrent.BlockingQueue;

public class InputPort extends Port {

    @Override
    public BlockingQueue<Integer> getOutQueue() {
        return null;
    }

}
