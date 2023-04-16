import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Silo {
    private final SiloRegisters siloRegisters;
    private final Ports ports;
    private  InOutPorts inoutports;
    private final List<String> instructions;
    Thread siloThread;

    public Silo(List<String> instructions){

        siloRegisters = new SiloRegisters();
        ports = new Ports();
        this.instructions = instructions;
    }

    public List<String> getInstruction() {
        return instructions;
    }

    public Ports getPorts(){
        return ports;
    }

    public InOutPorts getInoutports() {
        return inoutports;
    }

    public Thread getSiloThread() {
        return siloThread;
    }

    public SiloRegisters getSiloRegisters() {
        return siloRegisters;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String instruction : instructions) {
            sb.append(instruction).append("\n");
        }
        return sb.toString();
    }
}

