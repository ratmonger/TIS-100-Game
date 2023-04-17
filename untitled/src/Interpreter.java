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
     int index = 0;
     int count = 0;


    public Interpreter(Port upPort, Port downPort, Port leftPort, Port rightPort, CountDownLatch latch, int index) {
        this.upPort = upPort;
        this.downPort = downPort;
        this.leftPort = leftPort;
        this.rightPort = rightPort;
        this.latch = latch;
        this.index = index;

    }

    @Override
    public void run() {
        Parcer parcer = new Parcer();
        while (true){
            if(count>parcer.getinstruct().get(index).size()-1)
                count=0;
            String word = (String) parcer.getinstruct().get(index).get(count);
            Scanner sc = new Scanner(word);
            switch (sc.next()) {
                case "ADD":
                    ACC = ACC + sc.nextInt();
                    break;
                case "SWAP":
                    int temp = ACC;
                    ACC = BAK;
                    BAK = temp;
                    break;
                case "NEGATE":
                    ACC = ACC *-1;
                    break;
                case "NOOP":
                    break;
                case "SUB":
                    ACC = ACC - sc.nextInt();
                    break;
                case "MOVE":
                    int srcint = 0;
                    int dstint = 0;
                    String src = sc.next();
                    String dst = sc.next();
                    switch (src) {
                        case "UP":
                            if (upPort instanceof InputPort) {
                                try {
                                    srcint =upPort.getInQueue().take();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (upPort instanceof OutPort) {
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
                            if (downPort instanceof InputPort) {
                                try {
                                    srcint =downPort.getInQueue().take();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (upPort instanceof OutPort) {
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
                            if (leftPort instanceof InputPort) {
                                try {
                                    srcint =leftPort.getInQueue().take();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (upPort instanceof OutPort) {
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
                            if (rightPort instanceof InputPort) {
                                try {
                                    srcint =rightPort.getInQueue().take();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (upPort instanceof OutPort) {
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
                            srcint=ACC;
                            break;
                        case "BAK":
                            srcint=BAK;
                            break;
                    }

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
                        case "BAK":
                            BAK = srcint;
                            break;
                    }
                    break;

                case "SAVE":
                    BAK = ACC;
                    break;
            }

            count++;
            System.out.println(Thread.currentThread().getName()+" ACC:" + ACC + " BAK:"+ BAK);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
