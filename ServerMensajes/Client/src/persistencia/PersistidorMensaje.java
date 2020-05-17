package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PersistidorMensaje implements IPersistencia {
    private static final String NOMBRE_ARCHIVO = "usuariosDatabase.txt";
    public PersistidorMensaje() {
        super();
    }
    
    public void persistir(String mensaje, boolean reemplazarInfoActual) throws IOException {
        try(FileWriter fw = new FileWriter(PersistidorMensaje.NOMBRE_ARCHIVO, !reemplazarInfoActual);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(mensaje);
        }
    }

    @Override
    public String recuperarPersistencia() {
        String mensajes = "";
        try (FileReader fr = new FileReader(PersistidorMensaje.NOMBRE_ARCHIVO);
            BufferedReader br = new BufferedReader(fr))
        {
            String st; 
            while ((st = br.readLine()) != null) 
                mensajes += st+"\n"; 
                
        } catch (IOException e) {
            
        }
        return mensajes;
    }
}
