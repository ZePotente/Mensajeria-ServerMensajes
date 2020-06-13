package persistencia;

import client.Agenda;
import client.Port;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerMensajes {
    private IPersistencia persistidor;
    private Agenda agenda;
    private ArrayList<String> cache = new ArrayList<String>();
    public ManagerMensajes(Agenda agenda) {
        persistidor = new PersistidorMensaje();
        this.agenda = agenda;
    }
    
    
    public void enviarMensaje(ArrayList<String> mensaje) {
        Socket socket;
        try {
            socket = new Socket(mensaje.get(1).trim(), Port.Receptor.getValue());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje.get(2));
            socket.close(); 
        } catch (IOException e) {
            persistirMensaje(mensaje, false, mensaje.size() == 4);
        } 
    }
    
    private void notificarEnvioDeMensaje() {
        
    }
    
    public void persistirMensaje(ArrayList<String> mensaje, boolean reemplazaListaExistente, boolean mensajeAvisoRecepcion) {
        try {
            // Si fallo la ultima persistencia, la cache permite que se guarde cuando llegue un nuevo mensaje y asi no perder ese mensaje
            if (cache != null && !cache.isEmpty()) {
                mensaje.addAll(cache);
            }
            persistidor.persistir(mensaje, reemplazaListaExistente, mensajeAvisoRecepcion);
        } catch (IOException e) {
            cache.addAll(mensaje);
        }
    }
    
    public void chequearMensajesPendientes(boolean mensajesConAvisoDeRecepcion) {
        String mensajes = persistidor.recuperarPersistencia(mensajesConAvisoDeRecepcion);
        ArrayList<String> auxListaMensajes = new ArrayList<>();
        if (mensajes != null && !mensajes.isEmpty()) {
            String[] lines = mensajes.split("\n");
            ArrayList<String> infoMensaje = new ArrayList<>();
            Collections.addAll(infoMensaje, lines);
            // La primera linea representa al nombre. La segunda la IP y la tercera info del mensaje. Cada mensaje ocupa 3 o 4 lineas (dependiendo si es de recepcion)
            int paso = mensajesConAvisoDeRecepcion ? 4 : 3;
            for (int i = 0; i<infoMensaje.size(); i+=paso) {
                if (agenda.isUserOnline(infoMensaje.get(i))) { // Si esta online, debo enviar el mensaje que estaba persistido
                    ArrayList<String> mensajeAEnviar = new ArrayList<String>(infoMensaje.subList(i, i+paso));
                    enviarMensaje(mensajeAEnviar);
                } else {
                    for (int j=0; j<paso; j++) {
                        auxListaMensajes.add(infoMensaje.get(i));
                    }
                }
            }
            persistirMensaje(auxListaMensajes, true, mensajesConAvisoDeRecepcion);
        }
    }
}
