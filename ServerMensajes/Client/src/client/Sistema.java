package client;

import configuracion.Configuracion;

import configuracion.LectorConfiguracion;

import configuracion.NoLecturaConfiguracionException;

import java.io.IOException;

public class Sistema {
    private static Sistema sistema;
    private static String ARCH_CONFIG = "configuracion.txt";
    
    private SocketServer server;
    private Agenda agenda;
    private String NRO_IP_DIRECTORIO = "";
    
    private Sistema() throws NoLecturaConfiguracionException {
        Configuracion config = LectorConfiguracion.leerConfig(Sistema.ARCH_CONFIG);
        this.NRO_IP_DIRECTORIO = config.getNroIPDirectorio();
        agenda = new Agenda();
        server = new SocketServer(agenda);
        iniciarServer();
    }
    
    public static synchronized Sistema getInstance() {
        if (sistema == null) {
            try {
                sistema = new Sistema();
            } catch (NoLecturaConfiguracionException e) {
                System.out.println("Error al leer el archivo de configuracion. Por favor reinicie el programa.");
            }
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
