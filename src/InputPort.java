import java.util.concurrent.BlockingQueue;

public class InputPort extends Port {

    public InputPort(int x){
        super(x);
    }

    @Override
    public BlockingQueue<Integer> getOutQueue() {
        return null;
    }

}
