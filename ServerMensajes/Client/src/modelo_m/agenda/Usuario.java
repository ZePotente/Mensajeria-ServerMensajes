package modelo_m.agenda;

public class Usuario {
    private String nombre;
    private String numeroDeIP;
    private boolean estado;

    public Usuario(String nombre, String numeroDeIP) {
        this.nombre = nombre;
        this.numeroDeIP = numeroDeIP;
    }
        
    public String getNumeroDeIP() {
         return numeroDeIP;
    }
        
    public boolean getEstado() {
        return estado;
    }
        
    public String getNombre() {
        return nombre;
    }
        
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

