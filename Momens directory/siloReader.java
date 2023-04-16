public class siloReader {

    public void Reader(String[] inputTokens,Silo[][]siloCamp, SiloRegisters registers, int row, int col, int inputPort) {
        Instruction instruction = null;
        SiloGrid siloGrid = new SiloGrid();
        switch (inputTokens[0]) {
            case "NOOP":
                instruction = new Noop();
                break;
            case "MOVE":
                if (inputTokens.length == 3) {
                    instruction = new Move(inputTokens[1], inputTokens[2]);
                } else {
                    System.out.println("Incorrect Syntax");
                }
                break;
            case "SWAP":
                instruction = new Swap();
                break;
            case "SAVE":
                instruction = new Save();
                break;
            case "ADD":
                if (inputTokens.length == 2) {
                    instruction = new Add(inputTokens[1]);
                }
                break;
            case "SUB":
                if (inputTokens.length == 2) {
                    instruction = new Sub(inputTokens[1]);
                }
                break;
            case "JUMP":
                if (inputTokens.length == 2) {
                    instruction = new Jump(inputTokens[1]);
                }
                break;
            case "NEGATE":
                instruction = new Negate();
                break;
            case "JEZ":
                if (inputTokens.length == 2) {
                    instruction = new JEZ(inputTokens[1]);
                }
                break;
            case "JNZ":
                if (inputTokens.length == 2) {
                    instruction = new JNZ(inputTokens[1]);
                }
                break;
            case "JGZ":
                if (inputTokens.length == 2) {
                    instruction = new JGZ(inputTokens[1]);
                }
                break;
            case "JLZ":
                if (inputTokens.length == 2) {
                    instruction = new JLZ(inputTokens[1]);
                }
                break;
            case "JRO":
                if (inputTokens.length == 2) {
                    instruction = new JRO(inputTokens[1]);
                }
                break;
            case "END":
                break;
            default:
                System.out.println("Incorrect Syntax");
                break;
        }
        if (instruction != null) {
            siloGrid.executeInstructionAt(siloCamp,row,col,instruction,registers,inputPort);
        }
    }
}
