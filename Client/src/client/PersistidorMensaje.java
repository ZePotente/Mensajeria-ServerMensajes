package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PersistidorMensaje implements IPersistencia {
    public PersistidorMensaje() {
        super();
    }
    
    public void persistir(String mensaje, boolean reemplazarInfoActual) throws IOException {
        try(FileWriter fw = new FileWriter("usuariosDatabase.txt", !reemplazarInfoActual);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(mensaje);
        }
    }

    @Override
    public String recuperarPersistencia() {
        String mensajes = "";
        try (FileReader fr = new FileReader("usuariosDatabase.txt");
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
