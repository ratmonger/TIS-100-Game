import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Interpreter implements Runnable {

    int ACC;
    int BAK;
    private Port upPort;
    private Port downPort;
    private Port leftPort;
    private Port rightPort;
    static int counter = 0;
    private final CountDownLatch latch;
    private volatile boolean isRunning = false;
    private volatile boolean isStep = false;
    int index = 0;
    private int count = 0;
    private Parcer parcer;


    public Interpreter(Port upPort, Port downPort, Port leftPort,
                       Port rightPort, CountDownLatch latch, int index,
                       Parcer parcer) {
        this.parcer = parcer;
        this.upPort = upPort;
        this.downPort = downPort;
        this.leftPort = leftPort;
        this.rightPort = rightPort;
        this.latch = latch;
        this.index = index;
        this.ACC = 0;
        this.BAK = 0;

    }

    public ArrayList<String> getCommands(){
        return this.parcer.getinstruct().get(index);
    }

    public int getCount(){
        return this.count;
    }

    public int getACC(){
        return ACC;
    }

    public int getBAK(){
        return BAK;
    }

    @Override
    public void run() {

        while (true) {
            synchronized (this) {
                while (!isRunning) {
                    try {
                        this.wait(); // Wait until the flag is set to true
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (count > parcer.getinstruct().get(index).size() - 1)
                count = 0;

            if (this.upPort.getOutQueue() != null && this.upPort.getOutQueue().size() > 0 ||
                    this.downPort.getInQueue() != null && this.downPort.getInQueue().size() > 0 ||
                    this.leftPort.getInQueue() != null && this.leftPort.getInQueue().size() > 0 ||
                    this.rightPort.getOutQueue() != null && this.rightPort.getOutQueue().size() > 0 ){
                continue;
            }

            if (parcer.getinstruct().get(index).size() -1 < count) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                continue;
            }
            String word = (String) parcer.getinstruct().get(index).get(count);
            Scanner sc = new Scanner(word);
            int srcint = 0;
            int dstint = 0;
            String src;
            String dst;
            String label;
            switch (sc.next()) {
                case "JRO":
                    src = sc.next();
                    srcint = srcCase(src);
                    count = ((count + srcint) %
                            parcer.getinstruct().get(index).size()) - 1;
                    break;
                case "JUMP":
                    label = sc.next();
                    count =
                            parcer.getinstruct().get(index).indexOf(":"+label+":") -1;
                    // jump to label? or jump to instruction after label?
                    // remove the -1 to jump to instruction asap

                    // this means we stall for one turn or we immediately
                    // resume instructions
                    break;



                case "JEZ":
                    label = sc.next();
                    if (ACC == 0) {
                        count = parcer.getinstruct().get(index).indexOf(":"+label+":") -
                                1;
                    }
                    break;

                case "JNZ":
                    label = sc.next();
                    if (ACC != 0) {
                        count = parcer.getinstruct().get(index).indexOf(":"+label+":") -
                                1;
                    }
                    break;

                case "JGZ":
                    label = sc.next();
                    if (ACC > 0) {
                        count = parcer.getinstruct().get(index).indexOf(":"+label+":") -
                                1;
                    }
                    break;

                case "JLZ":
                    label = sc.next();
                    if (ACC < 0) {
                        count = parcer.getinstruct().get(index).indexOf(":"+label+":") -
                                1;
                    }
                    break;

                case "ADD":
                    src = sc.next();
                    srcint = srcCase(src);
                    ACC += srcint;
                    break;
                case "SWAP":
                    int temp = ACC;
                    ACC = BAK;
                    BAK = temp;
                    break;
                case "NEGATE":
                    ACC = ACC * -1;
                    break;
                case "NOOP":
                    break;
                case "SUB":
                    src = sc.next();
                    srcint = srcCase(src);
                    ACC -= srcint;
                    break;
                case "MOVE":
                    src = sc.next();
                    dst = sc.next();
                    srcint = srcCase(src);

                    switch (dst) {
                        case "UP":
                            try {
                                upPort.getOutQueue().put(srcint);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "DOWN":
                            try {
                                downPort.getInQueue().put(srcint);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "LEFT":
                            try {
                                leftPort.getInQueue().put(srcint);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "RIGHT":
                            try {
                                rightPort.getOutQueue().put(srcint);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "ACC":
                            ACC = srcint;
                            break;
                        case "NIL":
                            break;
                        //case "BAK":
                        //   BAK = srcint;
                        //  break;
                    }


                    break;
                case "SAVE":
                    BAK = ACC;
                    break;
                default: //label here, do nothing?
            }
            latch.countDown();
            count++;
            System.out.println(
                    Thread.currentThread().getName() + " ACC:" + ACC + " BAK:" +
                            BAK);
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(isStep){
                isStep=false;
                isRunning=false;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public int srcCase(String src) {
        int srcint = 0;
        switch (src) {
            case "UP":
                if (upPort instanceof OutPort) {
                    // upPort is an output port
                } else {
                    try {
                        latch.countDown();
                        srcint = upPort.getInQueue().take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "DOWN":
                if (downPort instanceof OutPort) {
                    // upPort is an output port
                } else {
                    try {
                        latch.countDown();
                        srcint = downPort.getOutQueue().take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "LEFT":
                if (leftPort instanceof OutPort) {
                    // upPort is an output port
                } else {
                    try {
                        latch.countDown();
                        srcint = leftPort.getOutQueue().take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "RIGHT":
                if (rightPort instanceof OutPort) {
                    // upPort is an output port
                } else {
                    try {
                        latch.countDown();
                        srcint = rightPort.getInQueue().take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "ACC":
                srcint = ACC;
                break;
            case "NIL":
                srcint = 0;
            default:
                srcint = Integer.parseInt(src);
        }
        return srcint;
    }
    public void pause() {
        isRunning = false;
    }
    public void resume() {
        isRunning = true;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void step() {
        isStep =true;
        resume();
    }


}
