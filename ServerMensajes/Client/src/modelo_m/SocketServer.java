package modelo_m;

import java.awt.List;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.io.StringReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modelo_m.agenda.Agenda;


public class SocketServer {
    private Agenda agenda;
    private ManagerMensajes managerMensajes;
    public SocketServer(Agenda agenda) {
        this.agenda = agenda;
        managerMensajes = new ManagerMensajes(agenda);
    }
    
    public void abrirServer(String nroIPDirectorio) throws IOException {
        executePeriodUsersRequest(nroIPDirectorio, Port.Directorio.getValue());
            new Thread() {
                public void run() {
                    try {
                        ServerSocket s = new ServerSocket(Port.ServerMensajes.getValue());
                        while (true) { // una vez que escucha ese puerto se queda escuchandolo aunque ingresen otro puerto
                            Socket soc = s.accept();
                            BufferedReader objectIn = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                            String stringCompleto = objectIn.lines().collect(Collectors.joining("\n"));
                            String[] stringSplit = stringCompleto.split("\n");
                            ArrayList<String> infoMensaje = new ArrayList<>();
                            Collections.addAll(infoMensaje, stringSplit);
                            if (stringSplit.length == 3) { // Una linea para nombre, otra IP y otra info del mensaje
                                String nombre = infoMensaje.get(0);
                                if (agenda.isUserOnline(nombre)) {
                                    String nroIP = infoMensaje.get(1);
                                    managerMensajes.enviarMensaje(infoMensaje);
                                } else { // Persistir mensaje
                                    managerMensajes.persistirMensaje(infoMensaje, false);
                                }
                            }
                            soc.close(); // deberian?
                        }
                    } catch (Exception e) {
                        System.out.println("Fallo");
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
        out.println("RequestReceptores");
                
        // leida de la lista de receptores
        String lista = objectIn.readLine();
        socket.close();
        agenda.actualizarUsuarios(lista);
        managerMensajes.chequearMensajesPendientes();
    }
    
    public void executePeriodUsersRequest(String nroIPDirectorio, int nroPuertoDirectorio) {
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.scheduleAtFixedRate(
            new Runnable() {
                @Override
                public void run() {
                try {
                    actualizaListaUsuarios(nroIPDirectorio, nroPuertoDirectorio);
                } catch (IOException e) {
                }
            }
            }, 
            0, 10, TimeUnit.SECONDS);
    }
}
