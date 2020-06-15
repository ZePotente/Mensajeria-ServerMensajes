package modelo_m;

public enum Port {
    Receptor(123),
    ServerMensajes(200),
    EmisorRecepcion(300);
    
    private final int value;
    
    private Port(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
