package modelo_m;

import modelo_m.agenda.Agenda;

import configuracion.Configuracion;

import configuracion.LectorConfiguracion;

import configuracion.NoLecturaConfiguracionException;

import java.io.IOException;

public class SistemaM {
    private static SistemaM sistema;
    private static String ARCHIVO_CONFIG = "configuracion.txt";
    
    private SocketServer server;
    private Agenda agenda;
    private Configuracion config;
    
    private SistemaM() throws NoLecturaConfiguracionException {
        config = LectorConfiguracion.leerConfig(ARCHIVO_CONFIG);
        agenda = new Agenda();
        server = new SocketServer(agenda);
    }
    
    public static synchronized SistemaM getInstance() {
        if (sistema == null) {
            try {
                sistema = new SistemaM();
            } catch (NoLecturaConfiguracionException e) {
                System.out.println("Error al leer el archivo de configuracion. Por favor reinicie el programa.");
            }
        }
        return sistema;
    } 
    
    public void iniciarServer() {
        try {
            server.abrirServer();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor.");
        }
    }

    public Configuracion getConfig() {
        return config;
    }
}
