package client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.io.StringReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SocketServer {
    private Agenda agenda;
    private ManagerMensajes managerMensajes;
    public SocketServer(Agenda agenda) {
        this.agenda = agenda;
        managerMensajes = new ManagerMensajes(agenda);
    }
    
    public void abrirServer() throws IOException {
        executePeriodUsersRequest(Port.Directorio.getValue());
            new Thread() {
                public void run() {
                    try {
                        ServerSocket s = new ServerSocket(Port.ServerMensajes.getValue());
                        while (true) { // una vez que escucha ese puerto se queda escuchandolo aunque ingresen otro puerto
                            Socket soc = s.accept();
                            BufferedReader objectIn = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                            String aux;
                            String nombre = objectIn.readLine();
                            if (agenda.isUserOnline(nombre)) {
                                managerMensajes.enviarMensaje(nombre, objectIn.lines().collect(Collectors.joining("\n")));
                            } else { // Persistir mensaje
                                managerMensajes.persistirMensaje(objectIn.lines().collect(Collectors.joining()), false);
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
