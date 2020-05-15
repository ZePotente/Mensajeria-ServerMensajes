package client;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

public class ManagerMensajes {
    private IPersistencia persistidor;
    private Agenda agenda;
    private String cache = "";
    public ManagerMensajes(Agenda agenda) {
        persistidor = new PersistidorMensaje();
        this.agenda = agenda;
    }
    
    
    public void enviarMensaje(String nroIP, String mensaje) throws IOException {
        Socket socket;
        socket = new Socket(nroIP.trim(), Port.Receptor.getValue());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensaje);
        out.close();
        socket.close(); 
    }
    
    
    public void persistirMensaje(String mensaje, boolean reemplazaListaExistente) {
        try {
            // Si fallo la ultima persistencia, la cache permite que se guarde cuando llegue un nuevo mensaje y asi no perder ese mensaje
            if (cache != null && !cache.isEmpty()) {
                mensaje += cache;
            }
            persistidor.persistir(mensaje, reemplazaListaExistente);
        } catch (IOException e) {
            cache = mensaje;
        }
    }
    
    public void chequearMensajesPendientes() {
        String mensajes = persistidor.recuperarPersistencia();
        String listaMensajesActualizada = "";
        if (mensajes != null && !mensajes.isEmpty()) {
            String[] lines = mensajes.split(System.getProperty("line.separator"));
            // La primera linea representa al nombre. La segunda la IP y la tercera info del mensaje. Cada mensaje ocupa 3 lineas
            for (int i = 0; i<lines.length; i+=3) {
                if (agenda.isUserOnline(lines[i])) { // Si esta online, debo enviar el mensaje que estaba persistido
                    try {
                        enviarMensaje(lines[i+1], lines[i+2]);
                    } catch (IOException e) {
                        // Si hubo un error al enviar el mensaje, lo vuelvo a persistir para que pueda ser enviado luego
                        listaMensajesActualizada += lines[i]+"\n"+lines[i+1]+"\n"+lines[i+2]+"\n";
                    }
                } else {
                    listaMensajesActualizada += lines[i]+"\n"+lines[i+1]+"\n"+lines[i+2]+"\n";
                }
            }
        }
        // Persiste la nueva lista de mensajes
        persistirMensaje(listaMensajesActualizada, true);
    }
}
