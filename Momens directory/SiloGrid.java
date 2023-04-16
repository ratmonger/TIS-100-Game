public class SiloGrid {
    Parser parser = new Parser();
    private final Silo[][] siloCamp = parser.siloCamp;
    // Other attributes and methods

    public void executeInstructionAt(Silo[][] siloCamp,int row, int col, Instruction instruction,SiloRegisters registers,  int inport) {
        Ports upPorts = null;
        Ports downPorts = null;
        Ports leftPorts = null;
        Ports rightPorts = null;

        if (row > 0) {
            upPorts = siloCamp[row - 1][col].getPorts();
        }

        if (row < siloCamp.length - 1) {
            downPorts = siloCamp[row + 1][col].getPorts();
        }

        if (col > 0) {
            leftPorts = siloCamp[row][col - 1].getPorts();
        }

        if (col < siloCamp[row].length - 1) {
            rightPorts = siloCamp[row][col + 1].getPorts();
        }

        instruction.execute(upPorts, downPorts , leftPorts, rightPorts, registers ,inport);
    }
}