package client;

import java.io.IOException;

public class Sistema {
    private static Sistema sistema;
    private SocketServer server;
    private Agenda agenda;
    
    private Sistema() {
        agenda = new Agenda();
        server = new SocketServer(agenda);
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
            server.abrirServer();
            server.actualizaListaUsuarios("192.168.0.41", 100);
        } catch (IOException e) {
        }
    }
    
    
}