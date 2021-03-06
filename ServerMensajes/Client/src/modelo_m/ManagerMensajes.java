package modelo_m;


import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo_m.agenda.Agenda;

import modelo_m.persistencia.IPersistencia;
import modelo_m.persistencia.PersistidorMensaje;

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
            if (mensaje.size() == 4) { // Mensaje con aviso de recepcion
                notificarEnvioDeMensaje(mensaje.get(3), mensaje.get(0)); // Numero de IP del emisor
            }
        } catch (IOException e) {
            persistirMensaje(mensaje, false, mensaje.size() == 4);
        } 
    }
    
    private void notificarEnvioDeMensaje(String nroIP, String nombre) {
        Socket socket;
        try {
            // Aca poner bien el puerto para notificar el envio y el mensaje a enviar tambien
            socket = new Socket(nroIP.trim(), Port.EmisorRecepcion.getValue());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nombre);
            socket.close();
        } catch (IOException e) {
            System.out.println("Fallo en el envio de notificacion de mensaje.");
            e.printStackTrace();
            System.out.println(nombre);
            System.out.println(nroIP);
        }
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
                    for (int j=i; j<paso+i; j++) {
                        auxListaMensajes.add(infoMensaje.get(j));
                    }
                }
            }
            persistirMensaje(auxListaMensajes, true, mensajesConAvisoDeRecepcion);
        }
    }
}
