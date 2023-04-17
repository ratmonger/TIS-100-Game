import java.util.concurrent.BlockingQueue;

public class OutPort extends Port {


    public OutPort(int x){
        super(x);
    }

    @Override
    public BlockingQueue<Integer> getInQueue() {
        return null;
    }

}

