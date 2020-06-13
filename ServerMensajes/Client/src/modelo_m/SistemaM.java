package modelo_m;

import modelo_m.agenda.Agenda;

import modelo_m.configuracion.Configuracion;

import modelo_m.configuracion.LectorConfiguracion;

import modelo_m.configuracion.NoLecturaConfiguracionException;

import java.io.IOException;

public class SistemaM {
    private static SistemaM sistema;
    private static String ARCH_CONFIG = "configuracion.txt";
    
    private SocketServer server;
    private Agenda agenda;
    private Configuracion config;
    
    private SistemaM() throws NoLecturaConfiguracionException {
        config = LectorConfiguracion.leerConfig(SistemaM.ARCH_CONFIG);
        agenda = new Agenda();
        server = new SocketServer(agenda);
        iniciarServer();
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
    
    private void iniciarServer() {
        try {
            server.abrirServer(config.getNroIPDirectorio());
            server.actualizaListaUsuarios(config.getNroIPDirectorio(), Port.Directorio.getValue());
        } catch (IOException e) {
        }
    }
    
    
}
