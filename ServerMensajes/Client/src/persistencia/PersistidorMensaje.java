package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

public class PersistidorMensaje implements IPersistencia {
    private static final String NOMBRE_ARCHIVO = "mensajesSinAvisoDeRecepcion";
    private static final String NOMBRE_ARCHIVO_RECEPCION = "mensajesConAvisoDeRecepcion.txt";
    public PersistidorMensaje() {
        super();
    }
    
    public void persistir(ArrayList<String> mensajes, boolean reemplazarInfoActual, boolean mensajeAvisoRecepcion) throws IOException {
        String archivoAEscribir = mensajeAvisoRecepcion ? NOMBRE_ARCHIVO : NOMBRE_ARCHIVO_RECEPCION;
        try(FileWriter fw = new FileWriter(archivoAEscribir, !reemplazarInfoActual);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (int i = 0; i<mensajes.size(); i++) {
                out.println(mensajes.get(i));
            }
            out.close();
            bw.close();
            fw.close();
        }
    }

    @Override
    public String recuperarPersistencia(boolean mensajesConAvisoDeRecepcion) {
        String mensajes = "";
        String archivoALeer = mensajesConAvisoDeRecepcion ? NOMBRE_ARCHIVO : NOMBRE_ARCHIVO_RECEPCION;
        try (FileReader fr = new FileReader(archivoALeer);
            BufferedReader br = new BufferedReader(fr))
        {
            String st; 
            while ((st = br.readLine()) != null)
                if (!st.isEmpty()) {
                    mensajes += st+"\n"; 
                }
            br.close();
            fr.close();
        } catch (IOException e) {
            
        }
        return mensajes;
    }
}
