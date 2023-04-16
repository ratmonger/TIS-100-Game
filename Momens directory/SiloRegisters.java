public class SiloRegisters {
    private int Backup;
    private int Acc;

    public SiloRegisters(){
        this.Backup = 0;
        this.Acc = 0;
    }

    public int getAcc() {
        return Acc;
    }

    public void setAcc(int acc) {
        this.Acc = acc;
    }
    public int getBackup() {
        return Backup;
    }
    public void setBak(int bak) {
        this.Backup = bak;
    }
}
