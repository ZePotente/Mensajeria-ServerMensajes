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
            persistirMensaje(mensaje, false);
        } 
    }
    
    
    public void persistirMensaje(ArrayList<String> mensaje, boolean reemplazaListaExistente) {
        try {
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
                if (agenda.isUserOnline(infoMensaje.get(i))) { // Si esta online, debo enviar el mensaje que estaba persistido
                    ArrayList<String> mensajeAEnviar = new ArrayList<String>(infoMensaje.subList(i, i+3));
                    enviarMensaje(mensajeAEnviar);
                } else {
                    auxListaMensajes.add(infoMensaje.get(i));
                    auxListaMensajes.add(infoMensaje.get(i+1));
                    auxListaMensajes.add(infoMensaje.get(i+2));
                }
            }
            persistirMensaje(auxListaMensajes, true);
            }
    }
}
