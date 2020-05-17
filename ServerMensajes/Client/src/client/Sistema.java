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
    private Configuracion config;
    
    private Sistema() throws NoLecturaConfiguracionException {
        config = LectorConfiguracion.leerConfig(Sistema.ARCH_CONFIG);
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
            server.abrirServer(config.getNroIPDirectorio());
            server.actualizaListaUsuarios(config.getNroIPDirectorio(), Port.Directorio.getValue());
        } catch (IOException e) {
        }
    }
    
    
}
