//public interface Instruction {
//    void execute();
//}
//
//class Noop implements Instruction {
//    @Override
//    public void execute() {
//        // do nothing
//    }
//}
//class Move implements Instruction {
//    private final String src;
//    private final String  dst;
//
//    public Move(String src, String dst) {
//        this.src = src;
//        this.dst = dst;
//    }
//    @Override
//    public void execute() {
//        if (src.equals("ACC")) {
//            srcValue = siloCamp[row][col].getSiloRegisters().getAcc();
//        } else if (src.equals("LEFT") && col > 0) {
//            srcValue = siloCamp[row][col - 1].getPorts().getInport();
//        } else if (src.equals("RIGHT") && col < siloCamp[row].length - 1) {
//            srcValue = siloCamp[row][col + 1].getPorts().getInport();
//        } else if (src.equals("UP") && row > 0) {
//            srcValue = siloCamp[row - 1][col].getPorts().getInport();
//        } else if (src.equals("DOWN") && row < siloCamp.length - 1) {
//            srcValue = siloCamp[row + 1][col].getPorts().getInport();
//        } else if (src.equals("UP") && row == parser.inputRow && col == parser.inputCol) {
//            srcValue = ports.inport;
//            System.out.println("GUNG BUNG");
//        } else {
//            return;
//        }
//
//        if (dst.equals("ACC")) {
//            siloCamp[row][col].getSiloRegisters().setAcc(srcValue);
//        } else {
//            if (dst.equals("UP") && row > 0) {
//                siloCamp[row - 1][col].getPorts().setOutport(srcValue);
//            } else if (dst.equals("DOWN") && row < siloCamp.length - 1) {
//                siloCamp[row + 1][col].getPorts().setOutport(srcValue);
//            } else if (dst.equals("LEFT") && col > 0) {
//                siloCamp[row][col - 1].getPorts().setOutport(srcValue);
//            } else if (dst.equals("RIGHT") && col < siloCamp[row].length - 1) {
//                siloCamp[row][col + 1].getPorts().setOutport(srcValue);
//            } else if (dst.equals("NIL")) {
//
//            } else {
//            }
//        }
//    }
//}
//class Swap implements Instruction {
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//        siloCamp[row][col].getSiloRegisters().setAcc(siloCamp[row][col].getSiloRegisters().getBackup());
//    }
//}
//
//// SAVE
//class Save implements Instruction {
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//        siloCamp[row][col].getSiloRegisters().setBak(siloCamp[row][col].getSiloRegisters().getAcc());
//    }
//}
//
//class Add implements Instruction{
//    private final String src;
//
//    public Add(String src) {
//        this.src = src;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//        int srcValue = 0;
//        if (src.equals("UP") && row - 1 >= 0) {
//            srcValue = siloCamp[row - 1][col].getSiloRegisters().getAcc();
//        } else if (src.equals("DOWN") && row + 1 < siloCamp.length) {
//            srcValue = siloCamp[row + 1][col].getSiloRegisters().getAcc();
//        } else if (src.equals("RIGHT") && col + 1 < siloCamp[0].length) {
//            srcValue = siloCamp[row][col + 1].getSiloRegisters().getAcc();
//        } else if (src.equals("LEFT") && col - 1 >= 0) {
//            srcValue = siloCamp[row][col - 1].getSiloRegisters().getAcc();
//        } else {
//            try {
//                srcValue = Integer.parseInt(src);
//            } catch (NumberFormatException e) {
//                System.out.println();
//            }
//        }
//
//        siloCamp[row][col].getSiloRegisters().setAcc(siloCamp[row][col].getSiloRegisters().getAcc() + srcValue);
//    }
//}
//
//
//class Sub implements Instruction {
//    private final String src;
//
//    public Sub(String src) {
//        this.src = src;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//        int srcValue = 0;
//
//        if (src.equals("UP") && row - 1 >= 0) {
//            srcValue = siloCamp[row - 1][col].getSiloRegisters().getAcc();
//        } else if (src.equals("DOWN") && row + 1 < siloCamp.length) {
//            srcValue = siloCamp[row + 1][col].getSiloRegisters().getAcc();
//        } else if (src.equals("RIGHT") && col + 1 < siloCamp[0].length) {
//            srcValue = siloCamp[row][col + 1].getSiloRegisters().getAcc();
//        } else if (src.equals("LEFT") && col - 1 >= 0) {
//            srcValue = siloCamp[row][col - 1].getSiloRegisters().getAcc();
//        } else {
//            try {
//                srcValue = Integer.parseInt(src);
//            } catch (NumberFormatException e) {
//                System.out.println();
//            }
//        }
//        siloCamp[row][col].getSiloRegisters().setAcc(siloCamp[row][col].getSiloRegisters().getAcc() - srcValue);
//    }
//}
//class Negate implements Instruction {
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//        siloCamp[row][col].getSiloRegisters().setAcc(siloCamp[row][col].getSiloRegisters().getAcc() * -1);
//    }
//}
//class Jump implements Instruction {
//    private final Object label;
//
//    public Jump(Object label) {
//        this.label = label;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//
//    }
//}
//class JEZ implements Instruction {
//    private final Object label;
//
//    public JEZ(Object label) {
//        this.label = label;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//    }
//}
//class JNZ implements Instruction {
//    private final Object label;
//
//    public JNZ(String label) {
//        this.label = label;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//
//    }
//}
//class JGZ implements Instruction {
//    private final Object label;
//
//    public JGZ(Object label) {
//        this.label = label;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//    }
//}
//class JLZ implements Instruction {
//    private final Object label;
//
//    public JLZ(Object label) {
//        this.label = label;
//    }
//
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//    }
//}
//class JRO implements Instruction {
//    private final Object src;
//
//    public JRO(Object src) {
//        this.src = src;
//    }
//    @Override
//    public void execute(SiloRegisters Register, Silo[][] siloCamp, int row, int col) {
//    }
//}
//
