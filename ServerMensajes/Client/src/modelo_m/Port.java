package modelo_m;

public enum Port {
    ServerMensajes(200),
    Directorio(100),
    Receptor(123);
    
    private final int value;
    
    private Port(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
