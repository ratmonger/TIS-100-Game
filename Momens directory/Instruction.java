public interface Instruction {
    void execute(Ports upPort, Ports downPort,Ports leftPort,Ports rightPort,SiloRegisters registers, int inputPort);
}

class Noop implements Instruction {
    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        // do nothing
    }
}
class Move implements Instruction {
    private final String src;
    private final String  dst;
    int srcValue;
    Parser parser = new Parser();

    public Move(String src, String dst) {
        this.src = src;
        this.dst = dst;
    }
    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        parser.ReadParser("SampleText.txt");
        if (src.equals("ACC")) {
            srcValue = registers.getAcc();
        } else if (src.equals("UP") && upPort == null ) {
            srcValue = inputPort;
        }else if (src.equals("LEFT")) {
            srcValue = leftPort.getOutport();
        } else if (src.equals("RIGHT")) {
            srcValue = rightPort.getOutport();
        } else if (src.equals("UP")) {
            srcValue = upPort.getOutport();
        } else if (src.equals("DOWN")) {
            srcValue = downPort.getOutport();
        }
        else {
            return;
        }

        if (dst.equals("ACC")) {
            registers.setAcc(srcValue);
        } else {
            if (dst.equals("UP")) {
                upPort.setOutport(srcValue);
            } else if (dst.equals("DOWN")) {
                downPort.setOutport(srcValue);
            } else if (dst.equals("LEFT")) {
                leftPort.setOutport(srcValue);
            } else if (dst.equals("RIGHT")) {
                rightPort.setOutport(srcValue);
            } else if (dst.equals("NIL")) {

            } else {
            }
        }
    }
}
class Swap implements Instruction {
    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        registers.setAcc(registers.getBackup());
    }
}

// SAVE
class Save implements Instruction {
    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
       registers.setBak(registers.getAcc());
    }
}

class Add implements Instruction{
    private final String src;

    public Add(String src) {
        this.src = src;
    }

    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        int srcValue = 0;
        if (src.equals("UP")) {
            srcValue = upPort.getOutport();
        } else if (src.equals("DOWN")) {
            srcValue = downPort.getOutport();
        } else if (src.equals("RIGHT") ) {
            srcValue = rightPort.getOutport();
        } else if (src.equals("LEFT")) {
            srcValue = leftPort.getOutport();
        } else {
            try {
                srcValue = Integer.parseInt(src);
            } catch (NumberFormatException e) {
                System.out.println();
            }
        }
        registers.setAcc(registers.getAcc() + srcValue);
    }
}


class Sub implements Instruction {
    private final String src;

    public Sub(String src) {
        this.src = src;
    }

    @Override
    public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        int srcValue = 0;
        if (src.equals("UP")) {
            srcValue = upPort.getOutport();
        } else if (src.equals("DOWN")) {
            srcValue = downPort.getOutport();
        } else if (src.equals("RIGHT") ) {
            srcValue = rightPort.getOutport();
        } else if (src.equals("LEFT")) {
            srcValue = leftPort.getOutport();
        } else {
            try {
                srcValue = Integer.parseInt(src);
            } catch (NumberFormatException e) {
                System.out.println();
            }
        }
        registers.setAcc(registers.getAcc() - srcValue);
    }
}

    class Negate implements Instruction {

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        registers.setAcc(-registers.getAcc());
        }
    }

    class Jump implements Instruction {
        private final Object label;

        public Jump(Object label) {
            this.label = label;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {

        }
    }

    class JEZ implements Instruction {
        private final Object label;

        public JEZ(Object label) {
            this.label = label;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        }
    }

    class JNZ implements Instruction {
        private final Object label;

        public JNZ(String label) {
            this.label = label;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {

        }
    }

    class JGZ implements Instruction {
        private final Object label;

        public JGZ(Object label) {
            this.label = label;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        }
    }

    class JLZ implements Instruction {
        private final Object label;

        public JLZ(Object label) {
            this.label = label;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        }
    }

    class JRO implements Instruction {
        private final Object src;

        public JRO(Object src) {
            this.src = src;
        }

        @Override
        public void execute(Ports upPort, Ports downPort, Ports leftPort, Ports rightPort, SiloRegisters registers, int inputPort) {
        }
    }




