package client;

import java.io.IOException;

public class Sistema {
    private static Sistema sistema;
    private SocketServer server;
    
    private Sistema() {
        server = new SocketServer();
        iniciarServer();
    }
    
    public static synchronized Sistema getInstance() {
        if (sistema == null) {
            sistema = new Sistema();
        }
        return sistema;
    } 
    
    private void iniciarServer() {
        try {
            server.abrirServer(Port.Receptor.getValue(), Port.Directorio.getValue());
        } catch (IOException e) {
        }
    }
    
    
}
