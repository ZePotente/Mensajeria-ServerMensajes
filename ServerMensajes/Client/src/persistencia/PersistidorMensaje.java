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
    private static final String NOMBRE_ARCHIVO = "usuariosDatabase.txt";
    public PersistidorMensaje() {
        super();
    }
    
    public void persistir(ArrayList<String> mensajes, boolean reemplazarInfoActual) throws IOException {
        try(FileWriter fw = new FileWriter(PersistidorMensaje.NOMBRE_ARCHIVO, !reemplazarInfoActual);
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
    public String recuperarPersistencia() {
        String mensajes = "";
        try (FileReader fr = new FileReader(PersistidorMensaje.NOMBRE_ARCHIVO);
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
