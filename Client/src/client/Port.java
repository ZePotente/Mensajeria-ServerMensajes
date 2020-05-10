package client;

public enum Port {
    Directorio(123),
    Receptor(200);
    
    private final int value;
    
    private Port(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
