package client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketServer {
    private Agenda agenda;
    private PersistidorMensaje persistidor;
    public SocketServer() {
        agenda = new Agenda();
    }
    
    public void abrirServer(int nroPuerto, int nroPuertoDirectorio) throws IOException {
        executePeriodUsersRequest(nroPuertoDirectorio);
            new Thread() {
                public void run() {
                    try {
                        ServerSocket s = new ServerSocket(nroPuerto);
                        while (true) { // una vez que escucha ese puerto se queda escuchandolo aunque ingresen otro puerto
                            Socket soc = s.accept();
                            BufferedReader objectIn = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                            String aux;
                            String nombre = objectIn.readLine();
                            if (agenda.isUserOnline(nombre)) {
                                enviarMensaje(nombre, objectIn);
                            } else { // Persistir mensaje
                                persistirMensaje(objectIn);
                            }
                            soc.close(); // deberian?
                        }
                    } catch (Exception e) {
                    }
                    finally {
                    }
                }
            }.start();
    }
    
    public void enviarMensaje(String nroIP, BufferedReader mensaje) throws IOException {
        Socket socket = new Socket(nroIP.trim(), 123);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensaje);
        out.close();
        socket.close();
    }
    
    public void persistirMensaje(BufferedReader mensaje) {
        //persistidor.persistir(mensaje);
    }
    
    public void actualizaListaUsuarios(String nroIPDirectorio, int nroPuertoDirectorio) throws IOException {
        Socket socket = new Socket(nroIPDirectorio.trim(), nroPuertoDirectorio);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader objectIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("RequestReceptoresServidor");
                
        // leida de la lista de receptores
        String lista = objectIn.readLine();
        socket.close();
        objectIn.close();
        out.close();
        agenda.actualizarUsuarios(lista);
    }
    
    public void executePeriodUsersRequest(int nroPuertoDirectorio) {
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.scheduleAtFixedRate(
            new Runnable() {
                @Override
                public void run() {
                try {
                    actualizaListaUsuarios("123", nroPuertoDirectorio);
                } catch (IOException e) {
                }
            }
            }, 
            0, 7, TimeUnit.SECONDS);
    }
}
