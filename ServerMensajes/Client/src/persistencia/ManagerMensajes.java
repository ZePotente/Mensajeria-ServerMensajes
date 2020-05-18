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
    
    
    public boolean enviarMensaje(ArrayList<String> mensaje) {
        Socket socket;
        try {
            socket = new Socket(mensaje.get(1).trim(), Port.Receptor.getValue());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje.get(2));
            socket.close();
            return true;
        } catch (IOException e) {
            persistirMensaje(mensaje, false, mensaje.size() == 4);
            return false;
        } 
    }
    
    public void notificarEnvioMensaje(String nroIP) {
        // Notificar envio al emisor
    }
    
    public void persistirMensaje(ArrayList<String> mensaje, boolean reemplazaListaExistente, boolean isMensajeRecepcion) {
        try {
            if (isMensajeRecepcion) {
                mensaje.add(0, "Recepcion");
            }
            // Si fallo la ultima persistencia, la cache permite que se guarde cuando llegue un nuevo mensaje y asi no perder ese mensaje
            if (cache != null && !cache.isEmpty()) {
                mensaje.addAll(cache);
            }
            persistidor.persistir(mensaje, reemplazaListaExistente);
        } catch (IOException e) {
            cache.addAll(mensaje);
        }
    }
    
    public void chequearMensajesPendientes() {
        String mensajes = persistidor.recuperarPersistencia();
        ArrayList<String> auxListaMensajes = new ArrayList<>();
        if (mensajes != null && !mensajes.isEmpty()) {
            String[] lines = mensajes.split("\n");
            ArrayList<String> infoMensaje = new ArrayList<>();
            Collections.addAll(infoMensaje, lines);
            // La primera linea representa al nombre. La segunda la IP y la tercera info del mensaje. Cada mensaje ocupa 3 lineas
            for (int i = 0; i<infoMensaje.size(); i+=3) {
                boolean mensajeRecepcion = false;
                if (infoMensaje.get(0).equalsIgnoreCase("Recepcion")) {
                    i++;
                    mensajeRecepcion = true;
                }
                if (agenda.isUserOnline(infoMensaje.get(i))) { // Si esta online, debo enviar el mensaje que estaba persistido
                    ArrayList<String> mensajeAEnviar = new ArrayList<String>(infoMensaje.subList(i, i + 3 + (mensajeRecepcion ? 1 : 0)));
                    enviarMensaje(mensajeAEnviar);
                } else {
                    if (mensajeRecepcion) {
                        auxListaMensajes.add("Recepcion");
                    }
                    for (int j = 0; j < 3 + (mensajeRecepcion ? 1 : 0); i++) {
                        auxListaMensajes.add(infoMensaje.get(i));
                    }
                    
                }
            }
            persistirMensaje(auxListaMensajes, true);
            }
    }
}
